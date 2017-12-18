package com.authcoin.server.demo.services.wallet;


import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.wallet.UnreadableWalletException;
import org.bitcoinj.wallet.Wallet;
import org.springframework.stereotype.Service;

@Service
public class WalletService {

    private final DeterministicKey receiveKey; // isn't unique
    private final Wallet wallet;

    public WalletService() {
        try {
            this.wallet = Wallet.loadFromFileStream(Thread.currentThread().getContextClassLoader().getResourceAsStream("mywallet"));
            this.receiveKey = wallet.freshReceiveKey();
        } catch (UnreadableWalletException e) {
            throw new IllegalStateException("Unable to load wallet", e);
        }
    }

    public DeterministicKey getReceiveKey() {
        return receiveKey;
    }

}
