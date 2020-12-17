package frc.robot.subsystems.flywheel;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.subsystems.UnitModel;

/**
 * The flywheel module class represents one wheel of the flywheel subsystem, and the class itself is a subsystem.
 * In case when you have a single-wheel flywheel you can (and should) use this class as the subsystem of the flywheel.
 * The purpose of the flywheel is to provide a set of functions to handle a shooting situation in a game.
 *
 * @author Barel Shlidor
 * @version 1.0
 * @using TalonFX
 * @since 2021
 */
public class FlywheelModule extends SubsystemBase {
    private final TalonFX motor;
    private final UnitModel rpsUnitModel = new UnitModel(Constants.Flywheel.TICKS_PER_ROTATION);

    /**
     * Initialize the Flywheel module with default values.
     *
     * @param port        the port of the motor.
     * @param inverted    whether the motor is inverted.
     * @param sensorPhase whether the (internal) sensor is inverted
     */
    public FlywheelModule(int port, boolean inverted, boolean sensorPhase) {
        motor = new TalonFX(port);

        motor.setInverted(inverted);
        motor.setSensorPhase(sensorPhase);

        motor.config_kP(0, Constants.Flywheel.PIDF[0], Constants.TALON_TIMEOUT);
        motor.config_kI(0, Constants.Flywheel.PIDF[1], Constants.TALON_TIMEOUT);
        motor.config_kD(0, Constants.Flywheel.PIDF[2], Constants.TALON_TIMEOUT);
        motor.config_kF(0, Constants.Flywheel.PIDF[3], Constants.TALON_TIMEOUT);

        motor.setNeutralMode(NeutralMode.Coast);

        motor.configForwardLimitSwitchSource(LimitSwitchSource.Deactivated, LimitSwitchNormal.Disabled);
        motor.configReverseLimitSwitchSource(LimitSwitchSource.Deactivated, LimitSwitchNormal.Disabled);

        motor.enableVoltageCompensation(true);
        motor.configVoltageCompSaturation(12);
    }

    /**
     * Get the velocity of the motor in rps.
     *
     * @return the velocity applied by the motor. [rps]
     * @see #setVelocity(double)
     */
    public double getVelocity() {
        return rpsUnitModel.toVelocity(motor.getSelectedSensorVelocity());
    }

    /**
     * Set the velocity to apply by the motor.
     *
     * @param velocity the desired velocity at which the motor will rotate. [rps]
     * @see #setPower(double)
     */
    public void setVelocity(double velocity) {
        motor.set(ControlMode.Velocity, rpsUnitModel.toTicks100ms(velocity));
    }

    /**
     * Set the power to apply by the motor.
     *
     * @param power the power at which the motor will rotate. [percentage, between 0 and 1]
     * @see #stop()
     */
    public void setPower(double power) {
        motor.set(ControlMode.PercentOutput, power);
    }

    /**
     * Get whether the flywheel has reached the desired velocity in order to reach the target.
     * Also, we check whether the flywheel has enough velocity in order to move the motor in first place.
     *
     * @param desiredVelocity the desired velocity at the motor will rotate. [rps]
     * @return whether the flywheel reaches the desired velocity.
     */
    public boolean isReady(double desiredVelocity) {
        return Math.abs(getVelocity() - desiredVelocity) < Constants.Flywheel.VELOCITY_TOLERANCE && getVelocity() > Constants.Flywheel.MINIMAL_VELOCITY;
    }

    /**
     * Stop the motor from moving.
     */
    public void stop() {
        setPower(0);
    }
}
