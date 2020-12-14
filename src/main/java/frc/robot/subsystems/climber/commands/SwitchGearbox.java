package frc.robot.subsystems.climber.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.climber.Climber;

public class SwitchGearbox extends InstantCommand {
    private final Climber climber;

    public SwitchGearbox(Climber climber) {
        this.climber = climber;
        addRequirements(climber);
    }

    @Override
    public void initialize() {
        climber.setGearboxMode(!climber.getGearboxMode().getValue());
    }

    @Override
    public void execute() {

    }


    @Override
    public void end(boolean interrupted) {

    }
}
