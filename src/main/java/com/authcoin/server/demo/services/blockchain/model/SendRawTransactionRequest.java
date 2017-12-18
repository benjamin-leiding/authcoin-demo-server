package com.authcoin.server.demo.services.blockchain.model;

public class SendRawTransactionRequest {

    private String data;
    private Integer allowHighFee;

    public SendRawTransactionRequest(String data, Integer allowHighFee) {
        this.data = data;
        this.allowHighFee = allowHighFee;
    }

    public String getData() {
        return data;
    }

    public Integer getAllowHighFee() {
        return allowHighFee;
    }
}
