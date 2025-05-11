package com.example.crypto.websocket.client;

import java.net.URI;
import java.net.URISyntaxException;

import com.example.crypto.websocket.server.FrontendWebSocketHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebSocketConfig {
    private final FrontendWebSocketHandler frontendHandler;

    public WebSocketConfig(FrontendWebSocketHandler frontendHandler) {
        this.frontendHandler = frontendHandler;
    }
    @Bean
    public KrakenWebClient krakenWebClient() throws URISyntaxException {
        return new KrakenWebClient(new URI("wss://ws.kraken.com/v2"),frontendHandler);
    }
}
