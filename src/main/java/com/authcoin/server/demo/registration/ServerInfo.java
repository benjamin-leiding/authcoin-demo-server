package com.authcoin.server.demo.registration;

import java.util.UUID;

public class ServerInfo {

    private final String serverEir;
    private final int nonce;
    private final UUID id;

    public ServerInfo(UUID id, String serverEir, int nonce) {
        this.id = id;
        this.serverEir = serverEir;
        this.nonce = nonce;
    }

    public UUID getId() {
        return id;
    }

    public String getServerEir() {
        return serverEir;
    }

    public int getNonce() {
        return nonce;
    }
}
