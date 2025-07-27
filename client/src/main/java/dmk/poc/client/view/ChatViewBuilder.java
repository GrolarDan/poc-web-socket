package dmk.poc.client.view;

import javafx.beans.property.BooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.function.BiConsumer;

@RequiredArgsConstructor
public class ChatViewBuilder implements Builder<VBox> {

    @NonNull
    private final BiConsumer<String, Runnable> onMessageSend;
    @NonNull
    private final BooleanProperty isSubscribed;
    private VBox chatBox;

    @Override
    public VBox build() {
        // Main chat container
        chatBox = new VBox(10);
        chatBox.setPadding(new Insets(10));
        chatBox.setStyle("-fx-background-color: #f4f4f4;");

        // ScrollPane for chat messages
        ScrollPane scrollPane = new ScrollPane(chatBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);

        // Input field and send button
        TextField inputField = new TextField();
        inputField.setPromptText("Type your message...");
        inputField.setPrefWidth(300);

        inputField.setOnAction(event -> {
            String message = inputField.getText().trim();
            if (!message.isEmpty()) {
                onMessageSend.accept(message, inputField::clear);
            }
        });

        // Layout for input
        HBox inputBox = new HBox(10, inputField);
        inputBox.setPadding(new Insets(10));
        inputBox.setAlignment(Pos.CENTER);

        // Main layout
        VBox root = new VBox(10, scrollPane, inputBox);
        root.setStyle("-fx-background-color: #ffffff;");
        root.visibleProperty().bind(isSubscribed);

        return root;
    }

    public void addMessageBubble(Region messageBubble) {
        chatBox.getChildren().add(messageBubble);
    }
}
