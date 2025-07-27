package dmk.poc.client.controller;

import cz.masci.springfx.mvci.controller.ViewProvider;
import dmk.poc.client.model.ChatMessage;
import dmk.poc.client.service.MessageService;
import dmk.poc.client.view.SubscriptionViewBuilder;
import javafx.scene.layout.Region;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.function.Consumer;

@RequiredArgsConstructor
public class SubscriptionController implements ViewProvider<Region> {

    @NonNull
    private final AppController.AppModel appModel;
    @NonNull
    private final Consumer<ChatMessage> messageConsumer;
    private final MessageService messageService;

    @Override
    public Region getView() {
        return new SubscriptionViewBuilder(this::onSubscribe, appModel.subscribedProperty())
                .build();
    }

    private void onSubscribe(String userName) {
        appModel.setUserName(userName);
        messageService.subscribeToChat(userName, messageConsumer);
        appModel.setSubscribed(true);
    }
}
