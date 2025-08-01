package dmk.poc.client.view;

import dmk.poc.client.model.AppModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Region;
import javafx.util.Builder;

public class ChatTabViewBuilder implements Builder<Region> {

    private final TabPane tabPane = new TabPane();

    public ChatTabViewBuilder(AppModel appModel) {
        appModel.setOnUserAdded(this::onUserAdded);
        tabPane.visibleProperty().bind(appModel.subscribedProperty());
    }

    @Override
    public Region build() {
        return tabPane;
    }

    private void onUserAdded(String userName, Region chatView) {
        Tab tab = new Tab();
        tab.setText(userName);
        tab.setContent(chatView);
        tabPane.getTabs().add(tab);
    }
}
