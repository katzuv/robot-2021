package frc.robot.subsystems.climber.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.climber.Climber;

public class AutoClimb extends SequentialCommandGroup {

    private final Climber climber;

    public AutoClimb(Climber climber, double height) {
        this.climber = climber;
        addCommands(new ShiftGear(climber, Climber.PistonMode.OPEN), new SetStopper(climber, Climber.PistonMode.CLOSED), new ManageClimb(climber, height));
    }
}
