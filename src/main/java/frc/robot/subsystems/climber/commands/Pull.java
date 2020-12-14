package frc.robot.subsystems.climber.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.subsystems.climber.Climber;

public class Pull extends CommandBase {

    private final Climber climber;
    private double height;

    public Pull(Climber climber, double height) {
        this.climber = climber;
        addRequirements(climber);
        this.height = height;
    }

    @Override
    public void initialize() {
        climber.setHeight(height);
    }

    @Override
    public void execute() {

    }

    @Override
    public void end(boolean interrupted) {

    }
}
