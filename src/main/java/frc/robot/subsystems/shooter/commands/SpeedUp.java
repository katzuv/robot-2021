package frc.robot.subsystems.shooter.commands;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.shooter.Shooter;

/**
 * This command changes the velocity of the shooter to a specified velocity.
 */
public class SpeedUp extends CommandBase {
    private final Shooter shooter;
    private final Supplier<Double> velocity;

    public SpeedUp(Shooter shooter, Supplier<Double> velocity) {
        this.shooter = shooter;
        this.velocity = velocity;
        addRequirements(shooter);
    }

    @Override
    public void execute() {
        shooter.setVelocity(velocity.get());
    }

    @Override
    public boolean isFinished() {
        return shooter.hasReachedSetpoint(velocity.get());
    }
}
