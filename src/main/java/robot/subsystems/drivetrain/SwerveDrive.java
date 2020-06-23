package robot.subsystems.drivetrain;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import robot.Utils;

import static robot.Constants.SwerveDrive.*;
import static robot.Ports.SwerveDrive.*;

public class SwerveDrive extends SubsystemBase {

    private AHRS gyro = new AHRS(SPI.Port.kMXP);
    private SwerveModule[] swerveModules = new SwerveModule[4];
    private boolean isFieldOriented;

    public SwerveDrive(boolean isFieldOriented) {
        gyro.reset();

        swerveModules[0] = new SwerveModule(0, new TalonSRX(DRIVE_MOTOR_1), new TalonSRX(ANGLE_MOTOR_1));
        swerveModules[1] = new SwerveModule(1, new TalonSRX(DRIVE_MOTOR_2), new TalonSRX(ANGLE_MOTOR_2));
        swerveModules[2] = new SwerveModule(2, new TalonSRX(DRIVE_MOTOR_3), new TalonSRX(ANGLE_MOTOR_3));
        swerveModules[3] = new SwerveModule(3, new TalonSRX(DRIVE_MOTOR_4), new TalonSRX(ANGLE_MOTOR_4));

        this.isFieldOriented = isFieldOriented;
    }


    /**
     * sets the wheels of the robot to the calculated angle and speed
     * @param forward the Y value of the joystick
     * @param strafe the X value of the joystick
     * @param rotation the rotation Z of the joystick
     */
    public void holonomicDrive(double forward, double strafe, double rotation) {

        double[] robotHeading = getRobotHeading(forward, strafe, rotation);

        double[] velocities = calculateWheelVelocities(robotHeading);
        double[] polar;
        double[][] controls = new double[4][2];

        // converts the cartesian velocities to polar and transfers them to a control matrix
        for (int i = 0; i < 4; i ++) {
            polar = Utils.cartesianToPolar(velocities[2*i], velocities[2*i + 1]);
            controls[i][0] = polar[0];
            controls[i][1] = polar[1];
        }

        // feeds the corresponding control to each wheel
        for (int k = 0; k < 4; k++) {
            swerveModules[k].setSpeed(controls[k][0]);
            swerveModules[k].setTargetAngle(controls[k][1]);
        }
    }

    /**
     * turns the joystick inputs into the robot heading
     * @param forward the Y value of the joystick
     * @param strafe the X value of the joystick
     * @param rotation the rotation Z of the joystick
     * @return an array of the robot heading
     */
    public double[] getRobotHeading(double forward, double strafe, double rotation) {
        // turns the joystick values into the heading of the robot
        forward *= SPEED_MULTIPLIER;
        strafe *= SPEED_MULTIPLIER;
        rotation *= ROTATION_MULTIPLIER;

        // if the drive style is field oriented, changes the forward and strafe to be according to the field axises
        // see https://www.chiefdelphi.com/t/paper-4-wheel-independent-drive-independent-steering-swerve/107383
        if (isFieldOriented) {
            double robotAngle = gyro.getAngle();
            double tmp = forward * Math.cos(robotAngle) + strafe * Math.sin(robotAngle);
            strafe = (-1) * forward * Math.sin(robotAngle) + strafe * Math.cos(robotAngle);
            forward = tmp;
        }
        double[] robotHeading = {forward, strafe, rotation};

        return robotHeading;
    }

    /**
     * calculates the velocity vector of each wheel from the following three joystick outputs:
     * forward the heading of the robot in the Y direction
     * strafe the heading of the robot in the X direction
     * rotation the rotation of the robot
     * @return an array of length 8 in which each pair is the X and Y velocities of each wheel
     */
    public double[] calculateWheelVelocities(double[] robotHeading) {

        // calculates the distance from the center of the robot to the wheels
        double Rx = ROBOT_WIDTH/2;
        double Ry = ROBOT_LENGTH/2;

        // the sign vectors of Rx and Ry
        double[] signX = {1, 1, -1, -1};
        double[] signY = {-1, 1, 1, -1};

        // creates an inverse matrix of all the mathematical operations needed to calculate the wheel velocities
        // see https://file.tavsys.net/control/controls-engineering-in-frc.pdf pg.144
        double[][] M = new double[8][3];

        for (int i = 0; i < 8; i++) {
            if (i % 2 == 0) {
                M[i][0] = 1;
                M[i][1] = 0;
                M[i][2] = Ry * signY[i/2];
            } else {
                M[i][0] = 0;
                M[i][1] = 1;
                M[i][2] = Rx * signX[i/2];
            }
        }

        // multiplies M by the robotHeading to obtain the wheel velocities
        double[] wheelVelocities = Utils.matrixVectorMult(M, robotHeading);

        return wheelVelocities;

    }

    /**
     * set the angle of the wheels on the robot to lock the robot in place
     */
    public void lock() {
        // calculates the lock angle of the wheels
        double lockAngle = Math.PI / 2 - Math.tan(ROBOT_WIDTH / ROBOT_LENGTH);

        for (int i = 0; i < 4; i++) {
            swerveModules[i].setSpeed(0);
            swerveModules[i].setTargetAngle(lockAngle + i*Math.PI/2);
        }
    }

    /**
     * stops all the wheels
     */
    public void stop() {
        for (int i = 0; i < swerveModules.length; i++) {
            swerveModules[i].setSpeed(0);
            swerveModules[i].stopAngleMotor();
        }

    }

}
