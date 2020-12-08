package frc.robot.subsystems.drivetrain.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.OI;
import frc.robot.Robot;
import frc.robot.subsystems.drivetrain.SwerveDrive;
import org.techfire225.webapp.FireLog;

public class HolonomicDrive extends CommandBase {

    private SwerveDrive swerveDrive;

    public HolonomicDrive(SwerveDrive swerveDrive) {
        this.swerveDrive = swerveDrive;
        addRequirements(swerveDrive);
    }

    @Override
    public void initialize() {}

    @Override
    public void execute() {
        double forward = -OI.getJoystickY();
        double strafe = OI.getJoystickX();
        double rotation = OI.getJoystickZ();

        forward = joystickDeadband(forward);
        strafe = joystickDeadband(strafe);
        rotation = joystickDeadband(rotation);
        System.out.println(forward);
        System.out.println(strafe);
        System.out.println(rotation);
        swerveDrive.holonomicDrive(forward, strafe, rotation);
        //FireLog.log("swerve velocity", swerveDrive.getVelocity()[0]);
        FireLog.log("swerve angle by vectors", swerveDrive.getVelocity()[1]);
        FireLog.log("swerve direction", Robot.gyro.getAngle());
    }
    /**
     * sets the value of the joystick to 0 if the value is less than the threshold
     * @param val the joystick value
     * @return 0 if val is less than the threshold else val
     */
    private double joystickDeadband(double val) {
        if (val < Constants.SwerveDrive.JOYSTICK_THRESHOLD)
            return 0;
        return val;
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
