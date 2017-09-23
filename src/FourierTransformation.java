import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class FourierTransformation {
    public TreeMap<Integer, Float> fourierKoeffizients;
    public int maxFreq;
   // public float maxFreqF;
    private float maxFreqValue = 0;

    public FourierTransformation(float[] dataPoints, int freqLowLimit, int freqHighLimit, int freqStep, float samplingFrequencyRate) {
        int totalDataPoints = dataPoints.length;
        fourierKoeffizients = new TreeMap<>();
        for (int k = freqLowLimit; k <= freqHighLimit; k+= freqStep) {
            double fk = 0;
            for (int n = 0; n < totalDataPoints; n++) {
                fk += dataPoints[n] * CMath.cexpopt(360 * (-k * n ));
            }
            fk /= totalDataPoints;
            if (maxFreqValue < fk) {
                maxFreq = k;
                maxFreqValue = (float) fk;
            }
            fourierKoeffizients.put(k , (float)fk );
        }
    }

    /*
    public FourierTransformation(float[] dataPoints, int freqLowLimit, int freqSegments,  double stepToFrequ, float samplingFrequ) {
        int totalDataPoints = dataPoints.length;
        fourierKoeffizients = new TreeMap<>();
        double stepSize = getStep(stepToFrequ,(samplingFrequ / 2 -  freqLowLimit)/ freqSegments);
        for (double k = getStep(stepToFrequ, freqLowLimit); k <= getStep(stepToFrequ, samplingFrequ / 2); k+= stepSize) {
            double fk = 0;
            for (int n = 0; n < totalDataPoints; n++) {
                fk += dataPoints[n] * CMath.cexpopt(360 * -k * n );
            }
            fk /= totalDataPoints;
            if (maxFreqValue < fk) {
                maxFreq =(int) getFrequency(stepToFrequ, k);
                maxFreqValue = (float) fk;
            }
            fourierKoeffizients.put((int) (getFrequency(stepToFrequ, k)) , (float)fk );
        }
    }
    */
    private float getFrequency(double stepToFrequ, double step) {
        return (float)(step * stepToFrequ);
    }
    private double getStep(double stepToFrequ, float frequency) {
        return (frequency / stepToFrequ);
    }
    public FourierTransformation(int[] dataPoints, int freqLowLimit, int freqHighLimit, int freqStep) {
        int totalDataPoints = dataPoints.length;
        fourierKoeffizients = new TreeMap<>();
        for (int k = freqLowLimit; k <= freqHighLimit; k+= freqStep) {
            float fk = 0;
            for (int n = 0; n < totalDataPoints; n++) {
                fk += dataPoints[n] * CMath.cexp((-2 * Math.PI * n * k));
            }
            fk /= totalDataPoints;
            if (maxFreqValue < fk && k != 0) {
                maxFreq = k;
                maxFreqValue = fk;
            }
            fourierKoeffizients.put(k, fk);
        }
    }
}
