package robot.subsystems.drivetrain;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import robot.Utils;


import static robot.Constants.Drivetrain.*;
import static robot.Constants.SwerveModule.*;
import static robot.Constants.TALON_TIMEOUT;

public class SwerveModule extends SubsystemBase {
    private SupplyCurrentLimitConfiguration currLimitConfig = new SupplyCurrentLimitConfiguration(true, MAX_CURRENT, 0, 0);
    private final TalonFX driveMotor;
    private final TalonSRX angleMotor;

    private UnitModel unitDrive = new UnitModel(TICKS_PER_METER);
    private UnitModel unitAngle = new UnitModel(TICKS_PER_RAD);

    public SwerveModule(int wheel, TalonFX driveMotor, TalonSRX angleMotor) {
        // configure feedback sensors
        driveMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, wheel, TALON_TIMEOUT);
        angleMotor.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, wheel, TALON_TIMEOUT);

        // set inversions
        angleMotor.setInverted(false);
        driveMotor.setInverted(false);

        angleMotor.setSensorPhase(false);
        driveMotor.setSensorPhase(false);

        // Set amperage limits
        driveMotor.configSupplyCurrentLimit(currLimitConfig);

        angleMotor.configContinuousCurrentLimit(MAX_CURRENT);
        angleMotor.enableCurrentLimit(true);

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
        double targetAngle = getTargetAngle(angle, getAngle());

        angleMotor.set(ControlMode.Position, unitAngle.toTicks(targetAngle));
    }

    /**
     * finds the target angle of the wheel based on the shortest distance from the current position
     * @param angle the current target angle
     * @param currentAngle the current angle of the wheel
     * @return the target angle
     */
    public double getTargetAngle(double angle, double currentAngle) {
        // makes sure the value is between -pi and pi
        angle = Utils.floorMod(angle, Math.PI);
        double[] angles = {angle - 2 * Math.PI, angle, angle + 2 * Math.PI}; // An array of all possible target angles
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
