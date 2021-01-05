package frc.robot.utils;

import java.io.*;
import java.util.Map;
import java.util.TreeMap;

/**
 * The moving average contains a list of velocity based on the distance, and should give an approximation of the velocity to apply.
 * In order to the moving average object to approximate better, you need to pass more values.
 * The object calculate calculate a straight line between the closest point, and retrieve the velocity (in the straight line between the records).
 */
public class MovingAverage {
    private final String DELIMITER = ",";
    private final Map<Double, Double> distanceVelocityMap; // <key - distance, value - velocity>, tree map is automatically sort the values by distance.

    public MovingAverage(String pathToCsv) {
        this(new File(pathToCsv));
    }

    public MovingAverage(File csv) {
        this.distanceVelocityMap = new TreeMap<>();
        if (csv.exists() && csv.isFile()) {
            try {
                read(new FileReader(csv));
            } catch (FileNotFoundException e) { // Can't happen, we have already checked it
                e.printStackTrace();
            }
        } else
            System.err.println("File not found: " + csv.getName());
    }

    public MovingAverage(Map<Double, Double> map) {
        this.distanceVelocityMap = new TreeMap<>(map); // just because I want to order the values by the key.
    }

    public MovingAverage(double[][] values) {
        this.distanceVelocityMap = new TreeMap<>();
        for (double[] pair : values) {
            if (pair.length >= 2)
                this.distanceVelocityMap.put(pair[0], pair[1]);
        }
    }

    public MovingAverage(Reader reader) {
        this.distanceVelocityMap = new TreeMap<>();
        read(reader);
    }

    private void read(Reader reader) {
        String line = null;
        try (BufferedReader bufferedReader = new BufferedReader(reader)) {
            while ((line = bufferedReader.readLine()) != null) {
                String[] pair = line.split(DELIMITER);
                distanceVelocityMap.put(Double.parseDouble(pair[0]), Double.parseDouble(pair[1]));
            }
        } catch (IOException e) {
            System.err.println("MovingAverage class can't read the file");
            System.out.println(e.getMessage());
        } catch (IndexOutOfBoundsException e) {
            System.err.println("Please check that every line has a pair of values, the problem occurred on this line " + line);
        }
    }

    /**
     * Retrieve an approximation for the velocity based on the distance.
     *
     * @param distance the distance from the target.
     * @return an approximation of the velocity to apply
     */
    public double estimateVelocity(double distance) {
        double[][] nearest = getClosestDistance(distance);

        if (nearest[0][0] == nearest[1][0]) // we have already have a record of the distance, just return the velocity based of that
            return nearest[0][1];

        // y = mx + b
        // m = delta y / delta x
        double m = (nearest[1][1] - nearest[0][1]) / (nearest[1][0] - nearest[0][0]);
        // b = y - m*x
        double b = nearest[1][1] - m * nearest[1][0];

        return distance * m + b;
    }

    /**
     * get a 2 pairs of (distance, velocity) of the closets distances below and above.
     *
     * @param distance the distance to return the pairs.
     * @return a 2d array represents the closest values(below {distance, velocity}, above {distance, velocity}).
     */
    private double[][] getClosestDistance(double distance) {
        double[][] closest = {{0, 0}, {Double.MAX_VALUE, 0}}; //{min, max}
        distanceVelocityMap.forEach((key, value) -> {
            double currentDistance = key, velocity = value; //we need to do it because comparison of the Double wrapper-class is off
            double difference = currentDistance - distance;
            if (difference == 0) {
                closest[0][0] = currentDistance;
                closest[0][1] = velocity;
                closest[1][0] = currentDistance;
                closest[1][1] = velocity;
                return;
            }

            if (difference < closest[0][0] && difference < 0) { // the value below the desired distance
                closest[0][0] = currentDistance;
                closest[0][1] = velocity;
            } else if (difference < closest[1][0] && difference > 0) {
                closest[1][0] = currentDistance;
                closest[1][1] = velocity;
            }
        });
        return closest;
    }
}
