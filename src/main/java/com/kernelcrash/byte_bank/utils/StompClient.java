package com.kernelcrash.byte_bank.utils;

import com.google.gson.Gson;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.lang.reflect.Type;
import java.util.concurrent.ExecutionException;

public class StompClient {
    private static final WebSocketClient client = new StandardWebSocketClient();
    private static final WebSocketStompClient stompClient = new WebSocketStompClient(client);
    private static final StompSessionHandlerAdapter sessionHandler = new StompSessionHandlerAdapter() {
        @Override
        public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
            System.out.println("Connected to the server");
        }

        @Override
        public Type getPayloadType(StompHeaders headers) {
            return String.class;
        }

        @Override
        public void handleFrame(StompHeaders headers, Object payload) {
//            System.out.println("Received data");

            CurrencyDataStore.parseCoinbaseJSONData((String) payload);
        }
    };

    public static void fetchCryptoDataFeed() throws ExecutionException, InterruptedException {
        stompClient.setMessageConverter(new StringMessageConverter());
        String url = ConfigHelper.WS_CRYPTO_SOCKET_URL;
        StompSession session = stompClient.connectAsync(url, sessionHandler).get();
        session.subscribe("/topic/crypto-prices", sessionHandler);
    }

    public static void disconnect() {
        stompClient.stop();
    }
}
