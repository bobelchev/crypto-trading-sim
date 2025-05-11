package com.example.crypto.websocket.client;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class WebSocketRunner implements ApplicationRunner {

    private final KrakenWebClient krakenWebClient;

    public WebSocketRunner(KrakenWebClient krakenWebClient) {
        this.krakenWebClient = krakenWebClient;
    }

    @Override
    public void run(ApplicationArguments args) {
        krakenWebClient.connect();
    }
}