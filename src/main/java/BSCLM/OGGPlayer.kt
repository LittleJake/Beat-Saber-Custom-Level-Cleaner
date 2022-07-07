package BSCLM

import javax.sound.sampled.*
import java.io.File

class OGGPlayer {
    private var t: Thread? = null
    private lateinit var file: File
    private var input: AudioInputStream? = null
    private var line:SourceDataLine? = null
    fun play(filePath:String) {
        synchronized (this) {
            stop()

            t = Thread {
                file = File (filePath)
                try {
                    input = AudioSystem.getAudioInputStream(file)
                    val outFormat = getOutFormat(input!!.format)
                    val info = DataLine.Info(SourceDataLine::class.java, outFormat)

                    val line = AudioSystem.getLine(info) as SourceDataLine
                    with(line) {
                        open(outFormat)
                        start()
                        stream(AudioSystem.getAudioInputStream(outFormat, input), line)
                        drain()
                        stop()
                    }
                } catch (e: Exception) {
                    throw IllegalStateException (e.message)
                }
            }
            t?.start()
        }
    }

    fun stop() {
        input?.close()
        line?.close()
        t?.stop()
    }

    private fun getOutFormat(inFormat: AudioFormat):AudioFormat {
        val ch = inFormat.channels
        val rate = inFormat.sampleRate
        return AudioFormat(AudioFormat.Encoding.PCM_SIGNED, rate, 16, ch, ch * 2, rate, false)
    }

    private fun stream(input:AudioInputStream, line:SourceDataLine) {
        val buffer = ByteArray(1024)
        var n = input.read(buffer, 0, buffer.size)
        while (n != -1) {
            line.write(buffer, 0, n)
            n = input.read(buffer, 0, buffer.size)
        }
    }
}