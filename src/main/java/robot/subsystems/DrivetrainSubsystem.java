package robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.wpilibj.command.Subsystem;

public class DrivetrainSubsystem extends Subsystem {

    public TalonSRX leftMaster = new TalonSRX(0);
    public TalonSRX rightMaster = new TalonSRX(1);
    public VictorSPX right1 = new VictorSPX(2);
    public VictorSPX left1 = new VictorSPX(3);
    public VictorSPX right2 = new VictorSPX(4);
    public VictorSPX left2 = new VictorSPX(5);

    public DrivetrainSubsystem(){
        leftMaster.setInverted(true);
        rightMaster.setInverted(false);

        right1.follow(rightMaster);
        right2.follow(rightMaster);

        left1.follow(leftMaster);
        left2.follow(leftMaster);
    }

    public void setLeftSpeed(double speed){
        leftMaster.set(ControlMode.PercentOutput,speed);
    }

    public void setRightSpeed(double speed){
        rightMaster.set(ControlMode.PercentOutput,speed);
    }

    @Override
    protected void initDefaultCommand() {

    }
}
