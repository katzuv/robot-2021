package frc.robot.subsystems.drivetrain.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.*;
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
        rotation = Utils.joystickDeadband(rotation);

        swerveDrive.getModule(0).setAngle(target0.get());
        swerveDrive.getModule(1).setAngle(target1.get());
        swerveDrive.getModule(2).setAngle(target2.get());
        swerveDrive.getModule(3).setAngle(target3.get());

        FireLog.log("angle ", swerveDrive.swerveModules[2].getAngle());
        FireLog.log("swerve velocity", swerveDrive.getVelocity()[0]);
        FireLog.log("swerve angle by vectors", swerveDrive.getVelocity()[1]);
        FireLog.log("swerve direction", Robot.gyro.getAngle());
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
