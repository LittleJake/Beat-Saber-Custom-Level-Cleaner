package BSCLM

import javafx.collections.FXCollections
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.Pane
import javafx.stage.DirectoryChooser

import java.io.File
import java.util.*

class Controller {

    @FXML
    private lateinit var songList: ListView<Song>
    @FXML
    private lateinit var pane: Pane
    @FXML
    private lateinit var songImage:ImageView
    @FXML
    private lateinit var songName:Label
    @FXML
    private lateinit var songSubName:Label
    @FXML
    private lateinit var songAuthorName: Label
    @FXML
    private lateinit var levelAuthorName: Label
    @FXML
    private lateinit var btnDelete: Button
    @FXML
    private lateinit var btnClear: Button

    private var song = ArrayList<Song>()
    private var folder: File? = null
    private val res = ResourceBundle.getBundle("string")
    private val player = OGGPlayer()

    @FXML
    fun deleteSong() {
        val result = Alert(Alert.AlertType.CONFIRMATION).apply {
            title = res.getString("delete.confirm.title")
            contentText = "${res.getString("delete.confirm.msg.first")} ${songList.selectionModel.selectedIndices.count()} ${res.getString("delete.confirm.msg.last")}"
        }.showAndWait()

        if ((result.isPresent) && (result.get() == ButtonType.OK)) {
            var last_index = -1
            player.stop()
            with(songList){
                for (i in selectionModel.selectedIndices.sorted()){
                    print(deleteFolder(File(song[i].getPath())))
                    song.removeAt(i)
                    last_index = i
                }

                items = FXCollections.observableArrayList(song)
                selectionModel.select(last_index)
                requestFocus()
            }
        }
    }

    @FXML
    fun clearAllSong() {
        songList.selectionModel.clearSelection()
    }

    @FXML
    fun selectFolder() {
        val folderChooser = DirectoryChooser()
        folderChooser.title = res.getString("folderChooser.title")
        folder = folderChooser.showDialog(pane.scene.window)
        if (folder != null)
            initialize()
    }

    @FXML
    fun initialize() {
        song.clear()
        folder?:return

        val files = folder!!.listFiles() ?: return
        var successCount = 0
        for (file in files) {
            if (!file.isDirectory) continue
            try {
                val s = Song(file.path)
                song.add(s)
                successCount ++
            } catch (e: Exception) {
                print(e.message)
            }
        }

        Alert(Alert.AlertType.INFORMATION).apply {
            title = res.getString("import.title")
            contentText = "${res.getString("import.msg.first")} $successCount/${files.count()} ${res.getString("import.msg.last")}"
        }.showAndWait()

        song.sortWith(Comparator.comparing(Song::getSongName))
        with(songList) {
            cellFactory = SongCellFactory()
            selectionModel.selectedItemProperty().addListener { _, _, newValue ->
                newValue?: run { btnDelete.isDisable = true; btnClear.isDisable = true; return@addListener }
                songImage.image = Image(File(newValue.getSongImage()).toURI().toString())
                songName.text = newValue.getSongName()
                songSubName.text = newValue.getSongSubName()
                songAuthorName.text = newValue.getSongAuthorName()
                levelAuthorName.text = newValue.getLevelAuthorName()
                player.play(newValue.getSong())
                System.gc()
                btnDelete.isDisable = false
                btnClear.isDisable = false
            }
            items = FXCollections.observableArrayList(song)
            selectionModel.selectionMode = SelectionMode.MULTIPLE
        }
    }

    private fun deleteFolder(folder: File):Boolean {
        if (!folder.exists()) return false
        if (folder.isDirectory) {
            val children = folder.list()
            if (children != null)
                for (child in children) {
                val bool = deleteFolder(File(folder, child))
                if (!bool) return false
            }
        }
        return folder.delete()
    }
}
