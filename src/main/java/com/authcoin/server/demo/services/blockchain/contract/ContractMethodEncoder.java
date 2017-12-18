package com.authcoin.server.demo.services.blockchain.contract;

import com.authcoin.server.demo.services.blockchain.TransactionUtil;
import com.authcoin.server.demo.services.blockchain.model.ContractRequest;
import com.authcoin.server.demo.services.blockchain.model.UnspentOutput;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.script.Script;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;

import java.util.List;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.web3j.abi.FunctionEncoder.encode;
import static org.web3j.utils.Numeric.cleanHexPrefix;

class ContractMethodEncoder {

    static Script resolveAuthCoinScript(String methodName, List<Type> methodParameters) {
        return resolveScript(methodName, methodParameters, AuthcoinContractParams.AUTHCOIN_CONTRACT_ADDRESS);
    }

    private static Script resolveScript(String methodName, List<Type> methodParameters, String contractAddress) {
        String encodedFunction = encodeFunction(new Function(methodName, methodParameters, emptyList()));
        return TransactionUtil.createScript(encodedFunction, AuthcoinContractParams.FUNCTION_GAS_LIMIT, AuthcoinContractParams.GAS_PRICE, contractAddress);
    }

    static String resolveTransaction(DeterministicKey key, Script script, List<UnspentOutput> unspentOutput) {
        return TransactionUtil.createTransaction(script, unspentOutput, singletonList(key), AuthcoinContractParams.GAS_LIMIT, AuthcoinContractParams.GAS_PRICE, AuthcoinContractParams.FEE_PER_KB, AuthcoinContractParams.FEE);
    }

    static ContractRequest resolveContractRequest(String methodName, List<Type> methodParameters) {
        return new ContractRequest(new String[]{encodeFunction(new Function(methodName, methodParameters, emptyList()))});
    }

    private static String encodeFunction(Function function) {
        return cleanHexPrefix(encode(function));
    }
}
