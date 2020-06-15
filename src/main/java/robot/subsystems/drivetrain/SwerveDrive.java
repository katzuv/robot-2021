package robot.subsystems.drivetrain;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI;
import robot.Utils;

import static robot.Constants.SwerveDrive.*;
import static robot.Ports.SwerveDrive.*;

public class SwerveDrive {

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

    public double[] calculateWheelVelocities(double forward, double strafe, double rotation) {
        double[] robotHeading = {forward, strafe, rotation};

        double r = Math.sqrt(Math.pow(ROBOT_WIDTH/2, 2) + Math.pow(ROBOT_LENGTH/2, 2));

        double[] signX = {1, 1, -1, -1};
        double[] signY = {-1, 1, 1, -1};

        double[][] M = new double[8][3];

        for (int i = 0; i < 8; i++) {
            if (i % 2 != 0) {
                M[i][0] = 1;
                M[i][1] = 0;
                M[i][2] = r * signX[i/2];
            } else {
                M[i][0] = 0;
                M[i][1] = 1;
                M[i][2] = r * signY[i/2];
            }
        }

        double[] wheelVelocities = Utils.matrixVectorMult(M, robotHeading);

        return wheelVelocities;

    }


    public void holonomicDrive(double forward, double strafe, double rotation) {
        forward *= SPEED_MULTIPLIER;
        strafe *= SPEED_MULTIPLIER;
        rotation *= ROTATION_MULTIPLIER;

        if (isFieldOriented) {
            double robotAngle = gyro.getAngle();
            double tmp = forward * Math.cos(robotAngle) + strafe * Math.sin(robotAngle);
            strafe = (-1) * forward * Math.sin(robotAngle) + strafe * Math.cos(robotAngle);
            forward = tmp;
        }

        double[] velocities = calculateWheelVelocities(forward, strafe, rotation);
        double[] polar;
        double[][] controls = new double[4][2];

        for (int i = 0; i < velocities.length; i += 2) {
            polar = Utils.cartesianToPolar(velocities[i], velocities[i + 1]);
            controls[i/2][0] = polar[0];
            controls[i/2][1] = polar[1];
        }

        for (int k = 0; k < controls.length; k++) {
            swerveModules[k].setSpeed(controls[k][0]);
            swerveModules[k].setTargetAngle(controls[k][1]);
        }
    }

    public void stop() {
        for (int i = 0; i < swerveModules.length; i++) {
            swerveModules[i].setSpeed(0);
            swerveModules[i].stopAngleMotor();
        }

    }

}
