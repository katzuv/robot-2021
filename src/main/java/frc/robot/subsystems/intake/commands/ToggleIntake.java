package frc.robot.subsystems.intake.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.intake.Intake;

public class ToggleIntake extends InstantCommand {
    /**
     * this command toggles the state of intake's pistons
     * i.e. opened --> closed & closed --> opened
     */

    private Intake intake;
    public ToggleIntake(Intake i) {
        intake = i;
        addRequirements(intake);
    }

    @Override
    public void initialize() {
        intake.togglePiston();
    }
}
