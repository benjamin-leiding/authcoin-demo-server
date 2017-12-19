package com.authcoin.server.demo.services.blockchain.contract;

import com.authcoin.server.demo.services.KeyUtil;
import com.authcoin.server.demo.services.blockchain.BlockChainService;
import com.authcoin.server.demo.services.blockchain.EntityIdentityRecord;
import com.authcoin.server.demo.services.blockchain.mapper.ContractUtil;
import com.authcoin.server.demo.services.blockchain.mapper.RecordContractParamMapper;
import com.authcoin.server.demo.services.blockchain.model.*;
import com.authcoin.server.demo.services.wallet.WalletService;
import io.reactivex.Observable;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.params.QtumTestNetParams;
import org.bitcoinj.script.Script;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongycastle.util.encoders.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.crypto.Hash;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.List;

import static com.authcoin.server.demo.services.blockchain.contract.ContractMethodEncoder.*;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.web3j.utils.Numeric.cleanHexPrefix;

@Service
public class AuthcoinContractService {

    private static final Logger logger = LoggerFactory.getLogger(AuthcoinContractService.class);

    private static final String REGISTER_EIR = "registerEir";
    private static final String GET_EIR = "getEir";
    private static final String GET_EIR_DATA = "getData";
    private final BlockChainService blockChainService;
    private final WalletService walletService;
    private EntityIdentityRecord eir;

    @Autowired
    public AuthcoinContractService(BlockChainService blockChainService, WalletService walletService) {
        this.blockChainService = blockChainService;
        this.walletService = walletService;
    }

    public EntityIdentityRecord getEir() {
        return eir;
    }

    @PostConstruct
    public void postConstruct() throws Exception {
        KeyPair keyPair = KeyUtil.loadKeyPair();
        Bytes32 eirIdAsBytes32 = getEirIdAsBytes32(
                getEirIdAsString(keyPair.getPublic())
        );
        Observable<ContractResponse> address = this.getEirAddress(
                eirIdAsBytes32
        );
        ContractResponse contractResponse = address.blockingFirst();
        Address eirAddress = RecordContractParamMapper.resolveAddressFromAbiReturn(contractResponse.getItems().get(0).getOutput());
        if (BigInteger.ZERO.equals(eirAddress.getValue())) {
            logger.info("Creating a new EIR");
            EntityIdentityRecord eir = new EntityIdentityRecord(
                    false,
                    new byte[32],
                    new byte[56],
                    keyPair.getPublic(),
                    Arrays.asList("Authcoin Demo Server")
            );
            Observable<SendRawTransactionResponse> o = this.registerEir(walletService.getReceiveKey(), RecordContractParamMapper.resolveEirContractParams(eir));
            SendRawTransactionResponse result = o.blockingFirst();
            logger.info("EIR tx id is {}", result.txid);
            logger.warn("EIR isn't registered.");
            this.eir = eir;
        } else {
            logger.info("EIR is registered. Address is {} ", eirAddress.toString());
            Observable<List<UnspentOutput>> unspentOutputs = this.getUnspentOutputs(walletService.getReceiveKey());
            List<UnspentOutput> oos = unspentOutputs.blockingFirst();
            BigDecimal amount = BigDecimal.ZERO;
            for (UnspentOutput oo : oos) {
                amount = amount.add(oo.getAmount());
            }
            this.eir = RecordContractParamMapper.resolveEirFromAbiReturn(this.getEirByAddress(eirAddress).blockingFirst().getItems().get(0).getOutput());
            eir.setUnspentOutputs(amount.toPlainString());
        }
        eir.setEirId(Hex.toHexString(eirIdAsBytes32.getValue()));
        eir.setAddress(eirAddress);
    }

    public Observable<SendRawTransactionResponse> registerEir(final DeterministicKey key, List<Type> methodParameters) {
        return sendRawTransaction(key, resolveAuthCoinScript(REGISTER_EIR, methodParameters));
    }

    public Observable<ContractResponse> getEirAddress(Bytes32 eirId) {
        return callAuthCoinContract(resolveContractRequest(GET_EIR, singletonList(eirId)));
    }

    public Observable<ContractResponse> getEirByAddress(Address address) {
        String eirAddress = cleanHexPrefix(address.toString());
        if (eirAddress == null || eirAddress.length() == 0) {
            return Observable.error(new IllegalStateException("No such EIR with provided eirId"));
        } else {
            return callContract(eirAddress, resolveContractRequest(GET_EIR_DATA, emptyList()));
        }
    }

    public Observable<List<UnspentOutput>> getUnspentOutputs(DeterministicKey key) {
        return blockChainService.getUnspentOutput(singletonList(key.toAddress(QtumTestNetParams.get()).toBase58()));
    }

    public Observable<Transaction> getTransaction(String transaction) {
        return blockChainService.getTransaction(transaction);
    }

    private Observable<SendRawTransactionResponse> sendRawTransaction(DeterministicKey key, Script script) {
        return getUnspentOutputs(key).switchMap(
                unspentOutput -> blockChainService.sendRawTransaction(
                        new SendRawTransactionRequest(resolveTransaction(key, script, unspentOutput), 1)
                ));
    }

    private Observable<ContractResponse> callAuthCoinContract(ContractRequest contractRequest) {
        return callContract(AuthcoinContractParams.AUTHCOIN_CONTRACT_ADDRESS, contractRequest);
    }

    private Observable<ContractResponse> callContract(String contractAddress, ContractRequest contractRequest) {
        return blockChainService.callContract(contractAddress, contractRequest);
    }

    private Bytes32 getEirIdAsBytes32(String eirId) {
        return ContractUtil.bytesToBytes32(Hex.decode(eirId));
    }

    private String getEirIdAsString(PublicKey key) {
        return cleanHexPrefix(Hash.sha3(Hex.toHexString(key.getEncoded())));
    }
}
