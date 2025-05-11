package com.example.crypto.websocket.server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class FrontendWebSocketServerConfig implements WebSocketConfigurer {
    private final FrontendWebSocketHandler handler;
    public FrontendWebSocketServerConfig(FrontendWebSocketHandler handler) {
        this.handler = handler;
    }
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(handler, "/ws/marketdata").setAllowedOrigins("*");
    }

}
