package dmk.poc.client.view;

import dmk.poc.client.controller.AppController;
import dmk.poc.client.model.ChatMessage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ChatMessageViewBuilder implements Builder<Region> {

    @NonNull
    private final AppController.AppModel appModel;
    @NonNull
    private final ChatMessage chatMessage;

    @Override
    public Region build() {
        if (ChatMessage.MessageType.JOIN.equals(chatMessage.getType())) {
            TextFlow messageBubble = new TextFlow(new Text("%s has joined the chat".formatted(chatMessage.getSender())));
            messageBubble.setPadding(new Insets(10));

            HBox messageContainer = new HBox(messageBubble);
            messageContainer.setAlignment(Pos.CENTER);
            messageContainer.setPadding(new Insets(5));

            return messageContainer;
        } else {
            boolean isUser = chatMessage.getSender().equals(appModel.getUserName());

            TextFlow messageBubble = new TextFlow(new Text(chatMessage.getContent()));
            messageBubble.setPadding(new Insets(10));
            messageBubble.setStyle("-fx-background-color: " + (isUser ? "#d1e7dd;" : "#f8d7da;") +
                    " -fx-background-radius: 10;");

            HBox messageContainer = new HBox(messageBubble);
            messageContainer.setAlignment(isUser ? Pos.CENTER_RIGHT : Pos.CENTER_LEFT);
            messageContainer.setPadding(new Insets(5));

            return messageContainer;
        }
    }
}
