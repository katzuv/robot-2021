package robot.subsystems.drivetrain;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import robot.Utils;

import static robot.Constants.SwerveModule.*;
import static robot.Constants.TALON_TIMEOUT;

public class SwerveModule {
    private final TalonSRX driveMotor;
    private final TalonSRX angleMotor;
    private UnitModel unit = new UnitModel(TICKS_PER_METER);

    public SwerveModule(TalonSRX driveMotor, TalonSRX angleMotor) {
        // configure feedback sensors
        angleMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, TALON_TIMEOUT);
        driveMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, 0, TALON_TIMEOUT);

        // Set amperage limits
        angleMotor.configContinuousCurrentLimit(50);
        angleMotor.enableCurrentLimit(true);

        driveMotor.configContinuousCurrentLimit(50);
        driveMotor.enableCurrentLimit(true);

        // set PIDF
        angleMotor.config_kP(0, KP, TALON_TIMEOUT);
        angleMotor.config_kI(0, KI, TALON_TIMEOUT);
        angleMotor.config_kD(0, KD, TALON_TIMEOUT);
        angleMotor.config_kF(0, KF, TALON_TIMEOUT);


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
