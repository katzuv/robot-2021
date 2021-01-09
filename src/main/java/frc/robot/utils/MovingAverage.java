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
    private static final String DELIMITER = ",";
    private final Map<Double, Double> distanceVelocityMap; // <key - distance, value - velocity>, java.util.TreeMap iterates according to the "natural ordering" of the keys.

    public MovingAverage(String pathToCsv) {
        this(new File(pathToCsv));
    }

    public MovingAverage(File csv) {
        this.distanceVelocityMap = new TreeMap<>();
        if (csv.isFile()) {
            try {
                read(new FileReader(csv));
            } catch (Exception e) { // Can't happen, we have already checked it
                e.printStackTrace();
            }
        } else
            System.err.println("File not found: " + csv.getName());
        this.distanceVelocityMap.put(0.0, 0.0); // I don't want to use if statement
    }

    public MovingAverage(Map<Double, Double> map) {
        this.distanceVelocityMap = new TreeMap<>(map); // just because I want to order the values by the key.
        this.distanceVelocityMap.put(0.0, 0.0); // I don't want to use if statement
    }

    public MovingAverage(Reader reader) {
        this.distanceVelocityMap = new TreeMap<>();
        read(reader);
        this.distanceVelocityMap.put(0.0, 0.0); // I don't want to use if statement
    }

    private void read(Reader reader) {
        String line = null;
        try (BufferedReader bufferedReader = new BufferedReader(reader)) {
            while ((line = bufferedReader.readLine()) != null) {
                String[] pair = line.split(DELIMITER);
                this.distanceVelocityMap.put(Double.parseDouble(pair[0]), Double.parseDouble(pair[1]));
            }
        } catch (IOException e) {
            System.err.println("MovingAverage class can't read the file");
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
        double[] distances = getClosestDistances(distance);

        if (distances[0] == distances[1]) // There is already a record of the distance, just return the velocity based of that
            return distanceVelocityMap.get(distances[0]);

        double lowerVelocity = distanceVelocityMap.get(distances[0]);
        double higherVelocity = distanceVelocityMap.get(distances[1]);
        // y = mx + b
        // m = delta y / delta x
        double m = (higherVelocity - lowerVelocity) / (distances[1] - distances[0]);
        // b = y - m*x
        double b = higherVelocity - m * distances[1];

        return distance * m + b;
    }

    /**
     * get a pair of distances of the closets distances, one below and one above.
     *
     * @param distance the distance to return the pairs.
     * @return a 2d array represents the closest values(below {distance, velocity}, above {distance, velocity}).
     */
    private double[] getClosestDistances(double distance) {
        double[] closest = {0, Double.MAX_VALUE}; //{min, max}
        this.distanceVelocityMap.forEach((key, value) -> {
            double currentDistance = key, velocity = value; //we need to do it because comparison of the Double wrapper-class is off
            double difference = currentDistance - distance;

            if (difference < closest[0] && difference < 0) { // the value below the desired distance
                closest[0] = currentDistance;
            } else if (difference < closest[1] && difference > 0) {
                closest[1] = currentDistance;
            }
        });
        return closest;
    }
}
