package frc.robot.subsystems.flywheel;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.subsystems.UnitModel;

public class FlywheelModule extends SubsystemBase {
    private final TalonFX motor;
    private final UnitModel rpsUnitModel = new UnitModel(Constants.Flywheel.TICKS_PER_ROTATION);

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

    public double getVelocity() {
        return rpsUnitModel.toVelocity(motor.getSelectedSensorVelocity());
    }

    public void setVelocity(double velocity) {
        motor.set(ControlMode.Velocity, rpsUnitModel.toTicks100ms(velocity));
    }

    public void setPower(double power) {
        motor.set(ControlMode.PercentOutput, power);
    }

  
    public boolean isReady(double desiredVelocity) {
        return Math.abs(getVelocity() - desiredVelocity) < Constants.Flywheel.VELOCITY_TOLERANCE && getVelocity() > Constants.Flywheel.MINIMAL_VELOCITY;
    }

    public void stop() {
        setPower(0);
    }
}
