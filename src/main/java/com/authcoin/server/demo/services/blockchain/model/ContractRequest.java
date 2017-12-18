package com.authcoin.server.demo.services.blockchain.model;

public class ContractRequest {
    private String[] hashes;

    public ContractRequest(String[] hashes) {
        this.hashes = hashes;
    }

    public String[] getHashes() {
        return hashes;
    }
}
