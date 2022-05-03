package BSCLM;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class OGGPlayer {
    private Thread t;
    private File file;
    AudioInputStream in;
    SourceDataLine line;
    public void play(String filePath) {
        synchronized (this) {
            stop();

            t = new Thread(()->{
                file = new File(filePath);
                try {
                    in = AudioSystem.getAudioInputStream(file);

                    AudioFormat outFormat = getOutFormat(in.getFormat());
                    DataLine.Info info = new DataLine.Info(SourceDataLine.class, outFormat);

                    SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);


                    if (line != null) {
                        line.open(outFormat);
                        line.start();
                        stream(AudioSystem.getAudioInputStream(outFormat, in), line);
                        line.drain();
                        line.stop();
                    }
                } catch (Exception e) {
                    throw new IllegalStateException(e);
                }
            });
            t.start();
        }
    }

    public void stop() {
        if (in != null)
            try{
                in.close();
            } catch (Exception ignored){}

        if (line != null)
            line.close();
        if (t != null)
            t.stop();
    }

    private AudioFormat getOutFormat(AudioFormat inFormat) {
        final int ch = inFormat.getChannels();
        final float rate = inFormat.getSampleRate();
        return new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, rate, 16, ch, ch * 2, rate, false);
    }

    private void stream(AudioInputStream in, SourceDataLine line)
            throws IOException {
        final byte[] buffer = new byte[65536];
        for (int n = 0; n != -1; n = in.read(buffer, 0, buffer.length)) {
            line.write(buffer, 0, n);
        }
    }
}