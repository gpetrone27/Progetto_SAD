
package sadprojectwork;

import java.io.IOException;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

public class PaintApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/sadprojectwork/Paint.fxml")));
        stage.setScene(scene);
        stage.setTitle("PaintApp");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }    
}
