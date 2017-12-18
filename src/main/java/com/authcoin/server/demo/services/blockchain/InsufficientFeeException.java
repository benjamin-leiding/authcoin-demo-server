package com.authcoin.server.demo.services.blockchain;

public class InsufficientFeeException extends RuntimeException {

    public InsufficientFeeException(String message) {
        super(message);
    }
}
