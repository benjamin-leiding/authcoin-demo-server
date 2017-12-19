package com.authcoin.server.demo.services;

import org.junit.Test;

import java.security.KeyPair;

import static org.junit.Assert.assertNotNull;

public class KeyUtilTest {

    @Test
    public void testCreateKeyPair() throws Exception {
        KeyPair keyPair = KeyUtil.loadKeyPair();

        assertNotNull(keyPair);
        assertNotNull(keyPair.getPrivate());
        assertNotNull(keyPair.getPublic());
    }
}
