package frc.robot.subsystems.drivetrain.commands;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.OI;
import frc.robot.RobotContainer;
import frc.robot.subsystems.drivetrain.SwerveDrive;

public class TankDrive extends CommandBase {
    private SwerveDrive swerveDrive;

    public TankDrive(SwerveDrive swerveDrive) {
        this.swerveDrive = swerveDrive;
        addRequirements(swerveDrive);
    }

    @Override
    public void initialize() {
        for (int i = 0; i < 4; i++) {
            swerveDrive.swerveModules[i].setAngle(0);
        }
    }

    @Override
    public void execute() {
        swerveDrive.swerveModules[0].setSpeed(RobotContainer.xbox.getY(GenericHID.Hand.kRight));
        swerveDrive.swerveModules[2].setSpeed(RobotContainer.xbox.getY(GenericHID.Hand.kRight));

        swerveDrive.swerveModules[1].setSpeed(RobotContainer.xbox.getY(GenericHID.Hand.kLeft));
        swerveDrive.swerveModules[3].setSpeed(RobotContainer.xbox.getY(GenericHID.Hand.kLeft));
    }
}
