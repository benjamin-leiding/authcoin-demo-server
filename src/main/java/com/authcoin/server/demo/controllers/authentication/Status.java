package com.authcoin.server.demo.controllers.authentication;

public class Status {

    private final byte[] token;
    private String status;

    public Status(String status, byte[] token) {
        this.status = status;
        this.token = token;
    }

    public String getStatus() {
        return status;
    }

    public byte[] getToken() {
        return token;
    }
}
