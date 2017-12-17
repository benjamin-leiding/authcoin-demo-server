package com.authcoin.server.demo.controllers.authentication;

import java.util.UUID;

public class ServerInfo {

    private final String serverEir;
    private final int nonce;
    private final UUID id;
    private final String appName;

    public ServerInfo(UUID id, String serverEir, int nonce, String appName) {
        this.id = id;
        this.serverEir = serverEir;
        this.nonce = nonce;
        this.appName = appName;
    }

    public UUID getId() {
        return id;
    }

    public String getServerEir() {
        return serverEir;
    }

    public String getAppName() {
        return appName;
    }

    public int getNonce() {
        return nonce;
    }

}
