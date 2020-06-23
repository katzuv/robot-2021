package robot.subsystems.drivetrain;

import org.junit.Assert;
import org.junit.Test;

import static robot.Constants.SwerveDrive.*;

public class SwerveDriveTest {

    private SwerveDrive swerveDrive = new SwerveDrive(false);
    private SwerveDrive swerveField = new SwerveDrive(true);

    @Test
    public void runTests() {

        double deviation = 0.01;

        turnInPlace(-0.7, deviation);

        driveForward(0.5, Math.PI, deviation);

        calculateLockAngles(new double[]{45, 135, 225, 315}, deviation);
    }

    public void turnInPlace(double rotation, double deviation) {
        double[] expectedHeading = {0, 0, rotation * ROTATION_MULTIPLIER};
        double[] expectedHeadingField = {0, 0, rotation * ROTATION_MULTIPLIER};

        double[] expectedVel = {};
        double[] expectedVelField = {};

        double[] robotHeading = getRobotHeading(0, 0, rotation, expectedHeading, deviation);
        double[] robotHeadingField = getRobotHeadingField(0, 0, rotation, expectedHeadingField, deviation);
        calculateWheelVelocities(robotHeading, robotHeadingField, expectedVel, expectedVelField, deviation);
    }

    public void driveForward(double forward, double gyro, double deviation) {
        double[] expectedHeading = {forward * SPEED_MULTIPLIER, 0, 0};
        double[] expectedHeadingField = {forward * SPEED_MULTIPLIER * Math.cos(gyro), 0, 0};

        double[] expectedVel = {};
        double[] expectedVelField = {};

        double[] robotHeading = getRobotHeading(forward, 0, 0, expectedHeading, deviation);
        double[] robotHeadingField = getRobotHeadingField(forward, 0, 0, expectedHeadingField, deviation);
        calculateWheelVelocities(robotHeading, robotHeadingField, expectedVel, expectedVelField, deviation);
    }

    @Test
    public double[] getRobotHeading(double fwd, double str, double rtc, double[] expected, double deviation) {
        double[] robotHeading = swerveDrive.getRobotHeading(fwd, str, rtc);
        Assert.assertArrayEquals(robotHeading, expected, deviation);
        return robotHeading;
    }

    @Test
    public double[] getRobotHeadingField(double fwd, double str, double rtc, double[] expectedField, double deviation) {
        double[] robotHeadingField = swerveField.getRobotHeading(fwd, str, rtc);
        Assert.assertArrayEquals(robotHeadingField, expectedField, deviation);
        return robotHeadingField;
    }
    
    @Test
    public void calculateWheelVelocities(double[] robotHeading, double[] robotHeadingField, double[] expected, double[] expectedField, double deviation) {
        double[] wheelVelocities = swerveDrive.calculateWheelVelocities(robotHeading);
        double[] wheelVelField = swerveField.calculateWheelVelocities(robotHeadingField);

        Assert.assertArrayEquals(wheelVelocities, expected, deviation);
        Assert.assertArrayEquals(wheelVelField, expectedField, deviation);
    }

    @Test
    public void calculateLockAngles(double[] expected, double deviation) {
        Assert.assertArrayEquals(swerveDrive.calculateLockAngles(), expected, deviation);
    }
}