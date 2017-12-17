package com.authcoin.server.demo.controllers.authentication;

public class ChallengeResponseRecord {

    private byte[] challengeId;

    private byte[] vaeId;

    private long timestamp;

    private byte[] response;

    private ChallengeResponseRecord() {
    }

    public ChallengeResponseRecord(byte[] challengeId, byte[] vaeId, long timestamp, byte[] response) {
        this.challengeId = challengeId;
        this.vaeId = vaeId;
        this.timestamp = timestamp;
        this.response = response;
    }

    public byte[] getChallengeId() {
        return challengeId;
    }

    public byte[] getVaeId() {
        return vaeId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public byte[] getResponse() {
        return response;
    }
}
