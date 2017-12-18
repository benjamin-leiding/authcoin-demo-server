package com.authcoin.server.demo.services.blockchain.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SendRawTransactionResponse {

    @SerializedName("result")
    @Expose
    private String result;

    @SerializedName("txid")
    public String txid;

    public String getResult() {
        return result;
    }

    public String getTxid() {
        return txid;
    }
}
