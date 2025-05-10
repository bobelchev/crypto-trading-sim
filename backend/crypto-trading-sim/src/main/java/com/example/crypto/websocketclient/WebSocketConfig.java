package com.example.crypto.websocketclient;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebSocketConfig {
    @Bean
    public KrakenWebClient krakenWebClient() throws URISyntaxException {
        return new KrakenWebClient(new URI("wss://ws.kraken.com/v2"));
    }
}
