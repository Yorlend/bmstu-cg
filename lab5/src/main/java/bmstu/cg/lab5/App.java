package bmstu.cg.lab5;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("lab-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1700, 800);
        stage.setTitle("Растровая развертка сплошных областей");
        stage.setScene(scene);
        var controller = (Controller)fxmlLoader.getController();
        scene.setOnKeyPressed(controller::onKeyPressed);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}