package frc.robot.subsystems.climber.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.climber.Climber;

public class AutoClimb extends SequentialCommandGroup {

    private final Climber climber;

    public AutoClimb(Climber climber) {
        this.climber = climber;
        addCommands(new SetGearbox(climber, Climber.PistonMode.OPEN), new SetStopper(climber, Climber.PistonMode.CLOSED));
    }
}
