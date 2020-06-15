package robot.subsystems.drivetrain;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

import static robot.Constants.Drivetrain.TICKS_PER_METER;
import static robot.Constants.SwerveModule.*;
import static robot.Constants.TALON_TIMEOUT;

public class SwerveModule {
    private final TalonSRX angleMotor;
    private final TalonSRX driveMotor;
    private UnitModel unit = new UnitModel(TICKS_PER_METER);

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
        angleMotor.configContinuousCurrentLimit(50);
        angleMotor.enableCurrentLimit(true);

        driveMotor.configContinuousCurrentLimit(50);
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
        return unit.toVelocity(driveMotor.getSelectedSensorVelocity());
    }

    /**
     * @return the angle of the wheel in radians
     */
    public double getAngle() {
        return unit.toUnits(angleMotor.getSelectedSensorPosition());
    }

    /**
     * sets the speed of the the wheel in ticks per 100ms
     * @param speed the speed of the wheel in [m/s]
     */
    public void setSpeed(double speed) {
        driveMotor.set(ControlMode.PercentOutput, unit.toTicks100ms(speed));
    }

    /**
     * sets the angle of the wheel, in consideration of the shortest path to the target angle
     * @param angle the target angle in radians
     */
    public void setTargetAngle(double angle) {
        angle %= 2 * Math.PI;
        double[] positions = {angle - 2 * Math.PI, angle, angle + 2 * Math.PI}; // An array of all possible target angles
        double currentPosition = getAngle();
        double targetPosition = currentPosition;
        double shortestDistance = Double.MAX_VALUE;
        for (double targetPos : positions) { // for each possible position
            if (Math.abs(targetPos - currentPosition) < shortestDistance) // if the calculated distance is less than the current shortest distance
            {
                shortestDistance = Math.abs(targetPos - currentPosition);
                targetPosition = targetPos;
            }
        }

        angleMotor.set(ControlMode.Position, unit.toTicks(targetPosition));
    }

    /**
     * stops the angle motor
     */
    public void stopAngleMotor() {
        angleMotor.set(ControlMode.PercentOutput, 0);
    }


}
