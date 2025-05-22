package com.example.kraken_ws_service.websocket;

import com.example.crypto.websocket.server.FrontendWebSocketHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;
import java.net.URISyntaxException;

@Configuration
public class WebSocketConfig {
    @Bean
    public KrakenWebClient krakenWebClient() throws URISyntaxException {
        return new KrakenWebClient(new URI("wss://ws.kraken.com/v2"));
    }
}
