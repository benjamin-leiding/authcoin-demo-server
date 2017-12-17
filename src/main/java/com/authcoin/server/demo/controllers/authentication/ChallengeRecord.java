package com.authcoin.server.demo.controllers.authentication;

import java.util.Arrays;

public class ChallengeRecord {
    private byte[] id;
    private byte[] vaeId;
    private long timestamp;
    private String type;
    private byte[] challenge;
    private byte[] verifier;
    private byte[] target;

    private ChallengeRecord() {
    }

    public ChallengeRecord(byte[] id, byte[] vaeId, long timestamp, String type, byte[] challenge, byte[] verifier, byte[] target) {
        this.id = id;
        this.vaeId = vaeId;
        this.timestamp = timestamp;
        this.type = type;
        this.challenge = challenge;
        this.verifier = verifier;
        this.target = target;
    }

    public byte[] getId() {
        return id;
    }

    public byte[] getVaeId() {
        return vaeId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getType() {
        return type;
    }

    public byte[] getChallenge() {
        return challenge;
    }

    public byte[] getVerifier() {
        return verifier;
    }

    public byte[] getTarget() {
        return target;
    }

    @Override
    public String toString() {
        return "ChallengeRecord{" +
                "vaeId=" + Arrays.toString(vaeId) +
                ", timestamp=" + timestamp +
                ", type='" + type + '\'' +
                ", challenge=" + Arrays.toString(challenge) +
                ", verifier=" + Arrays.toString(verifier) +
                ", target=" + Arrays.toString(target) +
                '}';
    }
}
