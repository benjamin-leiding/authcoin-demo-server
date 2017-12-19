package com.authcoin.server.demo.services.blockchain.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SendRawTransactionResponse {

    @SerializedName("txid")
    public String txid;
    @SerializedName("result")
    @Expose
    private String result;

    public String getResult() {
        return result;
    }

    public String getTxid() {
        return txid;
    }
}
