package frc.robot.subsystems.intake.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.intake.Intake;

public class StartIntake extends CommandBase {
    private Intake intake;
    private boolean direction;
    public StartIntake(Intake i , boolean direction) {
        intake = i;
        this.direction = direction;
        addRequirements(intake);
    }

    @Override
    public void initialize() {
        if(!intake.isOpen())
            intake.togglePiston();
        if(direction)
            intake.setVelocity(Constants.Intake.VELOCITY);// field --> funnel
        else
            intake.setVelocity(-1*Constants.Intake.VELOCITY);// funnel --> field
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        intake.setVelocity(0);
    }
}
