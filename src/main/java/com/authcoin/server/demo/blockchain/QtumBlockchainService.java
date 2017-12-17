package com.authcoin.server.demo.blockchain;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class QtumBlockchainService implements BlockchainService {

    @Value("${authcoin.server.eir.id}")
    private String serverEirId;

    public String getServerEirId() {
        return serverEirId;
    }
}
