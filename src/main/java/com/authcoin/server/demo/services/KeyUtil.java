package com.authcoin.server.demo.services;

import org.spongycastle.jce.ECNamedCurveTable;
import org.spongycastle.jce.provider.BouncyCastleProvider;
import org.spongycastle.jce.spec.ECNamedCurveParameterSpec;

import java.io.IOException;
import java.security.*;

public final class KeyUtil {

    public static KeyPair create() {
        try {
            Security.addProvider(new BouncyCastleProvider());
            ECNamedCurveParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec("prime192v1");
            KeyPairGenerator g = KeyPairGenerator.getInstance("ECDSA", BouncyCastleProvider.PROVIDER_NAME);
            g.initialize(ecSpec);
            KeyPair keyPair = g.generateKeyPair();
            return keyPair;
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException(e);
        }
    }

    public static KeyPair loadKeyPair() {
        try {
            KeyStore ks = KeyStore.getInstance("JKS");
            ks.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("mykeystore.jks"), "changeit".toCharArray());
            Key privateKey = ks.getKey("demo", "demo".toCharArray());
            PublicKey pub = ks.getCertificate("demo").getPublicKey();
            return new KeyPair(pub, (PrivateKey) privateKey);
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException(e);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }


}
