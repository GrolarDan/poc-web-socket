package dmk.poc.client.controller;

import cz.masci.springfx.mvci.controller.ViewProvider;
import dmk.poc.client.model.ChatMessage;
import dmk.poc.client.service.MessageService;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import static cz.masci.springfx.mvci.util.ConcurrentUtils.runInFXThread;

@Component
public class AppController implements ViewProvider<Region> {

    private final SubscriptionController subscriptionController;
    private final ChatController chatController;

    public AppController(MessageService messageService) {
        var model = new AppModel();
        chatController = new ChatController(model, messageService);
        subscriptionController = new SubscriptionController(model, this::addMessage, messageService);
    }

    @Override
    public Region getView() {
        var root = new StackPane();
        root.getChildren().add(subscriptionController.getView());
        root.getChildren().add(chatController.getView());

        return root;
    }

    private void addMessage(ChatMessage message) {
        runInFXThread(() -> chatController.addMessage(message));
    }

    public static class AppModel {
        @Getter
        @Setter
        private String userName;
        private final BooleanProperty subscribedProperty = new SimpleBooleanProperty(false);

        public BooleanProperty subscribedProperty() {
            return subscribedProperty;
        }

        public void setSubscribed(boolean isSubscribed) {
            this.subscribedProperty.set(isSubscribed);
        }
    }
}
