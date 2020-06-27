package robot;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UtilsTest {

    @Test
    public void floorMod() {
        double val = Utils.floorMod(-1.5 * Math.PI, Math.PI);
        double expected = Math.PI / 2;

        Assert.assertEquals(expected, val, 0.01);
    }

    @Test
    public void cartesianToPolar() {
        double[] polar = Utils.cartesianToPolar(3, 1);
        double[] expected = {Math.sqrt(10), Math.PI / 2.5};
        Assert.assertArrayEquals(expected, polar, 0.01);
    }

    @Test
    public void matrixVectorMult() {
        double[][] mat = { {0, 1, 0.5}, {1, 0, -0.5}, {0, 1, 0.5}, {1, 0, .5}, {0, 1, -0.5}, {1, 0, .5}, {0, 1, -0.5}, {1, 0, -0.5} };
        double[] v = {.7, 0, 0};
        double[] vec = Utils.matrixVectorMult(mat, v);
        double[] expected = {0, .7, 0, .7, 0, .7, 0, .7};

        Assert.assertArrayEquals(expected, vec, 0.01);
    }
}