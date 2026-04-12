package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.service.CartService;
import org.example.service.LocalizationService;

import java.net.URL;
import java.util.Map;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the FXML file
        URL fxmlUrl = getClass().getResource("/org/example/main_view.fxml");
        FXMLLoader loader = new FXMLLoader(fxmlUrl);

        CartService cartService = new CartService();
        Map<String, String> rb = LocalizationService.getStrings("en");

        loader.setController(new Controller(cartService, rb));
        VBox root = loader.load();

        Scene scene = new Scene(root, 500, 600);

        primaryStage.setTitle("Olga Chitembo/Shopping Cart");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
