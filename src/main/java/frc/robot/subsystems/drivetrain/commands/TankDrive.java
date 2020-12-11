package frc.robot.subsystems.drivetrain.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.OI;
import frc.robot.subsystems.drivetrain.SwerveDrive;

public class TankDrive extends CommandBase {
    private SwerveDrive swerveDrive;

    public TankDrive(SwerveDrive swerveDrive) {
        this.swerveDrive = swerveDrive;
        addRequirements(swerveDrive);
    }

    @Override
    public void execute() {
        swerveDrive.swerveModules[0].setSpeed(-OI.getJoystickY());
        swerveDrive.swerveModules[2].setSpeed(-OI.getJoystickY());

        swerveDrive.swerveModules[0].setSpeed(OI.getJoystickX());
        swerveDrive.swerveModules[2].setSpeed(OI.getJoystickX());
    }
}
