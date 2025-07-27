package dmk.poc.client;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PocWebSocketClientApplication {

    public static void main(String[] args) {
        Application.launch(JavaFxApplication.class, args);
    }

}
