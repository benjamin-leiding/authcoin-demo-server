package com.authcoin.server.demo.services.blockchain;

import org.web3j.abi.datatypes.Address;

import java.security.PublicKey;
import java.util.List;

public class EntityIdentityRecord {

    private boolean revoked;
    private String contentType;
    private byte[] hash;
    private byte[] signature;
    private PublicKey publicKey;
    private List<String> identifiers;
    private Address address;
    private String eirId;
    private String unspentOutputs;

    public EntityIdentityRecord(boolean revoked, byte[] hash, byte[] signature, PublicKey publicKey, List<String> identifiers) {
        this.revoked = revoked;
        this.contentType = "test";
        this.hash = hash;
        this.signature = signature;
        this.publicKey = publicKey;
        this.identifiers = identifiers;
    }


    public boolean isRevoked() {
        return revoked;
    }

    public String getContentType() {
        return contentType;
    }

    public byte[] getHash() {
        return hash;
    }

    public byte[] getSignature() {
        return signature;
    }

    public List<String> getIdentifiers() {
        return identifiers;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Address getAddress() {
        return address;
    }

    public void setEirId(String eirId) {
        this.eirId = eirId;
    }

    public String getEirId() {
        return eirId;
    }

    public String getUnspentOutputs() {
        return unspentOutputs;
    }

    public void setUnspentOutputs(String unspentOutputs) {
        this.unspentOutputs = unspentOutputs;
    }
}
