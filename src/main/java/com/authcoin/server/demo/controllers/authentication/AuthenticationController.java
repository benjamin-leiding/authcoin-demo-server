package com.authcoin.server.demo.controllers.authentication;

import com.authcoin.server.demo.exceptions.AuthenticationNotStartedException;
import com.authcoin.server.demo.exceptions.ChallengeNotFoundException;
import com.authcoin.server.demo.exceptions.InvalidInputException;
import com.authcoin.server.demo.services.KeyUtil;
import com.authcoin.server.demo.services.blockchain.contract.AuthcoinContractService;
import com.authcoin.server.demo.services.session.AuthenticationSession;
import com.authcoin.server.demo.services.session.SessionService;
import com.authcoin.server.demo.services.session.StatusEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.nio.ByteBuffer;
import java.security.*;
import java.util.UUID;

/**
 * User registration flow:
 * 1. Client calls (HTTP GET) register endpoint. Server sends back an EIR ID, , nonce (public key + signature currently not implemented)
 * 2. Client receives response. Checks signature. Checks EIR. Shows info in Authcoin app => User select challenge.
 * 3. Client creates a challenge ((+nonce+1)+signature) and sends it to the server.
 * 4. Server verifies challenge. Generates it's own. and sends it to the client.
 * 5. client verifies server challenge. shows this to end-user and generates response. finally sends it to the server
 * 6. server verifies the response. and sends back it's own response.
 * 7. client verifies the response. and signs it. sends result to the server.
 * 8. server verifies the response and send its own SR to client
 * 9. client or server saves all the stuff to the blockchain.
 * 10. client sends txId to server. registration complete.
 * <p>
 * Notes about the implementation:
 * 1. It is as unsecure as it can be because
 * * signatures aren't added to request;
 * * signatures aren't verified
 * * HTTPS isn't used by default;
 * * it is a demo :)
 * 2. IT IS A DEMO!!
 */
@RestController
public class AuthenticationController {

    public static final String CHALLNEGE_TYPE_SIGN_CONTENT = "Sign Content";
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();
    private static final String APP_NAME = "Authcoin Demo Bank";
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationController.class);
    private final SessionService sessionService;

    private final AuthcoinContractService authcoinContractService;

    @Autowired
    public AuthenticationController(AuthcoinContractService authcoinContractService, SessionService sessionService) {
        this.sessionService = sessionService;
        this.authcoinContractService = authcoinContractService;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/registration")
    public ServerInfo serverInfo() {
        logger.info("Registration started ...");
        ServerInfo serverInfo = new ServerInfo(UUID.randomUUID(), authcoinContractService.getEir().getEirId(), 1, APP_NAME);
        sessionService.start(serverInfo.getId(), new AuthenticationSession(serverInfo.getId()));
        return serverInfo;
    }

    @RequestMapping(method = RequestMethod.POST, path = "/registration/{registrationId}/challenge")
    public ChallengeRecord challengeRecord(
            @PathVariable("registrationId") UUID registrationId,
            @RequestBody ChallengeRecord input) {
        logger.info("got challenge from client with registration id {}", registrationId);
        AuthenticationSession state = validateInput(registrationId, input);

        state.setReceivedChallengeRecord(input);

        ChallengeRecord challengeForVerifier = new ChallengeRecord(
                generateId(),
                input.getVaeId(),
                System.currentTimeMillis(),
                CHALLNEGE_TYPE_SIGN_CONTENT,
                generateChallenge(),
                input.getTarget(),
                input.getVerifier()
        );
        state.setSentChallengeRecord(challengeForVerifier);
        return challengeForVerifier;
    }

    @RequestMapping(method = RequestMethod.POST, path = "/registration/{registrationId}/response")
    public ChallengeResponseRecord responseRecord(
            @PathVariable("registrationId") UUID registrationId,
            @RequestBody ChallengeResponseRecord input) throws Exception {
        logger.info("got challenge response from client with registration id {}", registrationId);
        AuthenticationSession state = validateInput(registrationId, input);
        ChallengeRecord myChallengeRecord = state.getReceivedChallengeRecord();
        if (myChallengeRecord == null) {
            throw new ChallengeNotFoundException();
        }
        state.setReceivedResponseRecord(input);

        byte[] response = sign(myChallengeRecord.getChallenge());

        ChallengeResponseRecord serverResponse = new ChallengeResponseRecord(
                myChallengeRecord.getId(),
                myChallengeRecord.getVaeId(),
                System.currentTimeMillis(),
                response
        );
        state.setSentResponseRecord(serverResponse);
        return serverResponse;
    }

    @RequestMapping(method = RequestMethod.POST, path = "/registration/{registrationId}/signature")
    public ChallengeSignatureRecord signatureRecord(
            @PathVariable("registrationId") UUID registrationId,
            @RequestBody ChallengeSignatureRecord input) {
        logger.info("got challenge signature from client with registration id {}", registrationId);
        AuthenticationSession state = validateInput(registrationId, input);
        ChallengeResponseRecord receivedResponseRecord = state.getReceivedResponseRecord();
        if (receivedResponseRecord == null) {
            throw new ChallengeNotFoundException(); // TODO exception name
        }
        state.setReceivedSignatureRecord(input);

        // TODO verify response

        ChallengeSignatureRecord serverResponse = new ChallengeSignatureRecord(
                receivedResponseRecord.getChallengeId(),
                receivedResponseRecord.getVaeId(),
                365,
                false,
                true,
                System.currentTimeMillis()
        );

        state.setSentSignatureRecord(serverResponse);
        return serverResponse;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/registration/{registrationId}/finalize/{txId}")
    public void finalize(@PathVariable("registrationId") UUID registrationId, @PathVariable("txId") String txId) {
        logger.info("finalizing registration with id {}. tx id is {}", registrationId, txId);
        AuthenticationSession state = validateInput(registrationId, new Object()); // hack
        if (state.getReceivedSignatureRecord() == null) {
            throw new ChallengeNotFoundException(); // TODO exception name
        }
        state.setTxId(txId);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/registration/{registrationId}/status", produces = "application/json")
    public Status status(@PathVariable("registrationId") UUID registrationId) throws Exception {
        logger.info("status request for registration with id {}", registrationId);
        AuthenticationSession authenticationSession = sessionService.get(registrationId);
        if (authenticationSession == null) {
            return new Status("NOT_STARTED", null);
        }
        StatusEnum status = authenticationSession.getStatus();
        // TODO shouldn't be handled like this!!
        byte[] token = null;
        if (StatusEnum.CHALLENGE_RESPONSES_EXCHANGED.equals(status)) {
            byte[] challenge = generateChallenge();
            token = sign(challenge);
            authenticationSession.setToken(token);
        }
        return new Status(status.name(), token);
    }

    private byte[] generateId() {
        UUID uuid = UUID.randomUUID();
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return bb.array();
    }

    private byte[] generateChallenge() {
        byte[] bytes = new byte[32];
        SECURE_RANDOM.nextBytes(bytes);
        return bytes;
    }

    private AuthenticationSession validateInput(UUID registrationId, Object input) {
        if (input == null) {
            throw new InvalidInputException();
        }
        AuthenticationSession state = sessionService.get(registrationId);
        if (state == null) {
            throw new AuthenticationNotStartedException();
        }
        return state;
    }

    private byte[] sign(byte[] challenge) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature signature = Signature.getInstance("SHA256withECDSA");
        PrivateKey aPrivate = KeyUtil.loadKeyPair().getPrivate();
        signature.initSign(aPrivate);
        signature.update(challenge);
        return signature.sign();
    }

}
