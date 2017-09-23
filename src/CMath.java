public class CMath {
    public static double cexp(double exponent) {
        double degree = exponent * (360 / (2 * Math.PI));
        return Math.cos(degree);
    }
    public static double ciexp(double exponent) {
        double degree = exponent * (360 / (2 * Math.PI));
        return Math.sin(degree);
    }
}
