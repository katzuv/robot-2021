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

    /**
     * sets the motor inverted
     */
    public Intake() {
        motor.setInverted(Ports.Intake.IS_INVERTED);
    }

    /**
     * @return intake's motor velocity
     */
    public double getVelocity(){
        return unitModel.toVelocity(motor.getSelectedSensorVelocity());
    }

    /**
     * @return the state of the right solenoid
     */
    public boolean getStateRight(){
        return solenoidR.get();
    }

    /**
     * @return the state of the left solenoid
     */
    public boolean getStateL(){
        return solenoidL.get();
    }

    /**
     * sets the output of intake's motor by present output (%)
     */
    public void setPower(double power){
        motor.set(ControlMode.PercentOutput,power);
    }

    /**
     * toggles piston's state (opened --> closed || closed -- > opened)
     */
    public void togglePiston(){
        solenoidL.set(!solenoidL.get());
        solenoidR.set(!solenoidR.get());
    }
}
