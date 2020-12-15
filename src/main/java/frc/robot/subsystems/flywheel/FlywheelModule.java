package frc.robot.subsystems.flywheel;

import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class FlywheelModule extends SubsystemBase {
    private final TalonFX motor;

    public FlywheelModule(int port, boolean inverted, boolean sensorPhase, double[] PIDF) {
        motor = new TalonFX(port);

        motor.setInverted(inverted);
        motor.setSensorPhase(sensorPhase);

        motor.config_kP(0, PIDF[0], Constants.TALON_TIMEOUT);
        motor.config_kI(0, PIDF[1], Constants.TALON_TIMEOUT);
        motor.config_kD(0, PIDF[2], Constants.TALON_TIMEOUT);
        motor.config_kF(0, PIDF[3], Constants.TALON_TIMEOUT);

        motor.setNeutralMode(NeutralMode.Coast);

        motor.configForwardLimitSwitchSource(LimitSwitchSource.Deactivated, LimitSwitchNormal.Disabled);
        motor.configReverseLimitSwitchSource(LimitSwitchSource.Deactivated, LimitSwitchNormal.Disabled);

        motor.enableVoltageCompensation(true);
        motor.configVoltageCompSaturation(12);
    }
}
