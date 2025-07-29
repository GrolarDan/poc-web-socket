package dmk.poc.client.controller;

import cz.masci.springfx.mvci.controller.ViewProvider;
import dmk.poc.client.model.AppModel;
import dmk.poc.client.model.ChatMessage;
import dmk.poc.client.service.MessageService;
import dmk.poc.client.view.ChatTabViewBuilder;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static cz.masci.springfx.mvci.util.ConcurrentUtils.runInFXThread;

@Component
@RequiredArgsConstructor
public class AppController implements ViewProvider<Region> {

    private final MessageService messageService;

    private final Map<String, ChatController> chatControllerMap = new HashMap<>();
    private final AppModel appModel = new AppModel();

    @Override
    public Region getView() {
        var subscriptionController = new SubscriptionController(this::onSubscribe, appModel.subscribedProperty());
        var chatTabViewBuilder = new ChatTabViewBuilder(appModel);

        var root = new StackPane();
        root.getChildren().add(subscriptionController.getView());
        root.getChildren().add(chatTabViewBuilder.build());

        return root;
    }

    private void addChatView(String userName) {
        if (!chatControllerMap.containsKey(userName)) {
            var chatController = new ChatController(appModel, userName, messageService);
            chatControllerMap.put(userName, chatController);
            appModel.getUsers().add(userName);
            appModel.getOnUserAdded().accept(userName, chatController.getView());
        }
    }

    private void addMessage(ChatMessage message) {
        runInFXThread(() -> chatControllerMap.get(message.getSender()).addMessage(message));
    }

    private void onSubscribe(String userName) {
        appModel.setUserName(userName);
        messageService.subscribeToChat(userName, this::addMessage, this::addChatView);
        appModel.setSubscribed(true);
    }

}
