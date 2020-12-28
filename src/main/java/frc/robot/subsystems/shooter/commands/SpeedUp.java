package frc.robot.subsystems.shooter.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.shooter.Shooter;

/**
 * This command changes the velocity of the shooter to a specified velocity, in order to shoot at the best rate.
 */
public class SpeedUp extends CommandBase {
    private final Shooter shooter;
    private final double velocity;

    public SpeedUp(Shooter shooter, double velocity) {
        this.shooter = shooter;
        this.velocity = velocity;
        addRequirements(shooter);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        shooter.setVelocity(velocity);
    }

    @Override
    public boolean isFinished() {
        return shooter.isReady(velocity);
    }

    @Override
    public void end(boolean interrupted) {
    }
}
