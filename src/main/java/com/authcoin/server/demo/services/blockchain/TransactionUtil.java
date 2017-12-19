package com.authcoin.server.demo.services.blockchain;

import com.authcoin.server.demo.services.blockchain.model.UnspentOutput;
import org.bitcoinj.core.*;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.params.QtumTestNetParams;
import org.bitcoinj.script.Script;
import org.bitcoinj.script.ScriptBuilder;
import org.bitcoinj.script.ScriptChunk;
import org.spongycastle.util.encoders.Hex;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

public final class TransactionUtil {

    private static final byte[] VERSION = new byte[]{4};
    private static final int OP_CALL = 194;
    private static final BigDecimal ONE_QTUM = BigDecimal.valueOf(100000000);

    private TransactionUtil() {
    }

    /**
     * Creates a new (bitcoin) script that can be used to call solidity smart contracts.
     */
    public static Script createScript(String abiMethod, String abiParams, int gasLimitInt, int gasPriceInt, String contractAddress) {
        return createScript(abiMethod + abiParams, gasLimitInt, gasPriceInt, contractAddress);
    }

    /**
     * Creates a new (bitcoin) script that can be used to call solidity smart contracts.
     */
    public static Script createScript(String encodedFunction, int gasLimitInt, int gasPriceInt, String contractAddress) {
        ScriptBuilder builder = new ScriptBuilder();
        builder.addChunk(new ScriptChunk(VERSION.length, VERSION));
        builder.number(gasLimitInt);
        builder.number(gasPriceInt);
        builder.data(Hex.decode(encodedFunction));
        builder.data(Hex.decode(contractAddress));
        builder.op(OP_CALL);
        return builder.build();
    }

    /**
     * Creates a new (bitcoin) transaction.
     */
    public static String createTransaction(Script script, List<UnspentOutput> unspentOutputs, List<DeterministicKey> keys, int gasLimit, int gasPrice, BigDecimal feePerKb, BigDecimal fee) {
        if (unspentOutputs.size() == 0) {
            throw new InsufficientFundsException("Insufficient funds");
        }
        // script output
        Transaction transaction = new Transaction(QtumTestNetParams.get());
        transaction.addOutput(Coin.ZERO, script);

        // fee calculation
        BigDecimal gasFee = calculateGasFee(gasLimit, gasPrice);
        BigDecimal totalFee = fee.add(gasFee);
        BigDecimal amountFromOutput = BigDecimal.ZERO;
        BigDecimal overFlow = BigDecimal.ZERO;

        // check if we have enough coins (unspent outputs) to send the transaction
        for (UnspentOutput unspentOutput : unspentOutputs) {
            if (unspentOutput.isOutputAvailableToPay()) {
                overFlow = overFlow.add(unspentOutput.getAmount());
                if (overFlow.doubleValue() >= totalFee.doubleValue()) {
                    break;
                }
            }
        }
        if (overFlow.doubleValue() < totalFee.doubleValue()) {
            throw new InsufficientFundsException("You have insufficient funds for this transaction");
        }

        // Get payback address
        Address myAddress = parseAddress(unspentOutputs);

        // payback output??
        BigDecimal delivery = overFlow.subtract(totalFee);
        if (delivery.doubleValue() != 0.0) {
            transaction.addOutput(Coin.valueOf((long) (delivery.multiply(ONE_QTUM).doubleValue())), myAddress);
        }

        // tx inputs
        for (UnspentOutput unspentOutput : unspentOutputs) {
            //TODO can be optimized
            if (unspentOutput.isOutputAvailableToPay()) {
                for (DeterministicKey deterministicKey : keys) {
                    if (deterministicKey.toAddress(QtumTestNetParams.get()).toString().equals(unspentOutput.getAddress())) {
                        Sha256Hash sha256Hash = Sha256Hash.wrap(Utils.parseAsHexOrBase58(unspentOutput.getTxHash()));
                        TransactionOutPoint outPoint = new TransactionOutPoint(QtumTestNetParams.get(), unspentOutput.getVout(), sha256Hash);
                        Script script2 = new Script(Utils.parseAsHexOrBase58(unspentOutput.getTxoutScriptPubKey()));
                        transaction.addSignedInput(outPoint, script2, deterministicKey, Transaction.SigHash.ALL, true);
                        amountFromOutput = amountFromOutput.add(unspentOutput.getAmount());
                        break;
                    }
                }
                if (amountFromOutput.doubleValue() >= totalFee.doubleValue()) {
                    break;
                }
            }
        }
        // set confidence and purpuse
        transaction.getConfidence().setSource(TransactionConfidence.Source.SELF);
        transaction.setPurpose(Transaction.Purpose.USER_PAYMENT);

        //serialize and return
        byte[] bytes = transaction.unsafeBitcoinSerialize();

        int txSizeInkB = (int) Math.ceil(bytes.length / 1024.);
        BigDecimal minimumFee = (feePerKb.multiply(new BigDecimal(txSizeInkB)));
        if (minimumFee.doubleValue() > fee.doubleValue()) {
            throw new InsufficientFeeException("Insufficient fee. Please use minimum of " + minimumFee.toString() + " QTUM");
        }
        //TODO return byte array instead? or create a new HexString class.
        return Hex.toHexString(bytes);
    }

    private static Address parseAddress(List<UnspentOutput> unspentOutputs) {
        try {
            return Address.fromBase58(QtumTestNetParams.get(), unspentOutputs.get(0).getAddress());
        } catch (AddressFormatException e) {
            throw new InvalidAddressException("Invalid address", e);
        }
    }

    private static BigDecimal calculateGasFee(int gasLimit, int gasPrice) {
        return (BigDecimal.valueOf(gasLimit)).multiply(BigDecimal.valueOf(gasPrice)).divide(ONE_QTUM, MathContext.DECIMAL128);
    }

}
