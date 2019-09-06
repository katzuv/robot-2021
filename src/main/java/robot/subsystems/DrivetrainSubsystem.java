package robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import edu.wpi.first.wpilibj.command.Subsystem;

public class DrivetrainSubsystem extends Subsystem {

    public TalonSRX leftMaster = new TalonSRX(16);
    public TalonSRX rightMaster = new TalonSRX(11);
    public VictorSPX right1 = new VictorSPX(12);
    public VictorSPX left1 = new VictorSPX(14);
    public VictorSPX right2 = new VictorSPX(13);
    public VictorSPX left2 = new VictorSPX(15);

    public DrivetrainSubsystem(){
        leftMaster.setInverted(true);
        left1.setInverted(true);
        left2.setInverted(true);
        rightMaster.setInverted(false);
        right1.setInverted(false);
        right2.setInverted(false);

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

    public double getLeftDistance(){
        return convertTicksToDistance(leftMaster.getSelectedSensorPosition());
    }

    public double getRightDistance(){
        return convertTicksToDistance(rightMaster.getSelectedSensorPosition());
    }

    public int convertDistanceToTicks(double distance) {
        return (int) (distance * DrivetrainConstants.TICKS_PER_METER);
    }

    public double convertTicksToDistance(int tick) {
        return tick / DrivetrainConstants.TICKS_PER_METER;
    }

    @Override
    protected void initDefaultCommand() {

    }
}
