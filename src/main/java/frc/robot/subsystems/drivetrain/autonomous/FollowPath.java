package frc.robot.subsystems.drivetrain.autonomous;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.controller.RamseteController;
import edu.wpi.first.wpilibj.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.geometry.Translation2d;
import edu.wpi.first.wpilibj.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveKinematics;
import edu.wpi.first.wpilibj.kinematics.DifferentialDriveWheelSpeeds;
import edu.wpi.first.wpilibj.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.trajectory.Trajectory;
import edu.wpi.first.wpilibj.util.Units;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.drivetrain.SwerveDrive;
import frc.robot.utils.auto.Path;
import org.ghrobotics.lib.debug.FalconDashboard;
import org.techfire225.webapp.FireLog;

/**
 * This command handles trajectory-following.
 * A modified fork of {@link edu.wpi.first.wpilibj2.command.RamseteCommand}
 */
public class FollowPath extends CommandBase {

    private final Timer timer = new Timer();
    private boolean resetDrivetrain = false;
    private Path path;
    private SwerveModuleState[] prevSpeeds;
    private double prevTime;

    private static final RamseteController follower = new RamseteController(Constants.Autonomous.kBeta, Constants.Autonomous.kZeta);
    private static final SimpleMotorFeedforward leftfeedforward = new SimpleMotorFeedforward(Constants.Autonomous.leftkS, Constants.Autonomous.leftkV, Constants.Autonomous.leftkA);
    private static final SimpleMotorFeedforward rightfeedforward = new SimpleMotorFeedforward(Constants.Autonomous.rightkS, Constants.Autonomous.rightkV, Constants.Autonomous.rightkA);
    private static final PoseEstimator
    private final SwerveDrive swerveDrive;
    private Trajectory trajectory;

    public FollowPath(SwerveDrive swerveDrive, Trajectory trajectory, boolean resetDrivetrain) {
        addRequirements(swerveDrive);
        this.trajectory = trajectory;
        this.swerveDrive = swerveDrive;
        this.resetDrivetrain = resetDrivetrain;
    }

    public FollowPath(SwerveDrive swerveDrive, Path path) {
        addRequirements(swerveDrive);
        this.path = path;
        this.swerveDrive = swerveDrive;
    }

    @Override
    public void initialize() {
        if(trajectory == null) {
            if (!path.hasTrajectory()) {
                path.generate(swerveDrive.getPose());
            }

            this.trajectory = path.getTrajectory();
        }

        if(resetDrivetrain)
            swerveDrive.setPose(trajectory.getInitialPose());

        FalconDashboard.INSTANCE.setFollowingPath(true);
        prevTime = 0;
        var initialState = trajectory.sample(0);

        prevSpeeds = SwerveDrive.kinematics.toSwerveModuleStates(new ChassisSpeeds(), new Translation2d());

        timer.reset();
        timer.start();
    }

    @Override
    public void execute() {
        double curTime = timer.get();
        double dt = curTime - prevTime;

        Trajectory.State state = trajectory.sample(curTime);

        var targetWheelSpeeds = SwerveDrive.kinematics.toSwerveModuleStates(
                follower.calculate(swerveDrive.getPose(), state)
        );

        var leftSpeedSetpoint = targetWheelSpeeds.leftMetersPerSecond;
        var rightSpeedSetpoint = targetWheelSpeeds.rightMetersPerSecond;

        FireLog.log("autoLeftSetpoint", Math.abs(leftSpeedSetpoint));
        FireLog.log("autoRightSetpoint", Math.abs(rightSpeedSetpoint));

        double leftFeedforward =
                leftfeedforward.calculate(leftSpeedSetpoint,
                        (leftSpeedSetpoint - prevSpeeds.leftMetersPerSecond) / dt);

        double rightFeedforward =
                rightfeedforward.calculate(rightSpeedSetpoint,
                        (rightSpeedSetpoint - prevSpeeds.rightMetersPerSecond) / dt);

        // feeds the corresponding control to each wheel
        for (int k = 0; k < 4; k++) {
            swerveDrive.swerveModules[k].setSpeed();
            swerveDrive.swerveModules[k].setAngle();
        }

        FireLog.log("autoLeftTarget", leftSpeedSetpoint);
        FireLog.log("autoLeftVelocity", swerveDrive.getLeftVelocity());

        FalconDashboard.INSTANCE.setPathHeading(state.poseMeters.getRotation().getRadians());
        FalconDashboard.INSTANCE.setPathX(Units.metersToFeet(state.poseMeters.getTranslation().getX()));
        FalconDashboard.INSTANCE.setPathY(Units.metersToFeet(state.poseMeters.getTranslation().getY()));

        prevTime = curTime;
        prevSpeeds = targetWheelSpeeds;
    }

    @Override
    public void end(boolean interrupted) {
        FalconDashboard.INSTANCE.setFollowingPath(false);
        timer.stop();
    }

    @Override
    public boolean isFinished() {
        return timer.hasPeriodPassed(trajectory.getTotalTimeSeconds());
    }
}
