package frc.robot.subsystems.intake;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.Solenoid;
import frc.robot.Constants;
import frc.robot.Ports;
import frc.robot.subsystems.UnitModel;

public class Intake {
    private TalonSRX motor = new TalonSRX(Ports.Intake.MOTOR);
    private Solenoid solenoidR = new Solenoid(Ports.Intake.SOLENOID_RIGHT);
    private Solenoid solenoidL = new Solenoid(Ports.Intake.SOLENOID_LEFT);

    public Intake() {
        motor.setInverted(Ports.Intake.IS_INVERTED);
        UnitModel unitModel = new UnitModel(Constants.Intake.TICK_PER_METER);
    public double getVelocity(){
        return unitModel.toVelocity(motor.getSelectedSensorVelocity());
    }
}
