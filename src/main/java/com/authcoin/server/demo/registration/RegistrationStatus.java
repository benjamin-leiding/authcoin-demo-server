package com.authcoin.server.demo.registration;

import java.util.UUID;

public class RegistrationStatus {

    private UUID id;
    private ChallengeRecord challengeRecord;
    private ChallengeResponseRecord responseRecord;
    private ChallengeSignatureRecord signatureRecord;
    private String txId;

    public RegistrationStatus(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public ChallengeRecord getChallengeRecord() {
        return challengeRecord;
    }

    public void setChallengeRecord(ChallengeRecord challengeRecord) {
        this.challengeRecord = challengeRecord;
    }

    public ChallengeResponseRecord getResponseRecord() {
        return responseRecord;
    }

    public void setResponseRecord(ChallengeResponseRecord responseRecord) {
        this.responseRecord = responseRecord;
    }

    public ChallengeSignatureRecord getSignatureRecord() {
        return signatureRecord;
    }

    public void setSignatureRecord(ChallengeSignatureRecord signatureRecord) {
        this.signatureRecord = signatureRecord;
    }

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }
}
