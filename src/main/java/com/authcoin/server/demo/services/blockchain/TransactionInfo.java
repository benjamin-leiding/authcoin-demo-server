package com.authcoin.server.demo.services.blockchain;

import java.math.BigDecimal;

public abstract class TransactionInfo {
    public abstract String getAddress();

    public abstract BigDecimal getValue();

    public abstract boolean isOwnAddress();
}
