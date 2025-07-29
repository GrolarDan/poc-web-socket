package dmk.poc.websocket.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ChatMessage {
    private MessageType type;
    private String content;
    private String sender;
    private String receiver;
    private List<String> users = new ArrayList<>();

    public enum MessageType {
        CHAT,
        JOIN,
        JOIN_PJ,
        LEAVE
    }
}