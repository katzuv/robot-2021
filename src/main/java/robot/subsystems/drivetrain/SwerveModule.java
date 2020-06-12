package robot.subsystems.drivetrain;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public class SwerveModule {
    private final TalonSRX driveMotor;
    private final TalonSRX angleMotor;

    public SwerveModule(TalonSRX driveMotor, TalonSRX angleMotor) {
        // configure feedback sensors
        angleMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);
        driveMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, 10);

        // Set amperage limits
        angleMotor.configContinuousCurrentLimit(50);
        angleMotor.enableCurrentLimit(true);

        driveMotor.configContinuousCurrentLimit(50);
        driveMotor.enableCurrentLimit(true);

        this.driveMotor = driveMotor;
        this.angleMotor = angleMotor;
    }


}
