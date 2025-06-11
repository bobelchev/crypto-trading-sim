package com.example.marketdata.market_data_streamer.consumer;

import com.example.marketdata.market_data_streamer.websocket.FrontendWebSocketHandler;
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
        System.out.println("Message received: " + message);
        frontendWebSocketHandler.pushMarketData(message);
    }
}