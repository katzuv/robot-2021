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

    }

    @Override
    protected boolean isFinished() {
        return false;
    }

    @Override
    protected void interrupted() {

    }

    @Override
    protected void end() {

    }
}
