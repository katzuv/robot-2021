package frc.robot.subsystems.conveyor.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.conveyor.Conveyor;

public class LoadConveyor extends CommandBase {
    private final Conveyor conveyor;
    private final double power;

    public LoadConveyor(Conveyor conveyor, double power) {
        this.conveyor = conveyor;
        this.power = power;

        addRequirements(conveyor);
    }

    @Override
    public void execute() {
        if (Conveyor.getBallsAmount() < Constants.Conveyor.MAX_BALLS_AMOUNT)
            conveyor.setPower(power);
        // else TURN LEDS TO RED
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
