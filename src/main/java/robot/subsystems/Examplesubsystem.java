package robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;

public class Examplesubsystem extends Subsystem {

    public TalonSRX test = new TalonSRX(0);




    public Examplesubsystem(){
        test.set(ControlMode.MotionMagic, 2);
        test.configMotionSCurveStrength(1);
    }
    @Override
    protected void initDefaultCommand() {

    }
}
