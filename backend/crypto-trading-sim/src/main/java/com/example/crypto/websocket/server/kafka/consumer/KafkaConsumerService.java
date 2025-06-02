package com.example.crypto.websocket.server.kafka.consumer;

import com.example.crypto.websocket.server.FrontendWebSocketHandler;
import com.example.crypto.websocket.server.FrontendWebSocketServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {
    @Autowired
    private final FrontendWebSocketHandler frontendWebSocketHandler;

    public KafkaConsumerService(FrontendWebSocketHandler frontendWebSocketHandler) {
        this.frontendWebSocketHandler = frontendWebSocketHandler;
    }

    @KafkaListener(topics = "my_topic", groupId = "group_id")
    public void consume(String message) {
        frontendWebSocketHandler.pushMarketData(message);
        System.out.println("Message received: " + message);
    }
}