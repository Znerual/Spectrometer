import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class FourierTransformation {
    public TreeMap<Integer, Float> fourierKoeffizients;
    public int maxFreq;
    private float maxFreqValue = 0;
    public FourierTransformation(float[] dataPoints, int freqLowLimit, int freqHighLimit, int freqStep) {
        int totalDataPoints = dataPoints.length;
        fourierKoeffizients = new TreeMap<>();
        for (int k = freqLowLimit; k <= freqHighLimit; k+= freqStep) {
            float fk = 0;
            for (int n = 0; n < totalDataPoints; n++) {
                fk += dataPoints[n] * CMath.cexp((-2 * Math.PI * n * k)/ totalDataPoints);
            }
            if (maxFreqValue < fk) {
                maxFreq = k;
                maxFreqValue = fk;
            }
            fourierKoeffizients.put(k, fk);
        }
    }
    public FourierTransformation(int[] dataPoints, int freqLowLimit, int freqHighLimit, int freqStep) {
        int totalDataPoints = dataPoints.length;
        fourierKoeffizients = new TreeMap<>();
        for (int k = freqLowLimit; k <= freqHighLimit; k+= freqStep) {
            float fk = 0;
            for (int n = 0; n < totalDataPoints; n++) {
                fk += dataPoints[n] * CMath.cexp((-2 * Math.PI * n * k)/ totalDataPoints);
            }
            if (maxFreqValue < fk && k != 0) {
                maxFreq = k;
                maxFreqValue = fk;
            }
            fourierKoeffizients.put(k, fk);
        }
    }
}
