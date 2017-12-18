package com.authcoin.server.demo.services.blockchain.model;

import com.google.gson.annotations.Expose;

import java.util.List;

public class ContractResponse {
    @Expose
    private List<Item> items;

    public ContractResponse() {
    }

    public ContractResponse(List<Item> items) {
        this.items = items;
    }

    public List<Item> getItems() {
        return items;
    }

}
