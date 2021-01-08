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

    public Convey(Conveyor conveyor, double power) {
        this.conveyor = conveyor;
        this.power = power;

        addRequirements(conveyor);
    }

    @Override
    public void execute() {
        if (Conveyor.getBallsAmount() > 0)
            conveyor.setPower(power);
        // else TURN ON LEDS
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
