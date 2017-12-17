package com.authcoin.server.demo.registration;

import java.util.UUID;

public interface RegistrationService {

    void start(UUID id, RegistrationStatus registrationStatus);

    RegistrationStatus get(UUID id);
}
