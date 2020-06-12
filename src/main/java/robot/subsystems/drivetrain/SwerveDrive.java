package robot.subsystems.drivetrain;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class SwerveDrive {

    private final SwerveModule wheelFrontRight;
    private final SwerveModule wheelFrontLeft;
    private final SwerveModule wheelBackRight;
    private final SwerveModule wheelBackLeft;
    private double forward;
    private double strafe;
    private double angle;

    public SwerveDrive(double forward, double strafe, double angle) {
        wheelFrontRight = new SwerveModule(new TalonSRX(10), new TalonSRX(10));
        wheelFrontLeft = new SwerveModule(new TalonSRX(10), new TalonSRX(10));
        wheelBackRight = new SwerveModule(new TalonSRX(10), new TalonSRX(10));
        wheelBackLeft = new SwerveModule(new TalonSRX(10), new TalonSRX(10));

        this.forward = forward;
        this.strafe = strafe;
        this.angle = angle;
    }





}
