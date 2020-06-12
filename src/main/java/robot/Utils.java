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
        polar[1] = Math.toDegrees(Math.acos(y/polar[0]));

        return polar;
    }

}
