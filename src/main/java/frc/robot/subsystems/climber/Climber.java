package frc.robot.subsystems.climber;

import frc.robot.Constants;
import frc.robot.Ports;
import frc.robot.subsystems.UnitModel;

public class Climber {
    private final TalonSRX leftMotor = new TalonSRX(Ports.Climber.LEFT_MOTOR);
    private final TalonSRX rightMotor = new TalonSRX(Ports.Climber.RIGHT_MOTOR);
    private final UnitModel unitModel = new UnitModel(Constants.Climber.TICKS_PER_UNIT);

    public Climber() {


    }

}
