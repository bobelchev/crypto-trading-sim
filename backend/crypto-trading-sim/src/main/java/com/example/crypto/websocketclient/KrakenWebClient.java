package com.example.crypto.websocketclient;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONObject;

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

    private void initializeMarketData() {
        for (String symbol : TOP_X_CRYPTO) {
            marketData.put(symbol, 0.0);
        }
    }
    private void updateMarketData(String ticker, double price){
        marketData.put(ticker,price);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        initializeMarketData();
        System.out.println("Market Data init");
        System.out.println("new connection opened");
        String pairs = "\"" + String.join("\", \"", marketData.keySet()) + "\"";

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
                
                """, pairs);

        send(subMessage);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("closed with exit code " + code + " additional info: " + reason);
    }

    @Override
    public void onMessage(String message) {
        //System.out.println("received message: " + message);
        //{"channel":"ticker","type":"snapshot","data":[{"symbol":"BNB/USD","bid":598.43,"bid_qty":0.83551,"ask":600.08,"ask_qty":8.20024,"last":600.21,"volume":485.14555,"vwap":602.41,"low":597.39,"high":608.89,"change":-1.37,"change_pct":-0.23}]}
        JSONObject json = new JSONObject(message);
        //System.out.println(json.toString(2));

        if (json.has("channel") && "ticker".equals(json.getString("channel")) && json.has("data")) {
            //"data":[{"symbol":"BNB/USD","bid":598.43,"bid_qty":0.83551,"ask":600.08,"ask_qty":8.20024,"last":600.21,"volume":485.14555,"vwap":602.41,"low":597.39,"high":608.89,"change":-1.37,"change_pct":-0.23}]
            JSONArray data = json.getJSONArray("data");
            //System.out.println(data.toString(2));
            //{"symbol":"BNB/USD","bid":598.43,"bid_qty":0.83551,"ask":600.08,"ask_qty":8.20024,"last":600.21,"volume":485.14555,"vwap":602.41,"low":597.39,"high":608.89,"change":-1.37,"change_pct":-0.23}
            JSONObject firstEntry = data.getJSONObject(0);
            String symbol = firstEntry.getString("symbol");
            double last = firstEntry.getDouble("last");
            System.out.println("Symbol: " + symbol);
            System.out.println("Last: " + last);
            updateMarketData(symbol,last);
            System.out.println(marketData);
        }

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
