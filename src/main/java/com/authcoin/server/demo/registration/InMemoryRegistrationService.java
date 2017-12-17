package com.authcoin.server.demo.registration;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Keeps track of registrations.
 */
@Service
public class InMemoryRegistrationService implements RegistrationService {

    private Map<UUID, RegistrationStatus> states = new HashMap<>();

    @Override
    public void start(UUID id, RegistrationStatus registrationStatus) {
        states.put(id, registrationStatus);
    }

    @Override
    public RegistrationStatus get(UUID id) {
        return states.get(id);
    }

}
