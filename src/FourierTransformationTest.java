import javafx.application.Application;
import org.junit.jupiter.api.Test;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;


class FourierTransformationTest {
    File audioFile;
    AudioInputStream ais;
    AudioFormat fmt;
    byte[] bytes;
    float[] samples;

    @Test
    void ftTest() {
        int freqLowL = 0;
        int freqHighL = 800;
        int freqStep = 1;
        FourierTransformation ft = new FourierTransformation(samples, freqLowL, freqHighL,freqStep);

       Chart.data = ft.fourierKoeffizients;
        Chart.startFreq = freqLowL;
        Chart.step = freqStep;


        System.out.println(samples.length);

        Application.launch(Chart.class, new String[]{});

    }
    @org.junit.jupiter.api.BeforeEach
    void setUp() throws IOException, UnsupportedAudioFileException{
        audioFile  = new File("/home/laurenz/IdeaProjects/FourierTransformation/Sound/rec220sin440.wav");
        ais =  AudioSystem.getAudioInputStream(audioFile);
        fmt = ais.getFormat();
        bytes = new byte[AudioSystem.getAudioFileFormat(audioFile).getByteLength()];
        samples = new float[AudioSystem.getAudioFileFormat(audioFile).getByteLength()];
        for (int blen = 0; (blen = ais.read(bytes)) > -1;) {
            int slen;
            slen = SimpleAudioConversion.unpack(bytes, samples, blen, fmt);

        }
    }

}