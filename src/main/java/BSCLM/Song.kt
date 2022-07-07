package BSCLM

import java.io.*
import java.nio.charset.StandardCharsets

import com.alibaba.fastjson.*


class Song(path: String) {
    private var path: String
    private lateinit var song: String
    private lateinit var songImage: String
    private lateinit var songName: String
    private lateinit var  songSubName: String
    private lateinit var  songAuthorName: String
    private lateinit var  levelAuthorName: String

    init {
        this.path = path
        loadFromPath()
    }

    fun getPath():String { return path }
    fun getSong():String { return song }
    fun getSongImage():String { return songImage }
    fun getSongName():String { return songName }
    fun getSongSubName():String { return songSubName }
    fun getSongAuthorName():String { return songAuthorName }
    fun getLevelAuthorName():String { return levelAuthorName }


    private fun loadFromPath() {
        try {
            val info = File("$path/info.dat")
            val br = info.bufferedReader(StandardCharsets.UTF_8)
            val infoDat = StringBuilder()

            br.use {
                var buf = it.readLine()
                while(buf != null){
                    infoDat.append(buf)
                    buf = it.readLine()
                }
            }

            val json = JSON.parseObject(infoDat.toString())

            songName = json.getString("_songName").ifEmpty { "Undefined" }
            songSubName = json.getString("_songSubName").ifEmpty { "Undefined" }
            songAuthorName = json.getString("_songAuthorName").ifEmpty { "Undefined" }
            levelAuthorName = json.getString("_levelAuthorName").ifEmpty { "Undefined" }
            song = "$path/${json.getString("_songFilename")}"
            songImage = "$path/${json.getString("_coverImageFilename")}"

        } catch (e:NullPointerException){
            throw FileNotFoundException("$path 不是正确的文件夹")
        }catch (e:FileNotFoundException){
            throw FileNotFoundException("$path 不是正确的文件夹")
        } catch (e:IOException) {
            throw IOException("读取info失败")
        }
    }
}
