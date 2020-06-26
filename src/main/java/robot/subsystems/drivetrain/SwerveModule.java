package robot.subsystems.drivetrain;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;


import static robot.Constants.Drivetrain.*;
import static robot.Constants.SwerveModule.*;
import static robot.Constants.TALON_TIMEOUT;

public class SwerveModule extends SubsystemBase {
    private final TalonSRX angleMotor;
    private final TalonSRX driveMotor;
    private UnitModel unitDrive = new UnitModel(TICKS_PER_METER);
    private UnitModel unitAngle = new UnitModel(TICKS_PER_RAD);

    public SwerveModule(int wheel, TalonSRX driveMotor, TalonSRX angleMotor) {
        // configure feedback sensors
        angleMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, wheel, TALON_TIMEOUT);
        driveMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, wheel, TALON_TIMEOUT);

        // set inversions
        angleMotor.setInverted(false);
        driveMotor.setInverted(false);

        angleMotor.setSensorPhase(false);
        driveMotor.setSensorPhase(false);

        // Set amperage limits
        angleMotor.configContinuousCurrentLimit(MAX_CURRENT);
        angleMotor.enableCurrentLimit(true);

        driveMotor.configContinuousCurrentLimit(MAX_CURRENT);
        driveMotor.enableCurrentLimit(true);

        // set PIDF
        angleMotor.config_kP(wheel, KP, TALON_TIMEOUT);
        angleMotor.config_kI(wheel, KI, TALON_TIMEOUT);
        angleMotor.config_kD(wheel, KD, TALON_TIMEOUT);
        angleMotor.config_kF(wheel, KF, TALON_TIMEOUT);


        this.driveMotor = driveMotor;
        this.angleMotor = angleMotor;
    }

    /**
     * @return the speed of the wheel in [m/s]
     */
    public double getSpeed() {
        return unitDrive.toVelocity(driveMotor.getSelectedSensorVelocity());
    }

    /**
     * @return the angle of the wheel in radians
     */
    public double getAngle() {
        return unitAngle.toUnits(angleMotor.getSelectedSensorPosition());
    }

    /**
     * sets the speed of the the wheel in ticks per 100ms
     * @param speed the speed of the wheel in [m/s]
     */
    public void setSpeed(double speed) {
        driveMotor.set(ControlMode.PercentOutput, unitDrive.toTicks100ms(speed));
    }

    /**
     * sets the angle of the wheel, in consideration of the shortest path to the target angle
     * @param angle the target angle in radians
     */
    public void setAngle(double angle) {
        double targetAngle = setTargetAngle(angle);

        angleMotor.set(ControlMode.Position, unitAngle.toTicks(targetAngle));
    }

    public double setTargetAngle(double angle) {
        // makes sure the value is between -pi and pi
        angle %= Math.PI;
        double[] angles = {angle - Math.PI, angle, angle + Math.PI}; // An array of all possible target angles
        double currentAngle = getAngle();
        double targetAngle = currentAngle;
        double shortestDistance = Double.MAX_VALUE;
        for (double target : angles) { // for each possible angle
            if (Math.abs(target - currentAngle) < shortestDistance) // if the calculated distance is less than the current shortest distance
            {
                shortestDistance = Math.abs(target - currentAngle);
                targetAngle = target;
            }
        }

        return targetAngle;
    }

    /**
     * stops the angle motor
     */
    public void stopAngleMotor() {
        angleMotor.set(ControlMode.PercentOutput, 0);
    }


}
