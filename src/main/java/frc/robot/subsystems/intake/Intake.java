package frc.robot.subsystems.intake;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Ports;
import frc.robot.subsystems.UnitModel;

public class Intake extends SubsystemBase {
    private TalonSRX motor = new TalonSRX(Ports.Intake.MOTOR);
    private Solenoid solenoidR = new Solenoid(Ports.Intake.SOLENOID_RIGHT);
    private Solenoid solenoidL = new Solenoid(Ports.Intake.SOLENOID_LEFT);
    private UnitModel unitModel = new UnitModel(Constants.Intake.TICK_PER_METER);

    public Intake() {
        motor.setInverted(Ports.Intake.IS_INVERTED);
    }

    public double getVelocity(){
        return unitModel.toVelocity(motor.getSelectedSensorVelocity());
    }

    public boolean getStateRight(){
        return solenoidR.get();
    }

    public boolean getStateL(){
        return solenoidL.get();
    }

    public void setPower(double power){
        motor.set(ControlMode.PercentOutput,power);
    }

    public void togglePiston(){
        solenoidL.set(!solenoidL.get());
        solenoidR.set(!solenoidR.get());
    }
}
