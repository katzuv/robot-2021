package frc.robot.subsystems.climber.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.Constants;
import frc.robot.subsystems.climber.Climber;

/**
 * The command lifts the robot up by a given number of meters.
 */
public class Climb extends CommandBase {

    private final Climber climber;
    private double height;

    public Climb(Climber climber, double height) {
        this.climber = climber;
        this.height = height;
        addRequirements(climber);
    }

    @Override
    public void initialize() {
        new AutoClimb(climber);
        climber.setHeight(height);
    }

    @Override
    public void execute() {

    }

    @Override
    public boolean isFinished() {
        return Math.abs(climber.getHeight() - height) <= Constants.Climber.TOLERANCE;
    }

    @Override
    public void end(boolean interrupted) {
        new SetStopper(climber, Climber.PistonMode.CLOSED);
    }
}