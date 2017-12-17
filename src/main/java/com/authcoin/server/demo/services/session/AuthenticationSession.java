package com.authcoin.server.demo.services.session;


import com.authcoin.server.demo.controllers.authentication.ChallengeRecord;
import com.authcoin.server.demo.controllers.authentication.ChallengeResponseRecord;
import com.authcoin.server.demo.controllers.authentication.ChallengeSignatureRecord;

import java.util.UUID;

public class AuthenticationSession {

    private UUID id;
    private ChallengeRecord sentChallengeRecord;
    private ChallengeResponseRecord sentResponseRecord;
    private ChallengeSignatureRecord sentSignatureRecord;

    private ChallengeRecord receivedChallengeRecord;
    private ChallengeResponseRecord receivedResponseRecord;
    private ChallengeSignatureRecord receivedSignatureRecord;

    private String txId;

    public AuthenticationSession(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public ChallengeRecord getSentChallengeRecord() {
        return sentChallengeRecord;
    }

    public void setSentChallengeRecord(ChallengeRecord sentChallengeRecord) {
        this.sentChallengeRecord = sentChallengeRecord;
    }

    public ChallengeResponseRecord getSentResponseRecord() {
        return sentResponseRecord;
    }

    public void setSentResponseRecord(ChallengeResponseRecord sentResponseRecord) {
        this.sentResponseRecord = sentResponseRecord;
    }

    public ChallengeSignatureRecord getSentSignatureRecord() {
        return sentSignatureRecord;
    }

    public void setSentSignatureRecord(ChallengeSignatureRecord sentSignatureRecord) {
        this.sentSignatureRecord = sentSignatureRecord;
    }

    public String getTxId() {
        return txId;
    }

    public void setTxId(String txId) {
        this.txId = txId;
    }

    public StatusEnum getStatus() {
        if (sentSignatureRecord != null) {
            return StatusEnum.CHALLENGE_SIGNATURES_EXCHANGED;
        }
        if (sentResponseRecord != null) {
            return StatusEnum.CHALLENGE_RESPONSES_EXCHANGED;
        }
        if (sentChallengeRecord != null) {
            return StatusEnum.CHALLENGES_EXCHANGED;
        }
        return StatusEnum.REGISTRATION_STARTED;
    }

    public ChallengeRecord getReceivedChallengeRecord() {
        return receivedChallengeRecord;
    }

    public void setReceivedChallengeRecord(ChallengeRecord receivedChallengeRecord) {
        this.receivedChallengeRecord = receivedChallengeRecord;
    }

    public ChallengeResponseRecord getReceivedResponseRecord() {
        return receivedResponseRecord;
    }

    public void setReceivedResponseRecord(ChallengeResponseRecord receivedResponseRecord) {
        this.receivedResponseRecord = receivedResponseRecord;
    }

    public ChallengeSignatureRecord getReceivedSignatureRecord() {
        return receivedSignatureRecord;
    }

    public void setReceivedSignatureRecord(ChallengeSignatureRecord receivedSignatureRecord) {
        this.receivedSignatureRecord = receivedSignatureRecord;
    }
}
