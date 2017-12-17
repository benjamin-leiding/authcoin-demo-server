package com.authcoin.server.demo.registration;

import com.authcoin.server.demo.blockchain.BlockchainService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * User registration flow:
 * s 1. Client calls (HTTP GET) register endpoint. Server sends back an EIR ID, , nonce (public key + signature currently not implemented)
 * 2. Client receives response. Checks signature. Checks EIR. Shows info in Authcoin app => User select challenge.
 * 3. Client creates a challenge ((+nonce+1)+signature) and sends it to the server.
 * s 4. Server verifies challenge. Generates it's own. and sends it to the client.
 * 5. client verifies server challenge. shows this to end-user and generates response. finally sends it to the server
 * s 6. server verifies the response. and sends back it's own response.
 * 7. client verifies the response. and signs it. sends result to the server.
 * s 8. server verifies the response and send its own SR to client
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
public class RegisterController {

    private static final Logger logger = LoggerFactory.getLogger(RegisterController.class);
    private final RegistrationService registrationService;

    private BlockchainService blockchainService;

    @Autowired
    public RegisterController(BlockchainService blockchainService, RegistrationService registrationService) {
        this.blockchainService = blockchainService;
        this.registrationService = registrationService;
        logger.info("Server identity is {}", blockchainService.getServerEirId());
    }

    @RequestMapping(method = RequestMethod.GET, path = "/registration")
    public ServerInfo serverInfo() {
        logger.info("Registration started ...");
        ServerInfo serverInfo = new ServerInfo(UUID.randomUUID(), blockchainService.getServerEirId(), 1);
        registrationService.start(serverInfo.getId(), new RegistrationStatus(serverInfo.getId()));
        return serverInfo;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/registration/{registrationId}/challenge")
    public ChallengeRecord challengeRecord(@PathVariable("registrationId") UUID registrationId) {
        logger.info("got challenge from client with registration id {}", registrationId);

        return null;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/registration/{registrationId}/response")
    public ChallengeResponseRecord responseRecord(@PathVariable("registrationId") UUID registrationId) {
        logger.info("got challenge response from client with registration id {}", registrationId);

        return null;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/registration/{registrationId}/signature")
    public ChallengeSignatureRecord signatureRecord(@PathVariable("registrationId") UUID registrationId) {
        logger.info("got challenge signature from client with registration id {}", registrationId);

        return null;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/registration/{registrationId}/finalize/{txId}")
    public ChallengeSignatureRecord finalize(@PathVariable("registrationId") UUID registrationId, @PathVariable("txId") String txId) {
        logger.info("finalizing registration with id {}. tx id is {}", registrationId, txId);

        return null;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/registration/{registrationId}/status")
    public ChallengeSignatureRecord status(@PathVariable("registrationId") UUID registrationId) {
        logger.info("status request for registration with id {}", registrationId);

        // this checks transaction.

        return null;
    }

}
