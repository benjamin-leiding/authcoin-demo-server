package com.authcoin.server.demo.controllers.info;

public class InfoResponse {

    private String authcoinAddress;
    private String serverWalletAddress;
    private String serverUnspentOutputs;
    private String publicKey;
    private String serverEirId;
    private String serverEirAddress;

    public InfoResponse(String authcoinAddress, String serverWalletAddress, String serverUnspentOutputs, String publicKey, String serverEirId, String serverEirAddress) {
        this.authcoinAddress = authcoinAddress;
        this.serverWalletAddress = serverWalletAddress;
        this.serverUnspentOutputs = serverUnspentOutputs;
        this.publicKey = publicKey;
        this.serverEirId = serverEirId;
        this.serverEirAddress = serverEirAddress;
    }

    public String getAuthcoinAddress() {
        return authcoinAddress;
    }

    public String getServerWalletAddress() {
        return serverWalletAddress;
    }

    public String getServerUnspentOutputs() {
        return serverUnspentOutputs;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public String getServerEirId() {
        return serverEirId;
    }

    public String getServerEirAddress() {
        return serverEirAddress;
    }
}
