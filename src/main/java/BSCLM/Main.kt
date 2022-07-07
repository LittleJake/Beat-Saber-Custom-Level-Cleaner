package BSCLM
import javafx.application.Application
import javafx.application.Platform
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage
import java.util.*

class Main : Application() {
    override fun start(primaryStage: Stage) {
        val res = ResourceBundle.getBundle("string")
        val root: Parent = FXMLLoader.load(javaClass.classLoader.getResource("beat_saber.fxml"), res)
        with (primaryStage){
            title = res.getString("app_title")
            scene = Scene(root, 570.0, 400.0)
            isResizable = false
            isFullScreen = false
            setOnCloseRequest {
                Platform.exit()
                kotlin.system.exitProcess(0)
            }
            show()
        }
    }
}

fun main(args: Array<String>) {
    Application.launch(Main::class.java, *args)
}
