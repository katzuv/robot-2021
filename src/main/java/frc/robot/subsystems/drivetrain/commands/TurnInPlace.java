package frc.robot.subsystems.drivetrain.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.OI;
import frc.robot.Robot;
import frc.robot.RobotContainer;
import frc.robot.subsystems.drivetrain.SwerveDrive;
import frc.robot.valuetuner.WebConstant;
import org.techfire225.webapp.FireLog;

public class TurnInPlace extends CommandBase {

    private SwerveDrive swerveDrive;
    //    private WebConstant target = new WebConstant("targetAngle", 0);
    private WebConstant target0 = new WebConstant("target0", 0);
    private WebConstant target1 = new WebConstant("target1", 0);
    private WebConstant target2 = new WebConstant("target2", 0);
    private WebConstant target3 = new WebConstant("target3", 0);


    public TurnInPlace(SwerveDrive swerveDrive) {
        this.swerveDrive = swerveDrive;
        addRequirements(swerveDrive);
    }

    @Override
    public void execute() {
        double rotation = -OI.xbox.getY();
        rotation = joystickDeadband(rotation);

        swerveDrive.swerveModules[0].setAngle(target0.get());
        swerveDrive.swerveModules[1].setAngle(target1.get());
        swerveDrive.swerveModules[2].setAngle(target2.get());
        swerveDrive.swerveModules[3].setAngle(target3.get());

        FireLog.log("angle ", swerveDrive.swerveModules[2].getAngle());
        FireLog.log("swerve velocity", swerveDrive.getVelocity()[0]);
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
