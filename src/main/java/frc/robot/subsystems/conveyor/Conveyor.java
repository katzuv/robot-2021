package frc.robot.subsystems.conveyor;

import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Ports;
import frc.robot.subsystems.UnitModel;
import frc.robot.utils.DeadbandProximity;

import static frc.robot.Constants.Conveyor.*;

public class Conveyor extends SubsystemBase {
    private static int balls = Constants.Conveyor.INITIAL_BALLS_AMOUNT;

    private final TalonFX motor = new TalonFX(Ports.Conveyor.MOTOR);
    private final UnitModel unitModel = new UnitModel(Constants.Conveyor.TICKS_PER_METER);
    private final DeadbandProximity shooterProximity = new DeadbandProximity(Ports.Conveyor.SHOOTER_PROXIMITY,
            SHOOTER_PROXIMITY_LOST_VOLTAGE, SHOOTER_PROXIMITY_SENSE_VOLTAGE);
    private final DeadbandProximity funnelProximity = new DeadbandProximity(Ports.Conveyor.SHOOTER_PROXIMITY,
            FUNNEL_PROXIMITY_LOST_VOLTAGE, FUNNEL_PROXIMITY_SENSE_VOLTAGE);

    public Conveyor() {
        motor.setInverted(Ports.Conveyor.IS_MOTOR_INVERTED);

        motor.configPeakOutputForward(FORWARD_PEAK, Constants.TALON_TIMEOUT);
        motor.configPeakOutputReverse(REVERSE_PEAK, Constants.TALON_TIMEOUT);

        motor.configSupplyCurrentLimit(SUPPLY_CURRENT_LIMIT, Constants.TALON_TIMEOUT);
        motor.configStatorCurrentLimit(STATOR_CURRENT_LIMIT, Constants.TALON_TIMEOUT);

        motor.enableVoltageCompensation(true);
        motor.configVoltageCompSaturation(12);
    }

    /**
     * Get the amount of balls sensed by the proximity.
     *
     * @return the amount of balls in the conveyor.
     */
    public static synchronized int getBallsAmount() {
        return balls;
    }

    /**
     * Increment by one the amount of balls (only programmatically).
     */
    public static synchronized void addBall() {
        balls++;
    }

    /**
     * Decrement by one the amount of balls (only programmatically).
     */
    public static synchronized void removeBall() {
        balls--;
    }

    /**
     * Get the power applied by the motor.
     *
     * @return the power applied by the motor. [%]
     */
    public double getPower() {
        return motor.getMotorOutputPercent();
    }

    /**
     * Set the power to apply by the motor of the conveyor.
     *
     * @param power the power to apply by the motor. [%]
     */
    public void setPower(double power) {
        motor.set(TalonFXControlMode.PercentOutput, power);
    }

    /**
     * Stop the motor.
     */
    public void stop() {
        setPower(0);
    }

    @Override
    public void periodic() {
        shooterProximity.updateState();
        funnelProximity.updateState();

        boolean movingUp = getPower() >= 0;
        if (!shooterProximity.hasObjectSensed() && shooterProximity.hasStateChanged() && movingUp ||
                !funnelProximity.hasObjectSensed() && funnelProximity.hasStateChanged() && !movingUp) {
            Conveyor.removeBall();
        }

        if (funnelProximity.hasObjectSensed() && funnelProximity.hasStateChanged() && movingUp) {
            Conveyor.addBall();
        }
    }
}
