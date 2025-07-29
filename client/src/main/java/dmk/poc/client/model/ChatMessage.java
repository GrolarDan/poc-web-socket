package dmk.poc.client.model;

import lombok.Data;

import java.util.List;

@Data
public class ChatMessage {
    private MessageType type;
    private String content;
    private String sender;
    private String receiver;
    private List<String> users;

    public enum MessageType {
        CHAT,
        JOIN,
        JOIN_PJ,
        LEAVE
    }
}