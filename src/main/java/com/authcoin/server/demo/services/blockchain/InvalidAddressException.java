package com.authcoin.server.demo.services.blockchain;

public class InvalidAddressException extends RuntimeException {

    public InvalidAddressException(String message, Throwable cause) {
        super(message, cause);
    }
}
