package frc.robot.subsystems.climber.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;

import frc.robot.Constants;
import frc.robot.subsystems.climber.Climber;

public class Pull extends CommandBase {

    private final Climber climber;
    private double height;

    public Pull(Climber climber, double height) {
        this.climber = climber;
        this.height = height;
        addRequirements(climber);
    }

    @Override
    public void initialize() {
        if (climber.getStopperMode() == Climber.PistonMode.OPEN)
            new SwitchMechanicalStopper(climber);
        if (climber.getGearboxMode() == Climber.PistonMode.CLOSED)
            new SwitchGearbox(climber);
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

    }
}
