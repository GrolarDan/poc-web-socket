package dmk.poc.client.controller;

import cz.masci.springfx.mvci.controller.ViewProvider;
import dmk.poc.client.view.SubscriptionViewBuilder;
import javafx.beans.property.BooleanProperty;
import javafx.scene.layout.Region;
import lombok.RequiredArgsConstructor;

import java.util.function.Consumer;

@RequiredArgsConstructor
public class SubscriptionController implements ViewProvider<Region> {

    private final Consumer<String> onSubscribe;
    private final BooleanProperty subscribedProperty;

    @Override
    public Region getView() {
        return new SubscriptionViewBuilder(onSubscribe, subscribedProperty)
                .build();
    }


}
