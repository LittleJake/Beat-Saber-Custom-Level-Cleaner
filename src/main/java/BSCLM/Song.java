package BSCLM;

import java.io.*;
import java.nio.charset.StandardCharsets;

import com.alibaba.fastjson.*;


public class Song {
    private final File path;
    private String song;
    private String songImage;
    private String songName = "Undefined";
    private String songSubName = "Undefined";
    private String songAuthorName = "Undefined";
    private String levelAuthorName = "Undefined";
    public File getPath() {
        return path;
    }

    public String getSong() {
        return song;
    }


    public String getSongImage() {
        return songImage;
    }


    public String getSongName() {
        return songName;
    }


    public String getSongSubName() {
        return songSubName;
    }


    public String getSongAuthorName() {
        return songAuthorName;
    }


    public String getLevelAuthorName() {
        return levelAuthorName;
    }

    public Song(File path) throws IOException {
        this.path = path;
        this.loadFromPath();
    }


    private void loadFromPath() throws IOException {
        try {
            File info = new File(path.getPath() + "/info.dat");
            FileInputStream fis = new FileInputStream(info);
            InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(isr);

            StringBuilder info_dat = new StringBuilder();
            String buf;

            while((buf = br.readLine()) != null)
                info_dat.append(buf);

            br.close();

            JSONObject json = JSON.parseObject(info_dat.toString());

            if(!json.getString("_songName").isEmpty()) this.songName = json.getString("_songName");
            if(!json.getString("_songSubName").isEmpty()) this.songSubName = json.getString("_songSubName");
            if(!json.getString("_songAuthorName").isEmpty()) this.songAuthorName = json.getString("_songAuthorName");
            if(!json.getString("_levelAuthorName").isEmpty()) this.levelAuthorName = json.getString("_levelAuthorName");

            this.song = path.getPath() + "/"+ json.getString("_songFilename");
            this.songImage = path.getPath() + "/"+ json.getString("_coverImageFilename");

        } catch (NullPointerException | FileNotFoundException e){
            throw new FileNotFoundException(path.getPath() + " 不是正确的文件夹");
        } catch (IOException e) {
            throw new IOException("读取info失败");
        }
    }
}
