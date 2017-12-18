package com.authcoin.server.demo.controllers.info;

import com.authcoin.server.demo.services.KeyUtil;
import com.authcoin.server.demo.services.blockchain.EntityIdentityRecord;
import com.authcoin.server.demo.services.blockchain.contract.AuthcoinContractService;
import com.authcoin.server.demo.services.wallet.WalletService;
import org.bitcoinj.params.QtumTestNetParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongycastle.util.encoders.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.PublicKey;

@RestController
public class InfoController {

    private static final Logger logger = LoggerFactory.getLogger(InfoController.class);
    private final String authcoinAddress;
    private final AuthcoinContractService authcoinContractService;

    private WalletService walletService;

    @Autowired
    public InfoController(
            WalletService walletService,
            AuthcoinContractService authcoinContractService,
            @Value("${authcoin.blockchain.address}") String authcoinAddress
    ) {
        this.walletService = walletService;
        this.authcoinAddress = authcoinAddress;
        this.authcoinContractService = authcoinContractService;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/info")
    public InfoResponse info() {
        logger.info("Info request ...");
        PublicKey pubkey = KeyUtil.loadKeyPair().getPublic();
        EntityIdentityRecord eir = authcoinContractService.getEir();
        return new InfoResponse(
                authcoinAddress,
                walletService.getReceiveKey().toAddress(QtumTestNetParams.get()).toBase58(),
                eir.getUnspentOutputs(),
                Hex.toHexString(pubkey.getEncoded()),
                eir.getEirId(),
                eir.getAddress().toString()
        );
    }

}
