package robot;

public class Utils {

    /**
     * recreates the results of Math.floorMod() for Double type variables.
     * The result is the unsigned remainder of the mod method.
     *
     * @param value the numerator
     * @param mod   the denominator
     * @return the remainder of the division
     */
    public static double floorMod(double value, double mod) {
        value %= mod;
        value += mod;
        value %= mod;
        return value;
    }

    public static double[] cartesianToPolar(double x, double y) {
        double[] polar = new double[2];
        polar[0] = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
        polar[1] = Math.toDegrees(Math.atan2(y, polar[0]));

        return polar;
    }

    public static double[] matrixVectorMult(double[][] m, double[] v) {
        int sum;
        double[] out = new double[m.length];

        for (int i = 0; i < m.length; i++) {
            sum = 0;
            for (int j = 0; j < v.length; j++) {
                sum += m[i][j] * v[j];
            }
            out[i] = sum;
        }

        return out;
    }

}
