package com.authcoin.server.demo.services.session;

import java.util.UUID;

public interface SessionService {

    void start(UUID id, AuthenticationSession authenticationSession);

    AuthenticationSession get(UUID id);
}
