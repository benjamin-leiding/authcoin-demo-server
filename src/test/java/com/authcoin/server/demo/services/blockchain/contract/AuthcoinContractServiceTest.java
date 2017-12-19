package com.authcoin.server.demo.services.blockchain.contract;

import com.authcoin.server.demo.services.KeyUtil;
import com.authcoin.server.demo.services.blockchain.EntityIdentityRecord;
import com.authcoin.server.demo.services.blockchain.BlockChainService;
import com.authcoin.server.demo.services.blockchain.mapper.RecordContractParamMapper;
import com.authcoin.server.demo.services.blockchain.model.ContractResponse;
import com.authcoin.server.demo.services.blockchain.model.SendRawTransactionResponse;
import com.authcoin.server.demo.services.wallet.WalletService;
import io.reactivex.Observable;
import org.bitcoinj.params.QtumTestNetParams;
import org.junit.Test;
import org.spongycastle.util.encoders.Hex;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.crypto.Hash;
import org.web3j.utils.Numeric;

import java.security.KeyPair;
import java.security.PublicKey;
import java.util.Arrays;

public class AuthcoinContractServiceTest {

    @Test
    public void registerEir() throws Exception {
        WalletService w = new WalletService();
        System.out.println("Address:"+ w.getReceiveKey().toAddress(QtumTestNetParams.get()));
        KeyPair keyPair = KeyUtil.create();
        System.out.println("Key: " + getEirIdAsString(keyPair.getPublic()));
        AuthcoinContractService acs =
                new AuthcoinContractService(new BlockChainService(), new WalletService());
        EntityIdentityRecord eir =
                new EntityIdentityRecord(
                        false,
                        new byte[32],
                        new byte[128],
                        keyPair.getPublic(),
                        Arrays.asList("John Doe")
                );
        Observable<SendRawTransactionResponse> o = acs.registerEir(w.getReceiveKey(), RecordContractParamMapper.resolveEirContractParams(eir));
        SendRawTransactionResponse result = o.blockingFirst();
        System.out.println("TxId: " + result.getTxid());
    }

    public static String getEirIdAsString(PublicKey key) {
        return Numeric.cleanHexPrefix(Hash.sha3(Hex.toHexString(key.getEncoded())));
    }

    @Test
    public void testGetEir() throws Exception {
        WalletService w = new WalletService();
        System.out.println("Address:"+ w.getReceiveKey().toAddress(QtumTestNetParams.get()));
        KeyPair keyPair = KeyUtil.create();
        System.out.println("Key: " + getEirIdAsString(keyPair.getPublic()));
        AuthcoinContractService acs =
                new AuthcoinContractService(new BlockChainService(), new WalletService());

        Observable<ContractResponse> address = acs.getEirAddress(new Bytes32(Hex.decode("557ec9eee3749c400b3d4fa01791466c059ccfb60d71506a51407f50387266d5")));
        ContractResponse contractResponse = address.blockingFirst();
        Address eirAddress = RecordContractParamMapper.resolveAddressFromAbiReturn(contractResponse.getItems().get(0).getOutput());
        EntityIdentityRecord eir = RecordContractParamMapper.resolveEirFromAbiReturn(acs.getEirByAddress(eirAddress).blockingFirst().getItems().get(0).getOutput());
        
        System.out.println(eirAddress);
    }
}
