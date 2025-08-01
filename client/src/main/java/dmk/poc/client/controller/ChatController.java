package dmk.poc.client.controller;

import cz.masci.springfx.mvci.controller.ViewProvider;
import cz.masci.springfx.mvci.util.builder.BackgroundTaskBuilder;
import dmk.poc.client.model.AppModel;
import dmk.poc.client.model.ChatMessage;
import dmk.poc.client.service.MessageService;
import dmk.poc.client.view.ChatMessageViewBuilder;
import dmk.poc.client.view.ChatViewBuilder;
import javafx.scene.layout.Region;
import lombok.NonNull;

public class ChatController implements ViewProvider<Region> {

    private final MessageService messageService;
    @NonNull
    private final AppModel appModel;
    private final String receiver;
    private final ChatViewBuilder chatViewBuilder;

    public ChatController(@NonNull AppModel appModel, String receiver, MessageService messageService) {
        this.messageService = messageService;
        this.appModel = appModel;
        this.receiver = receiver;
        chatViewBuilder = new ChatViewBuilder(this::onMessageSend);
    }

    @Override
    public Region getView() {
        return chatViewBuilder.build();
    }

    public void addMessage(ChatMessage message) {
        var messageBubble = new ChatMessageViewBuilder(appModel, message).build();
        chatViewBuilder.addMessageBubble(messageBubble);
    }

    private void onMessageSend(String message, Runnable clearInput) {
        // Logic to handle the message sending
        System.out.println("Message sent: " + message);
        var chatMessage = new ChatMessage();
        chatMessage.setSender(appModel.getUserName());
        chatMessage.setReceiver(receiver);
        chatMessage.setContent(message);
        chatMessage.setType(ChatMessage.MessageType.CHAT);

        BackgroundTaskBuilder.task(() -> messageService.sendMessage(chatMessage))
                .onSucceeded(this::addMessage)
                .postGuiCall(clearInput)
                .start();
    }
}
