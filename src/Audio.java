import com.sun.media.sound.FFT;
import javafx.application.Application;
import org.jtransforms.fft.FloatFFT_1D;

import static java.util.Comparator.comparing;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import static java.lang.Math.*;
import static java.lang.Math.log;

public class Audio {
    public static void processAudio(File audioFile) {
        int totalFramesRead = 0;
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioFile);
            AudioFormat format = audioInputStream.getFormat();
            int bytesPerFrame = format.getFrameSize();
            AudioFormat.Encoding encoding = format.getEncoding();
            int   bitsPerSample = format.getSampleSizeInBits();
            double fullScale = fullScale(bitsPerSample);
            if (bytesPerFrame == AudioSystem.NOT_SPECIFIED) {
                bytesPerFrame = 1;
            }
            int numBytes = 1024 * bytesPerFrame;
            byte[] audioByte = new byte[numBytes];

            int numBytesRead = 0;
            int numFramesRead = 0;
            while ((numBytesRead = audioInputStream.read(audioByte)) != -1) {
                numFramesRead = numBytesRead / bytesPerFrame;
                totalFramesRead += numFramesRead;
                float[] audioFloat;
                if (bytesPerFrame == 2) {
                    audioFloat = new float[audioByte.length / 2];
                    for (int i = 0; i < audioByte.length / 2; i++) { // read in the samples
                        int lsb = audioByte[i * 2] & 0xFF; // "least significant byte"
                        int msb = audioByte[i * 2 + 1] & 0xFF; // "most significant byte"
                        int num;
                        if (audioInputStream.getFormat().isBigEndian()) {
                            num = (lsb << 8) | msb;
                        } else {
                            num = (msb << 8) | lsb;
                        }
                        //System.out.println(num);
                        audioFloat[i] = getEncoded(num, encoding,fullScale, bitsPerSample );
                        //System.out.println(audioFloat[i]);
                    }
                } else {
                    audioFloat = new float[audioByte.length];
                    for (int i = 0; i < audioByte.length; i++) {
                        audioFloat[i] = audioFloat[i] = getEncoded(audioByte[i], encoding,fullScale, bitsPerSample );
                    }
                }
                //System.out.println(totalFramesRead);
                /*
                FourierTransformation ft = new FourierTransformation(audioFloat, 50, audioFloat.length / 2,1);
                for(Map.Entry<Integer,Float> entry : ft.fourierKoeffizients.entrySet()) {
                    //Integer key = entry.getKey();
                    Float value = entry.getValue();

                    System.out.println(value);
                }
                */
                FloatFFT_1D fftdo = new FloatFFT_1D(audioFloat.length);
                float[] fft = new float[audioFloat.length * 2];
                System.arraycopy(audioFloat, 0, fft, 0,audioFloat.length);
                fftdo.realForwardFull(fft);
                for (float v : fft) {
                    System.out.println(v);
                }


               // System.out.println(ft.maxFreq);
                /*
                Chart.data = ft.fourierKoeffizients;
                Chart.startFreq = 0;
                Chart.endFreq =audioFloat.length;
                Chart.step = 1;
                Application.launch(Chart.class, new String[]{});
*/

            }
           /* lChart.data = audioByte;
            Application.launch(lChart.class, new String[]{});
            */


//            System.out.println("Tf: " + totalFramesRead + " TB: " + numBytesRead);
        } catch (IOException | UnsupportedAudioFileException exc) {

        }

    }
    private static float getEncoded(int tmp, AudioFormat.Encoding encoding, double fullScale, int bitsPerSample) {
        long temp;
        if (encoding == AudioFormat.Encoding.PCM_SIGNED) {
            temp = extendSign(tmp, bitsPerSample);
            return (float) (temp / fullScale);

        } else if (encoding == AudioFormat.Encoding.PCM_UNSIGNED) {
            temp = signUnsigned(tmp, bitsPerSample);
            return  (float) (temp / fullScale);

        } else if (encoding == AudioFormat.Encoding.PCM_FLOAT) {
            if (bitsPerSample == 32) {
                return Float.intBitsToFloat((int) tmp);
            } else if (bitsPerSample == 64) {
                return (float) Double.longBitsToDouble(tmp);
            }
        } else if (encoding == AudioFormat.Encoding.ULAW) {
            return bitsToMuLaw(tmp);

        } else if (encoding == AudioFormat.Encoding.ALAW) {
            return bitsToALaw(tmp);
        }
        return (float) (tmp / fullScale);
    }
    public static double fullScale(int bitsPerSample) {
        return pow(2.0, bitsPerSample - 1);
    }


    private static long signUnsigned(long temp, int bitsPerSample) {
        return temp - (long) fullScale(bitsPerSample);
    }


    // mu-law constant
    private static final double MU = 255.0;
    // A-law constant
    private static final double A = 87.7;
    // reciprocal of A
    private static final double RE_A = 1.0 / A;
    // natural logarithm of A
    private static final double LN_A = log(A);
    // if values are below this, the A-law exponent is 0
    private static final double EXP_0 = 1.0 / (1.0 + LN_A);

    private static float bitsToMuLaw(long temp) {
        temp ^= 0xffL;
        if ((temp & 0x80L) == 0x80L) {
            temp = -(temp ^ 0x80L);
        }

        float sample = (float) (temp / fullScale(8));

        return (float) (
                signum(sample)
                        *
                        (1.0 / MU)
                        *
                        (pow(1.0 + MU, abs(sample)) - 1.0)
        );
    }


    private static float bitsToALaw(long temp) {
        temp ^= 0x55L;
        if ((temp & 0x80L) == 0x80L) {
            temp = -(temp ^ 0x80L);
        }

        float sample = (float) (temp / fullScale(8));

        float sign = signum(sample);
        sample = abs(sample);

        if (sample < EXP_0) {
            sample = (float) (sample * ((1.0 + LN_A) / A));
        } else {
            sample = (float) (exp((sample * (1.0 + LN_A)) - 1.0) / A);
        }

        return sign * sample;
    }
    private static long extendSign(long temp, int bitsPerSample) {
        int extensionBits = 64 - bitsPerSample;
        return (temp << extensionBits) >> extensionBits;
    }

    public static void main(String[] args) {
        processAudio(new File("Sound/sawtooth440.wav"));
    }
}
