package frc.robot;

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

    /**
     * converts cartesian coordinates to polar coordinates
     * @param x the X cartesian coordinate
     * @param y the Y cartesian coordinate
     * @return a vector of length 2 with the polar coordinates [length, angle]
     */
    public static double[] cartesianToPolar(double x, double y) {
        double[] polar = new double[2];
        polar[0] = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
        polar[1] = Math.atan2(x, y);

        return polar;
    }

    /**
     * calculates the matrix - vector multiplication
     * assuming that the number of columns in the matrix correspond to the number of rows in the vector
     * @param m a matrix of size R * C
     * @param v a vector of size C
     * @return a vector of length R with the corresponding matrix multiplication
     */
    public static double[] matrixVectorMult(double[][] m, double[] v) {
        double sum;
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
