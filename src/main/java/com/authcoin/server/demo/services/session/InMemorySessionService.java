package com.authcoin.server.demo.services.session;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Keeps track of registrations.
 */
@Service
public class InMemorySessionService implements SessionService {

    private Map<UUID, AuthenticationSession> states = new HashMap<>();

    @Override
    public void start(UUID id, AuthenticationSession authenticationSession) {
        states.put(id, authenticationSession);
    }

    @Override
    public AuthenticationSession get(UUID id) {
        return states.get(id);
    }

}
