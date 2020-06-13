package robot.subsystems.drivetrain;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import robot.Utils;

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

    public double getSpeed() {
        return unit.toVelocity(driveMotor.getSelectedSensorVelocity());
    }

    public double getAngle() {
        return unit.toUnits(angleMotor.getSelectedSensorPosition());
    }

    public void setSpeed(double speed) {
        driveMotor.set(ControlMode.PercentOutput, speed);
    }

    public void setTargetAngle(double angle) {
        angle = Utils.floorMod(angle, 360);
        double[] positions = {angle - 360, angle, angle + 360}; // An array of all possible target positions
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

        angleMotor.set(ControlMode.Position, targetPosition);
    }

    public void stopAngleMotor() {
        angleMotor.set(ControlMode.PercentOutput, 0);
    }


}
