package robot.subsystems.Commands;

import edu.wpi.first.wpilibj.command.Command;
import robot.Robot;

public class DriveCommand extends Command {

    private double speed;

    public DriveCommand(double speed){
        requires(Robot.drivetrain);
        this.speed = speed;
    }

    @Override
    protected void initialize() {
        Robot.drivetrain.setLeftSpeed(speed);
        Robot.drivetrain.setRightSpeed(speed);
    }

    @Override
    protected void execute() {
        System.out.println(speed);

    }

    @Override
    protected boolean isFinished() {
        return Robot.m_oi.xbox.getYButton();
    }

    @Override
    protected void interrupted() {

    }

    @Override
    protected void end() {
        Robot.drivetrain.setRightSpeed(0);
        Robot.drivetrain.setLeftSpeed(0);
    }
}
