package frc.robot.subsystems.conveyor;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Conveyor extends SubsystemBase {
    private final TalonFX motor = new TalonFX(Ports.Conveyor.MOTOR);
    private final UnitModel unitModel = new UnitModel(Constants.Conveyor.TICKS_PER_METER);
    public Conveyor() {
        motor.setInverted(Ports.Conveyor.IS_MOTOR_INVERTED);

        motor.configPeakOutputForward(Constants.Conveyor.FORWARD_PEAK, Constants.TALON_TIMEOUT);
        motor.configPeakOutputReverse(Constants.Conveyor.REVERSE_PEAK, Constants.TALON_TIMEOUT);

        motor.configSupplyCurrentLimit(Constants.Conveyor.SUPPLY_CURRENT_LIMIT, Constants.TALON_TIMEOUT);
        motor.configStatorCurrentLimit(Constants.Conveyor.STATOR_CURRENT_LIMIT, Constants.TALON_TIMEOUT);

        motor.enableVoltageCompensation(true);
        motor.configVoltageCompSaturation(12);
    }
}
