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
    private WebConstant target = new WebConstant("targetAngle", Math.PI);

    public TurnInPlace(SwerveDrive swerveDrive) {
        this.swerveDrive = swerveDrive;
        addRequirements(swerveDrive);
    }

    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        double rotation = -RobotContainer.xbox.getY();

        rotation = joystickDeadband(rotation);

        System.out.println(rotation);

        for (int i = 0; i < 4; i++){

            swerveDrive.swerveModules[i].setAngle(target.get());
            System.out.println(i + " " + swerveDrive.swerveModules[i].getAngle());

        }
//        swerveDrive.holonomicDrive(0, 0, rotation);
        FireLog.log("target angle", target.get());
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
