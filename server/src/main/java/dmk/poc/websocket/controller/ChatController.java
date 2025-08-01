package dmk.poc.websocket.controller;

import dmk.poc.websocket.model.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ChatController {
    private volatile String pjUserName;
    private final List<String> users = Collections.synchronizedList(new ArrayList<>());
    private final SimpMessagingTemplate messagingTemplate;

    @GetMapping("/chat/pjUserName")
    public String getPjUserName() {
        return  pjUserName;
    }

    @GetMapping("/chat/users")
    public List<String> getUsers() {
        return users;
    }

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessage chatMessage) {
        log.info("Incoming message: {}", chatMessage);
        // The destination is /queue/private, so the full path for the user is /user/{username}/queue/private
        messagingTemplate.convertAndSendToUser(
                Objects.requireNonNull(chatMessage.getReceiver(), "Message receiver cannot be null"),
                "/queue/private",
                chatMessage
        );
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage,
                               SimpMessageHeaderAccessor headerAccessor) {
        log.info("Adding user: {}", chatMessage.getSender());
        // Add username in web socket session
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        users.add(chatMessage.getSender());

        return chatMessage;
    }

    @MessageMapping("/chat.addPjUser")
    @SendTo("/topic/public")
    public ChatMessage addPjUser(@Payload ChatMessage chatMessage,
                                 SimpMessageHeaderAccessor headerAccessor) {
        log.info("Adding PJ user {}", chatMessage.getSender());
        // Add username in web socket session
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        pjUserName = chatMessage.getSender();

        return chatMessage;
    }
}
