package frc.robot.subsystems.flywheel.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.flywheel.Flywheel;

public class Shoot extends CommandBase {
    private final Flywheel flywheel;
    private final double velocity;

    public Shoot(Flywheel flywheel, double velocity) {
        this.flywheel = flywheel;
        this.velocity = velocity;

        addRequirements(flywheel);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        flywheel.setVelocity(velocity);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        flywheel.stop();
    }
}
