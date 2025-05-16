
package sadprojectwork;

import java.io.IOException;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;

public class PaintApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/logo.png")));
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/ui/arsnova.fxml")));
        stage.setScene(scene);
        stage.setTitle("ArsNova");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }    
}
