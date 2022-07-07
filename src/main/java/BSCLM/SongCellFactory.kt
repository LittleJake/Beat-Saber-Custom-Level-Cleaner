package BSCLM

import javafx.scene.control.ListCell
import javafx.scene.control.ListView
import javafx.util.Callback

class SongCellFactory: Callback<ListView<Song>, ListCell<Song>> {
    override fun call(param: ListView<Song>?): ListCell<Song> {
        return object : ListCell<Song>() {
            override fun updateItem(song: Song?, empty: Boolean) {
                super.updateItem(song, empty)
                text = if (empty || song == null) {
                    null
                } else {
                    "${song.getSongName().ifEmpty { "Untitled" }}-${song.getSongAuthorName().ifEmpty { "Noname" }}"
                }
            }
        }
    }
}