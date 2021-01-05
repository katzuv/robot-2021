package frc.robot.subsystems.climber.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.Constants;
import frc.robot.subsystems.climber.Climber;

/**
 * The command lifts the robot up by a given number of meters.
 */
public class ManageClimb extends CommandBase {

    private final Climber climber;
    private double height;

    public ManageClimb(Climber climber, double height) {
        this.climber = climber;
        this.height = height;
        addRequirements(climber);
    }

    @Override
    public void initialize() {
        climber.setHeight(height);
    }

    @Override
    public boolean isFinished() {
        return Math.abs(climber.getHeight() - height) <= Constants.Climber.TOLERANCE;
    }
}
