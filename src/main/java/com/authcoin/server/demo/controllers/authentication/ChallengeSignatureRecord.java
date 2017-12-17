package com.authcoin.server.demo.controllers.authentication;

public class ChallengeSignatureRecord {
    private byte[] challengeId;

    private byte[] vaeId;

    private Integer lifespan;

    private boolean revoked;

    private boolean successful;

    private long timestamp;

    private ChallengeSignatureRecord() {
    }

    public ChallengeSignatureRecord(byte[] challengeId, byte[] vaeId, Integer lifespan, boolean revoked, boolean successful, long timestamp) {
        this.challengeId = challengeId;
        this.vaeId = vaeId;
        this.lifespan = lifespan;
        this.revoked = revoked;
        this.successful = successful;
        this.timestamp = timestamp;
    }

    public byte[] getChallengeId() {
        return challengeId;
    }

    public byte[] getVaeId() {
        return vaeId;
    }

    public Integer getLifespan() {
        return lifespan;
    }

    public boolean isRevoked() {
        return revoked;
    }

    public boolean isSuccessful() {
        return successful;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
