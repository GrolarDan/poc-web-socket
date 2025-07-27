package dmk.poc.client.view;

import javafx.beans.property.BooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.util.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.function.Consumer;

@RequiredArgsConstructor
public class SubscriptionViewBuilder implements Builder<Region> {

    @NonNull
    private final Consumer<String> onSubscribe;
    @NonNull
    private final BooleanProperty isSubscribed;

    @Override
    public Region build() {
        TextField userName = new TextField("User: John Doe");
        userName.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Button subscribeButton = new Button("Subscribe");
        subscribeButton.setDefaultButton(true);
        subscribeButton.disableProperty().bind(userName.textProperty().isEmpty());
        subscribeButton.setOnAction(event -> {
            onSubscribe.accept(userName.getText().trim());
            isSubscribed.set(true);
        });

        VBox subscribeBox = new VBox(10);
        subscribeBox.setAlignment(Pos.CENTER);
        subscribeBox.setPadding(new Insets(10));
        subscribeBox.setStyle("-fx-background-color: #f4f4f4;");
        subscribeBox.visibleProperty().bind(isSubscribed.not());

        subscribeBox.getChildren().add(userName);
        subscribeBox.getChildren().add(subscribeButton);

        return subscribeBox;
    }
}
