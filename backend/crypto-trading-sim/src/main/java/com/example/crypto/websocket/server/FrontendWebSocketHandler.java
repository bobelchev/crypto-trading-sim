package com.example.crypto.websocket.server;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class FrontendWebSocketHandler implements WebSocketHandler {
    private final List<WebSocketSession> sessions = new ArrayList<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        System.out.println("Client connected");
        addSession(session);
    }

    public List<WebSocketSession> getSessions() {
        return sessions;
    }
    public synchronized void addSession(WebSocketSession session) {
        sessions.add(session);
    }

    public synchronized void removeSession(WebSocketSession session) {
        sessions.remove(session);
    }

    public void pushMarketData(String message){
        System.out.println("Update");
        for (WebSocketSession session : sessions) {
            System.out.println("Into the loop");
            TextMessage msg = new TextMessage("Update market data");
            if (session.isOpen()) {
                try {
                    System.out.println("Message to be sent");
                    session.sendMessage(new TextMessage(message));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("Session closed: " + session.getId());
            }
        }

    }
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {

    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        System.out.println("Session closed: " + session.getId());
        removeSession(session);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
