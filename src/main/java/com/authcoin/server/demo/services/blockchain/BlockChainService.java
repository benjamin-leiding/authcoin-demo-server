package com.authcoin.server.demo.services.blockchain;

import com.authcoin.server.demo.services.blockchain.model.*;
import io.reactivex.Observable;
import org.springframework.stereotype.Service;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.List;

@Service
public class BlockChainService implements BlockChainApi {

    private BlockChainApi blockChainApi;

    public BlockChainService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://145.239.197.39:5931/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        blockChainApi = retrofit.create(BlockChainApi.class);
    }

    @Override
    public Observable<ContractResponse> callContract(String contractAddress, ContractRequest contractRequest) {
        return blockChainApi.callContract(contractAddress, contractRequest);
    }

    @Override
    public Observable<SendRawTransactionResponse> sendRawTransaction(SendRawTransactionRequest sendRawTransactionRequest) {
        return blockChainApi.sendRawTransaction(sendRawTransactionRequest);
    }

    @Override
    public Observable<List<UnspentOutput>> getUnspentOutput(List<String> addresses) {
        return blockChainApi.getUnspentOutput(addresses);
    }

    @Override
    public Observable<Transaction> getTransaction(String transaction) {
        return blockChainApi.getTransaction(transaction);
    }
}
