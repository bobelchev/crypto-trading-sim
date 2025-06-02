package com.example.kraken_ws_service.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@Configuration
public class WebSocketConfig {
    @Bean
    public KrakenWebClient krakenWebClient(KafkaTemplate<String, Object> kafkaTemplate) throws URISyntaxException {
        return new KrakenWebClient(new URI("wss://ws.kraken.com/v2"), kafkaTemplate);
    }
}
