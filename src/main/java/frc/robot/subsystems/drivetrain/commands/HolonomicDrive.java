package frc.robot.subsystems.drivetrain.commands;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.OI;
import frc.robot.Robot;
import frc.robot.RobotContainer;
import frc.robot.subsystems.drivetrain.SwerveDrive;
import org.techfire225.webapp.FireLog;

public class HolonomicDrive extends CommandBase {

    private SwerveDrive swerveDrive;

    public HolonomicDrive(SwerveDrive swerveDrive) {
        this.swerveDrive = swerveDrive;
        addRequirements(swerveDrive);
    }

    @Override
    public void initialize() {
        Robot.gyro.reset();

    }

    @Override
    public void execute() {
        double forward = -RobotContainer.xbox.getY(GenericHID.Hand.kRight);
        double strafe = -RobotContainer.xbox.getX(GenericHID.Hand.kRight);
        double rotation = -RobotContainer.xbox.getX(GenericHID.Hand.kLeft);
        forward = joystickDeadband(forward);
        strafe = joystickDeadband(strafe);
        rotation = joystickDeadband(rotation);
        if (forward != 0 || strafe != 0 || rotation != 0) {
            swerveDrive.holonomicDrive(forward, strafe, rotation);
        } else {
            for (int i = 0; i < 4; i++) {
                swerveDrive.swerveModules[i].setSpeed(0);
                swerveDrive.swerveModules[i].setAngle(swerveDrive.swerveModules[i].getAngle());
            }
        }

        FireLog.log("swerve angle by vectors", swerveDrive.getVelocity()[1]);
        FireLog.log("swerve direction", Robot.gyro.getAngle());
    }

    /**
     * sets the value of the joystick to 0 if the value is less than the threshold
     *
     * @param val the joystick value
     * @return 0 if val is less than the threshold else val
     */
    private double joystickDeadband(double val) {
        if (Math.abs(val) < Constants.SwerveDrive.JOYSTICK_THRESHOLD)
            return 0;
        return Math.pow(val, 3);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void end(boolean interrupted) {
        swerveDrive.stop();
    }


}
