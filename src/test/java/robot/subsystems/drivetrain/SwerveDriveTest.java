package robot.subsystems.drivetrain;

import org.junit.*;

import static robot.Constants.SwerveDrive.*;

public class SwerveDriveTest {

    private SwerveDrive swerveDrive;
    private SwerveDrive swerveField;

    private double deviation, forward, strafe, rotation;
    private double[] expectedHeading, expectedHeadingField, expectedVel,
            expectedVelField, robotHeading, robotHeadingField;

    @Before
    public void setup() {
        swerveDrive = new SwerveDrive(false);
        swerveField = new SwerveDrive(true);
    }

    @Test
    public void runTests() {

        deviation = 0.01;

        turnInPlace();

        driveForward();

        calculateLockAngles();
    }

    @Test
    public void turnInPlace() {
        forward = 0;
        strafe = 0;
        rotation = -1;

        expectedHeading = new double[]{0, 0, rotation * ROTATION_MULTIPLIER};
        expectedHeadingField = new double[]{0, 0, rotation * ROTATION_MULTIPLIER};

        expectedVel = new double[]{-0.7, 0.7, -0.7, -0.7, 0.7, -0.7, 0.7, 0.7};
        expectedVelField = new double[]{0, 0, 0, 0, 0, 0, 0, 0};

        getRobotHeading();
        getRobotHeadingField();
        calculateWheelVelocities();
    }

    @Test
    public void driveForward() {
        forward = 1;
        strafe = 0;
        rotation = 0;
        double gyro = 0.5;

        expectedHeading = new double[]{forward * SPEED_MULTIPLIER, 0, 0};
        expectedHeadingField = new double[]{forward * SPEED_MULTIPLIER * Math.cos(gyro), 0, 0};

        expectedVel = new double[]{0, forward * SPEED_MULTIPLIER, 0, forward * SPEED_MULTIPLIER, 0, forward * SPEED_MULTIPLIER, 0, forward * SPEED_MULTIPLIER};
        expectedVelField = new double[]{0, 0, 0, 0, 0, 0, 0, 0};

        getRobotHeading();
        getRobotHeadingField();
        calculateWheelVelocities();
    }

    @Test
    public void getRobotHeading() {
        robotHeading = swerveDrive.getRobotHeading(forward, strafe, rotation);
        Assert.assertArrayEquals(expectedHeading, robotHeading, deviation);
    }

    @Test
    public void getRobotHeadingField() {
        robotHeadingField = swerveField.getRobotHeading(forward, strafe, rotation);
        Assert.assertArrayEquals(expectedHeadingField, robotHeadingField, deviation);
    }
    
    @Test
    public void calculateWheelVelocities() {
        double[] wheelVelocities = swerveDrive.calculateWheelVelocities(robotHeading);
        double[] wheelVelField = swerveField.calculateWheelVelocities(robotHeadingField);

        Assert.assertArrayEquals(expectedVel, wheelVelocities, deviation);
        Assert.assertArrayEquals(expectedVelField, wheelVelField, deviation);
    }

    @Test
    public void calculateLockAngles() {
        double[] expected = new double[]{Math.PI / 4, 3 * Math.PI / 4, 5 * Math.PI / 4, 7 * Math.PI / 4};
        Assert.assertArrayEquals(expected, swerveDrive.calculateLockAngles(), deviation);
    }

    @Test
    public void check() {
        Assert.assertArrayEquals(new double[]{1, 0, 1}, new double[]{1, 0, 1}, 0.1);
    }
}