package robot.subsystems.drivetrain;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.SPI;
import robot.Utils;

import static robot.Constants.SwerveDrive.*;

public class SwerveDrive {

    private AHRS gyro = new AHRS(SPI.Port.kMXP);
    private SwerveModule[] swerveModules = new SwerveModule[4];
    private double forward;
    private double strafe;
    private double angle;

    public SwerveDrive(double forward, double strafe, double angle) {
        gyro.reset();

        swerveModules[0] = new SwerveModule(new TalonSRX(10), new TalonSRX(10));
        swerveModules[1] = new SwerveModule(new TalonSRX(10), new TalonSRX(10));
        swerveModules[2] = new SwerveModule(new TalonSRX(10), new TalonSRX(10));
        swerveModules[3] = new SwerveModule(new TalonSRX(10), new TalonSRX(10));

        this.forward = forward;
        this.strafe = strafe;
        this.angle = angle;
    }

    public double[] calculateWheelVelocities() {
        double[] robotHeading = {forward, strafe, angle};

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

        double[] wheelVelocities = matrixVectorMult(M, robotHeading);

        return wheelVelocities;

    }

    public double[] matrixVectorMult(double[][] m, double[] v) {
        int sum;
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
