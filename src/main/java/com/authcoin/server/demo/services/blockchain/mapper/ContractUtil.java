package com.authcoin.server.demo.services.blockchain.mapper;

import org.web3j.abi.datatypes.generated.Bytes32;

public final class ContractUtil {

    private ContractUtil() {
    }

    public static Bytes32 stringToBytes32(String string) {
        return bytesToBytes32(string.getBytes());
    }

    public static Bytes32 bytesToBytes32(byte[] byteValue) {
        byte[] byteValueLen32 = new byte[32];
        System.arraycopy(byteValue, 0, byteValueLen32, 0, byteValue.length);
        return new Bytes32(byteValueLen32);
    }

}
