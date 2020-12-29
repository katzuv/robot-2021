package frc.robot.subsystems.shooter.commands;

import java.util.function.Supplier;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.shooter.Shooter;

/**
 * This command keeps the shooter in the specified velocity.
 * This command should <b>NOT</b> be used in order to change the velocity applied by the shooter.
 * The command should come in conjunction with a button, and not to start/stop it after a specified time.
 * If you wish to accelerate the shooter to a specified velocity, please look at {@link SpeedUp}.
 *
 * @see SpeedUp
 */
public class Shoot extends CommandBase {
    private final Shooter shooter;
    private final Supplier<Double> velocity;

    public Shoot(Shooter shooter, Supplier<Double> velocity) {
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
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        shooter.stop();
    }
}
