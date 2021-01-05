package frc.robot.subsystems.conveyor;

import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Ports;
import frc.robot.subsystems.UnitModel;

import static frc.robot.Constants.Conveyor.*;

public class Conveyor extends SubsystemBase {
    private final TalonFX motor = new TalonFX(Ports.Conveyor.MOTOR);
    private final UnitModel unitModel = new UnitModel(Constants.Conveyor.TICKS_PER_METER);
    public Conveyor() {
        motor.setInverted(Ports.Conveyor.IS_MOTOR_INVERTED);

        motor.configPeakOutputForward(FORWARD_PEAK, Constants.TALON_TIMEOUT);
        motor.configPeakOutputReverse(REVERSE_PEAK, Constants.TALON_TIMEOUT);

        motor.configSupplyCurrentLimit(SUPPLY_CURRENT_LIMIT, Constants.TALON_TIMEOUT);
        motor.configStatorCurrentLimit(STATOR_CURRENT_LIMIT, Constants.TALON_TIMEOUT);

        motor.enableVoltageCompensation(true);
        motor.configVoltageCompSaturation(12);
    }
    public double getPosition() {
        return unitModel.toUnits(motor.getSelectedSensorPosition());
    }

    public double getPower() {
        return motor.getMotorOutputPercent();
    }

    public void setPower(double power) {
        motor.set(TalonFXControlMode.PercentOutput, power);
    }

    /**
     * Stop the motor.
     */
    public void stop() {
        setPower(0);
    }
}
