package BSCLM;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;

import java.io.File;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;

public class Controller {

    @FXML
    private ListView<Song> song_list;
    @FXML
    private Pane pane;
    @FXML
    private ImageView song_image;
    @FXML
    private Label song_name;
    @FXML
    private Label song_sub_name;
    @FXML
    private Label song_author_name;
    @FXML
    private Label level_author_name;

    private ArrayList<Song> song;
    private File folder;

    final OGGPlayer player = new OGGPlayer();

    @FXML
    public void deleteSong()
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("删除确认");
        String s ="你将要删除 " + song_list.getSelectionModel().getSelectedIndices().size() + " 项文件";
        alert.setContentText(s);
        Optional<ButtonType> result = alert.showAndWait();

        if ((result.isPresent()) && (result.get() == ButtonType.OK)) {
            int last_index = -1;
            player.stop();
            for (int i: song_list.getSelectionModel().getSelectedIndices().sorted((o1, o2) -> o2-o1)){
                System.out.print(deleteFolder(song.get(i).getPath()));
                song.remove(i);
                last_index = i;
            }

            song_list.setItems(FXCollections.observableArrayList(song));
            song_list.getSelectionModel().select(last_index);
            song_list.requestFocus();
        }
    }

    @FXML
    public void clearAllSong()
    {
        song_list.getSelectionModel().clearSelection();
    }

    @FXML
    public void selectFolder()
    {
        DirectoryChooser folderChooser = new DirectoryChooser();
        folderChooser.setTitle("选择BeatSaber自定义曲目文件夹");
        folder = folderChooser.showDialog(pane.getScene().getWindow());
        if (folder != null)
            initialize();
    }

    @FXML
    private void initialize()
    {
        song = new ArrayList<>();
        if(folder == null) return;

        File[] files = folder.listFiles();

        if (files == null) return;

        for (File file: files) {
            if (!file.isDirectory()) continue;
            try {
                Song s = new Song(file);
                song.add(s);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        song.sort(Comparator.comparing(Song::getSongName));
        song_list.setCellFactory(new SongCellFactory());
        song_list.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    if (newValue == null) return;
                        song_image.setImage(new Image(new File(newValue.getSongImage()).toURI().toString()));
                        song_name.setText(newValue.getSongName());
                        song_sub_name.setText(newValue.getSongSubName());
                        song_author_name.setText(newValue.getSongAuthorName());
                        level_author_name.setText(newValue.getLevelAuthorName());
                        player.play(newValue.getSong());
                });

        song_list.setItems(FXCollections.observableArrayList(song));
        song_list.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

    }

    private boolean deleteFolder(File folder) {
        if (!folder.exists()) return false;
        if (folder.isDirectory()) {
            String[] children = folder.list();
            if (children != null)
                for (String child : children) {
                    boolean bool = deleteFolder(new File(folder, child));
                    if (!bool) return false;
                }
        }
        return folder.delete();
    }
}
