package com.authcoin.server.demo.services.blockchain.contract;

import java.math.BigDecimal;

class AuthcoinContractParams {

    static final String AUTHCOIN_CONTRACT_ADDRESS = "eb95c662869311bde0cc6cff0a178ea99f7eff22";

    static final int FUNCTION_GAS_LIMIT = 3000000;
    static final int GAS_LIMIT = 25000;
    static final int GAS_PRICE = 40;
    static final BigDecimal FEE_PER_KB = BigDecimal.valueOf(0.004);
    static final BigDecimal FEE = BigDecimal.valueOf(1.2);
}
