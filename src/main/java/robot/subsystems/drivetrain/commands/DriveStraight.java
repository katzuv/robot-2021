package robot.subsystems.drivetrain.commands;

import edu.wpi.first.wpilibj.command.Command;
import robot.Robot;

public class DriveStraight extends Command {

    private double speed;

    public DriveStraight(double speed) {
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
        return false;
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
