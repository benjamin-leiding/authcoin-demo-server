package com.authcoin.server.demo.controllers;

import com.authcoin.server.demo.services.session.AuthenticationSession;
import com.authcoin.server.demo.services.session.SessionService;
import org.spongycastle.util.encoders.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class ProdectedController {

    private final SessionService sessionService;

    @Autowired
    public ProdectedController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/secret")
    public SecretContent secret(@RequestHeader("Authorization") String token) {
        // TODO do not do this in production
        if (token == null) {
            throw new NotAllowedException();
        }
        String[] splits = token.split(" ");
        if (splits.length != 2) {
            throw new NotAllowedException();
        }
        AuthenticationSession sess;
        try {
            sess = sessionService.get(UUID.fromString(splits[0]));
        } catch (Exception e) {
            throw new NotAllowedException();
        }
        if (Base64.toBase64String(sess.getToken()).equals(splits[1])) {
            return new SecretContent("You are authenticated!");
        }
        throw new NotAllowedException();
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public class NotAllowedException extends RuntimeException {
    }

    public class SecretContent {

        private String content;

        public SecretContent(String content) {
            this.content = content;
        }

        public String getContent() {
            return content;
        }
    }
}
