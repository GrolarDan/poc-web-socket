package dmk.poc.client;

import dmk.poc.client.controller.AppController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class JavaFxApplication extends Application {

    private ConfigurableApplicationContext applicationContext;

    @Override
    public void init() {
        String[] args = getParameters().getRaw().toArray(new String[0]);

        this.applicationContext = new SpringApplicationBuilder()
                .sources(PocWebSocketClientApplication.class)
                .run(args);
    }

    @Override
    public void start(Stage stage) {
        var appController = applicationContext.getBean(AppController.class);

        Scene scene = new Scene(appController.getView(), 800, 600);
        stage.setTitle("Aplikace Dračí Doupě - Chatovací klient");
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() {
        this.applicationContext.close();
        Platform.exit();
    }

}
