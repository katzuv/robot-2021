package robot.subsystems.drivetrain.commands;

import robot.OI;
import robot.subsystems.drivetrain.SwerveDrive;

public class HolonomicDrive extends Command {

    private SwerveDrive swerveDrive;

    public HolonomicDrive(SwerveDrive swerveDrive) {
        this.swerveDrive = swerveDrive;
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

        swerveDrive.holonomicDrive(forward, strafe, rotation);
    }

    private double joystickDeadband(double val) {
        if (val < 0.05)
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
