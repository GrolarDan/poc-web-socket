package dmk.poc.client.service;

import dmk.poc.client.model.ChatMessage;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.*;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

@Service
@Slf4j
public class MessageService {

    private final String webSocketUrl;
    private final WebSocketStompClient stompClient;
    private StompSession stompSession;

    public MessageService(@Value("${websocket.url}") String baseUrl, @Value("${websocket.port}") String port) {
        List<Transport> transports = List.of(new WebSocketTransport(new StandardWebSocketClient()));
        WebSocketClient webSocketClient = new SockJsClient(transports);
        webSocketUrl = "http://%s:%s/ws".formatted(baseUrl, port);
        stompClient = new WebSocketStompClient(webSocketClient);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());
    }

    public StompSession subscribeToChat(String userName, Consumer<ChatMessage> onMessageReceived, Consumer<String> onUserAdd) {
        StompSessionHandler sessionHandler = new StompSessionHandlerAdapter() {
            @Override
            public void afterConnected(StompSession session, @NotNull StompHeaders connectedHeaders) {
                // subscribe
                session.subscribe("/user/queue/private", new StompFrameMessageHandler(onMessageReceived));
                // subscribe to join new user
                session.subscribe("/topic/public", new StompFrameJoinHandler(onUserAdd));

                var message = new ChatMessage();
                message.setType(ChatMessage.MessageType.JOIN_PJ);
                message.setSender(userName);
                session.send("/app/chat.addPjUser", message);
            }

        };

        var future = stompClient.connectAsync("%s?username=%s".formatted(webSocketUrl, userName), sessionHandler);
        try {
            stompSession = future.get();
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("Failed to connect to WebSocket server: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return stompSession;
    }

    public ChatMessage sendMessage(ChatMessage message) {
        if (stompSession == null || !stompSession.isConnected()) {
            throw new IllegalStateException("WebSocket session is not connected");
        }
        log.info("Sending message: {}", message);
        stompSession.send("/app/chat.sendMessage", message);
        return  message;
    }

    private record StompFrameJoinHandler(Consumer<String> onAddUser) implements StompFrameHandler {
        @NotNull
        @Override
        public Type getPayloadType(@NotNull StompHeaders headers) {
            return ChatMessage.class;
        }

        @Override
        public void handleFrame(@NotNull StompHeaders headers, Object payload) {
            if (payload instanceof ChatMessage chatMessage && ChatMessage.MessageType.JOIN.equals(chatMessage.getType())) {
                onAddUser.accept(chatMessage.getSender());
            }
        }
    }

    private record StompFrameMessageHandler(Consumer<ChatMessage> onMessageReceived) implements StompFrameHandler {
        @NotNull
        @Override
        public Type getPayloadType(@NotNull StompHeaders headers) {
            return ChatMessage.class;
        }

        @Override
        public void handleFrame(@NotNull StompHeaders headers, Object payload) {
            if (payload instanceof ChatMessage chatMessage) {
                if (ChatMessage.MessageType.CHAT.equals(chatMessage.getType())) {
                    onMessageReceived.accept(chatMessage);
                }
            }
        }
    }
}
