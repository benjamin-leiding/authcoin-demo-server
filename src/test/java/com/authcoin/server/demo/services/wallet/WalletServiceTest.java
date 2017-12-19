package com.authcoin.server.demo.services.wallet;

import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.params.QtumTestNetParams;
import org.junit.Assert;
import org.junit.Test;


public class WalletServiceTest {

    @Test
    public void testCreateWallet() throws Exception {
        DeterministicKey key = new WalletService().getReceiveKey();

        System.out.println(key.toAddress(QtumTestNetParams.get()));
        Assert.assertNotNull(key);
    }
}
