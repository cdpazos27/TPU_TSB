package GUI_Layer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Resultados PASO 2019");
        primaryStage.setScene(new Scene(root, 750, 750));
        ClassLoader classLoader = getClass().getClassLoader();
        try {
            primaryStage.getIcons().add(new Image("http://www.cryptopepe.net/img/market/pepe01.png"));
        }
        catch (Exception ex) {}
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
