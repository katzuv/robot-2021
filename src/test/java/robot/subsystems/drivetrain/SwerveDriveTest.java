package robot.subsystems.drivetrain;

import org.junit.*;

import static robot.Constants.SwerveDrive.*;

public class SwerveDriveTest {

    private SwerveDrive swerveDrive;
    private SwerveDrive swerveField;

    private double forward, strafe, rotation, gyro;
    private double deviation = 0.01;
    private double[] expectedHeading, expectedHeadingField, expectedVel,
            expectedVelField, robotHeading, robotHeadingField;

    @Before
    public void setup() {
        swerveDrive = new SwerveDrive(false);
        swerveField = new SwerveDrive(true);
    }

    @Test
    public void runTests() {

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

        expectedVel = new double[]{-Math.PI / 2, Math.PI / 2, -Math.PI / 2, -Math.PI / 2, Math.PI / 2, -Math.PI / 2, Math.PI / 2, Math.PI / 2};

        getRobotHeading();
        getRobotHeadingField();
        calculateWheelVelocities();
    }

    @Test
    public void driveForward() {
        forward = 1;
        strafe = 0;
        rotation = 0;
        gyro =  3 * Math.PI / 2;

        expectedHeading = new double[]{forward * SPEED_MULTIPLIER, 0, 0};
        expectedHeadingField = new double[]{0, -0.7, 0};

        expectedVel = new double[]{0, forward * SPEED_MULTIPLIER, 0, forward * SPEED_MULTIPLIER, 0, forward * SPEED_MULTIPLIER, 0, forward * SPEED_MULTIPLIER};
        expectedVelField = new double[]{-0.7, 0, -0.7, 0, -0.7, 0, -0.7, 0};

        getRobotHeading();
        getRobotHeadingField();
        calculateWheelVelocities();
        calculateWheelVelocitiesField();
    }

    @Test
    public void getRobotHeading() {
        robotHeading = swerveDrive.getRobotHeading(forward, strafe, rotation, gyro);
        for (int i = 0; i < 3; i++)
            System.out.print(robotHeading[i] + " ");
        System.out.println();

        Assert.assertArrayEquals(expectedHeading, robotHeading, deviation);
    }

    @Test
    public void getRobotHeadingField() {
        robotHeadingField = swerveField.getRobotHeading(forward, strafe, rotation, gyro);
        for (int i = 0; i < 3; i++)
            System.out.print(robotHeadingField[i] + " ");
        System.out.println();
        Assert.assertArrayEquals(expectedHeadingField, robotHeadingField, deviation);
    }
    
    @Test
    public void calculateWheelVelocities() {
        double[] wheelVelocities = swerveDrive.calculateWheelVelocities(robotHeading);

        for(int i = 0; i < 8; i++) {
            System.out.print(wheelVelocities[i] + " ");
        }

        System.out.println();

        Assert.assertArrayEquals(expectedVel, wheelVelocities, deviation);
    }

    @Test
    public void calculateWheelVelocitiesField() {
        double[] wheelVelField = swerveField.calculateWheelVelocities(robotHeadingField);
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