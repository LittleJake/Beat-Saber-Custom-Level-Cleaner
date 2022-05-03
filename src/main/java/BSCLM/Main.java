package BSCLM;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("beat_saber.fxml"));
        primaryStage.setTitle("Beat Saber歌曲清理器");
        primaryStage.setScene(new Scene(root, 570, 400));
        primaryStage.setResizable(false);
        primaryStage.setFullScreen(false);
        primaryStage.setOnCloseRequest(t -> {
            Platform.exit();
            System.exit(0);
        });
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
