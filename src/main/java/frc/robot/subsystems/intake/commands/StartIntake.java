package frc.robot.subsystems.intake.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.intake.Intake;

public class StartIntake extends CommandBase {
    private Intake intake;
    public StartIntake(Intake i) {
        intake = i;
        addRequirements(intake);
    }

    @Override
    public void initialize() {
        intake.setPower(0.5);
    }

    @Override
    public void execute() {
        //TODO: calc based on robot's speed
    }

    @Override
    public void end(boolean interrupted) {
        intake.setPower(0);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
