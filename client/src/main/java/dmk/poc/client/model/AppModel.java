package dmk.poc.client.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.layout.Region;
import lombok.Getter;
import lombok.Setter;

import java.util.function.BiConsumer;

public class AppModel {
    private final BooleanProperty subscribedProperty = new SimpleBooleanProperty(false);
    @Getter
    @Setter
    private String userName;
    @Getter
    @Setter
    private BiConsumer<String, Region> onUserAdded;

    public BooleanProperty subscribedProperty() {
        return subscribedProperty;
    }

    public void setSubscribed(boolean isSubscribed) {
        this.subscribedProperty.set(isSubscribed);
    }
}
