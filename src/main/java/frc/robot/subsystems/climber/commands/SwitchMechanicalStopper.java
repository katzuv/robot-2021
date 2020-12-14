package frc.robot.subsystems.climber.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.climber.Climber;

public class SwitchMechanicalStopper extends InstantCommand {
    private final Climber climber;

    public SwitchMechanicalStopper(Climber climber) {
        this.climber = climber;
        addRequirements(climber);
    }

    @Override
    public void initialize() {
        climber.setStopperMode(!climber.getGearboxMode().getValue());
    }

    @Override
    public void execute() {

    }

    @Override
    public void end(boolean interrupted) {

    }
}
