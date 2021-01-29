package frc.robot.subsystems.conveyor.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.conveyor.Conveyor;

public class LoadConveyor extends CommandBase {
    private final Conveyor conveyor;
    private final double power;

    public LoadConveyor(Conveyor conveyor, double power) {
        this.conveyor = conveyor;
        this.power = power;

        addRequirements(conveyor);
    }
}
