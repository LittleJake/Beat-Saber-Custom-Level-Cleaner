package BSCLM;

import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class SongCellFactory implements Callback<ListView<Song>, ListCell<Song>> {
    @Override
    public ListCell<Song> call(ListView<Song> param) {
        return new ListCell<Song>(){
            @Override
            public void updateItem(Song song, boolean empty) {
                super.updateItem(song, empty);
                if (empty || song == null) {
                    setText(null);
                } else {
                    String songName = song.getSongName();
                    if (songName.equals(""))
                        songName = "Untitled";
                    String authorName = song.getSongAuthorName();
                    if (authorName.equals(""))
                        authorName = "Noname";
                    setText(songName + "-" + authorName);
                }
            }
        };
    }
}