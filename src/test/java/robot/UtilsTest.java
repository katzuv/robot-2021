package robot;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UtilsTest {

    @Test
    public void cartesianToPolar() {
        double[] polar = Utils.cartesianToPolar(3, 1);
        double[] expected = {Math.sqrt(10), Math.PI / 2.5};
        Assert.assertArrayEquals(expected, polar, 0.01);
    }

    @Test
    public void matrixVectorMult() {
        double[][] mat = { {1, 0, 1}, {0, 1, 0}, {1, 0, 1} };
        double[] v = {3, 3, 3};
        double[] vec = Utils.matrixVectorMult(mat, v);
        double[] expected = {6, 3, 6};

        Assert.assertArrayEquals(expected, vec, 0.01);
    }
}