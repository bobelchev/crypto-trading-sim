package com.example.crypto.websocketclient;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
/**
 * WebSocket client for connecting to Kraken's WebSocket V2 API.
 * Built using the Java-WebSocket library:
 * https://github.com/TooTallNate/Java-WebSocket
 *
 * Licensed under the MIT License.
 * https://github.com/TooTallNate/Java-WebSocket/blob/master/LICENSE
 */

public class KrakenWebClient extends WebSocketClient {
    public KrakenWebClient(URI serverUri, Draft draft) {
        super(serverUri, draft);
    }

    public KrakenWebClient(URI serverURI) {
        super(serverURI);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("new connection opened");

        String subMessage = """
                {
                            "method": "subscribe",
                            "params": {
                                "channel": "ticker",
                                "symbol": ["BTC/USD"],
                                "snapshot": true
                            }
                        }
                
                """;
        send(subMessage);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("closed with exit code " + code + " additional info: " + reason);
    }

    @Override
    public void onMessage(String message) {
        System.out.println("received message: " + message);
    }

    @Override
    public void onMessage(ByteBuffer message) {
        System.out.println("received ByteBuffer");
    }

    @Override
    public void onError(Exception ex) {
        System.err.println("an error occurred:" + ex);
    }

    public static void main(String[] args) throws URISyntaxException {
        WebSocketClient client = new KrakenWebClient(new URI("wss://ws.kraken.com/v2"));
        client.connect();
    }
}
