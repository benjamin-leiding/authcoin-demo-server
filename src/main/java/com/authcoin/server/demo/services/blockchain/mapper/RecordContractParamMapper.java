package com.authcoin.server.demo.services.blockchain.mapper;

import com.authcoin.server.demo.services.blockchain.EntityIdentityRecord;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint256;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.authcoin.server.demo.services.blockchain.mapper.ContractUtil.bytesToBytes32;
import static com.authcoin.server.demo.services.blockchain.mapper.ContractUtil.stringToBytes32;
import static org.web3j.abi.Utils.convert;

public class RecordContractParamMapper {

    public static List<Type> resolveEirContractParams(EntityIdentityRecord eir) {
        List<Type> params = new ArrayList<>();
        List<Bytes32> identifiers = new ArrayList<>();
        for (String s : eir.getIdentifiers()) {
            identifiers.add(stringToBytes32(s));
        }
        params.add(new DynamicBytes(eir.getPublicKey().getEncoded()));
        params.add(stringToBytes32(eir.getContentType()));
        params.add(new DynamicArray<>(identifiers));
        params.add(bytesToBytes32(eir.getHash()));
        params.add(new DynamicBytes(eir.getSignature()));
        return params;
    }


    public static EntityIdentityRecord resolveEirFromAbiReturn(String abiReturn) {
        // let god have mercy on my soul
        List<TypeReference<?>> outputParameters = Arrays.asList(
                new TypeReference<Bytes32>() { // id
                }, new TypeReference<Uint256>() { // blockNumber
                }, new TypeReference<DynamicBytes>() { // content
                }, new TypeReference<Bytes32>() { // contentType
                }, new TypeReference<Bool>() { // revoked
                }, new TypeReference<DynamicArray<Bytes32>>() { // identifiers
                }, new TypeReference<Bytes32>() { // hash
                }, new TypeReference<DynamicBytes>() { // signature
                });
        List<Type> output = FunctionReturnDecoder.decode(abiReturn, convert(outputParameters));
        // TODO check if blockchain EIR and EIR in db match
        byte[] content = removeNullBytes((byte[]) output.get(2).getValue());
        EntityIdentityRecord eir = new EntityIdentityRecord(
                ((Bool) output.get(4)).getValue(),
                removeNullBytes((byte[]) output.get(6).getValue()),
                removeNullBytes((byte[]) output.get(7).getValue()),
                toPublicKey(content),
                getIdentifiers((List<Bytes32>) output.get(5).getValue())
        );
        return eir;
    }

    public static PublicKey toPublicKey(byte[] content) {
        try {
            return KeyFactory.getInstance("EC").generatePublic(new X509EncodedKeySpec(content));
        } catch (InvalidKeySpecException e) {
            throw new IllegalStateException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }


    public static List<Address> resolveAddressesFromAbiReturn(String abiReturn) {
        List<TypeReference<?>> outputParameters = Collections.singletonList(
                new TypeReference<DynamicArray<Address>>() {
                });
        List<Type> output = FunctionReturnDecoder.decode(abiReturn, convert(outputParameters));
        return (List<Address>) output.get(0).getValue();
    }

    public static Address resolveAddressFromAbiReturn(String abiReturn) {
        List<TypeReference<?>> outputParameters = Collections.singletonList(
                new TypeReference<Address>() {
                });
        List<Type> output = FunctionReturnDecoder.decode(abiReturn, convert(outputParameters));
        return (Address) output.get(0);
    }

    public static List<Bytes32> resolveBytes32FromAbiReturn(String abiReturn) {
        List<TypeReference<?>> outputParameters = Collections.singletonList(
                new TypeReference<DynamicArray<Bytes32>>() {
                });
        List<Type> output = FunctionReturnDecoder.decode(abiReturn, convert(outputParameters));
        return (List<Bytes32>) output.get(0).getValue();
    }

    private static List<String> getIdentifiers(List<Bytes32> ids) {
        List<String> identifiers = new ArrayList<>();
        for (Bytes32 identifier : ids) {
            identifiers.add(new String(removeNullBytes(identifier.getValue()), StandardCharsets.UTF_8));
        }
        return identifiers;
    }

    private static byte[] removeNullBytes(byte[] bytes) {
        int i = bytes.length - 1;
        while (i >= 0 && bytes[i] == 0) {
            --i;
        }
        return Arrays.copyOf(bytes, i + 1);
    }
}
