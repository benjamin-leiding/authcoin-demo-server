package com.authcoin.server.demo.services.blockchain.model;

import com.authcoin.server.demo.services.blockchain.TransactionInfo;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

public class Vout extends TransactionInfo {

    @SerializedName("value")
    @Expose
    private BigDecimal value;
    @SerializedName("address")
    @Expose
    private String address;
    private boolean isOwnAddress = false;

    public boolean isOwnAddress() {
        return isOwnAddress;
    }

    public void setOwnAddress(boolean ownAddress) {
        isOwnAddress = ownAddress;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
