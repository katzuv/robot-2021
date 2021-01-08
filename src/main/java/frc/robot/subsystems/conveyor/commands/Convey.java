package frc.robot.subsystems.conveyor.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.conveyor.Conveyor;

/**
 * This command convey the balls to the shooter, or to the funnel.
 */
public class Convey extends CommandBase {
    private final Conveyor conveyor;
    private final double power;
    private final boolean toShooter;

    public Convey(Conveyor conveyor, double power, boolean toShooter) {
        this.conveyor = conveyor;
        this.power = power;
        this.toShooter = toShooter;

        addRequirements(conveyor);
    }

    @Override
    public void execute() {
        if (toShooter) {
            if (Conveyor.getBallsAmount() > 0)
                conveyor.setPower(power);
            // else TURN ON LEDS
        } else
            if (Conveyor.getBallsAmount() < Constants.Conveyor.MAX_BALLS_AMOUNT)
                conveyor.setPower(power);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        conveyor.stop();
    }
}
