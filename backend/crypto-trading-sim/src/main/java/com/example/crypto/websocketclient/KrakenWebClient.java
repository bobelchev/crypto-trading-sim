package com.example.crypto.websocketclient;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * WebSocket client for connecting to Kraken's WebSocket V2 API.
 * Built using the Java-WebSocket library:
 * https://github.com/TooTallNate/Java-WebSocket
 * Licensed under the MIT License.
 * https://github.com/TooTallNate/Java-WebSocket/blob/master/LICENSE
 */

public class KrakenWebClient extends WebSocketClient {
    private static final String[] TOP_X_CRYPTO = {
            "BTC/USD", "ETH/USD", "USDT/USD", "BNB/USD", "SOL/USD",
            "XRP/USD", "DOGE/USD"
    };
    private final Map<String, Double> marketData = new HashMap<>();


    public KrakenWebClient(URI serverUri, Draft draft) {
        super(serverUri, draft);
    }

    public KrakenWebClient(URI serverURI) {
        super(serverURI);
    }
    private void initializeMarketData(){
        for (String symbol : TOP_X_CRYPTO) {
            marketData.put(symbol,0.0);
        }
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        initializeMarketData();
        System.out.println("Market Data init");
        System.out.println("new connection opened");
        String pairs = "\""+ String.join("\", \"", marketData.keySet())+"\"";

        //"symbol": ["BTC/USD", "ETH/USD"],
        String subMessage = String.format("""
                {
                            "method": "subscribe",
                            "params": {
                                "channel": "ticker",
                                "symbol": [%s],
                                "snapshot": true
                            }
                        }
                
                """,pairs);

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
