package frc.robot.subsystems.shooter;

import com.ctre.phoenix.motorcontrol.*;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import edu.wpi.first.wpilibj.controller.LinearQuadraticRegulator;
import edu.wpi.first.wpilibj.estimator.KalmanFilter;
import edu.wpi.first.wpilibj.system.LinearSystem;
import edu.wpi.first.wpilibj.system.LinearSystemLoop;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpiutil.math.Matrix;
import edu.wpi.first.wpiutil.math.Nat;
import edu.wpi.first.wpiutil.math.VecBuilder;
import edu.wpi.first.wpiutil.math.Vector;
import edu.wpi.first.wpiutil.math.numbers.N1;
import frc.robot.Constants;
import frc.robot.Ports;
import frc.robot.subsystems.UnitModel;

import static frc.robot.Constants.Shooter.*;

/**
 * The shooter class represents the physical shooter.
 * The purpose of the flywheel is to provide a set of functions to handle a shooting situation in a game.
 *
 * @author Barel
 * @version 1.0
 * @using TalonFX
 * @since 2021
 */
public class Shooter extends SubsystemBase {
    private final TalonFX main = new TalonFX(Ports.Shooter.MAIN);
    private final TalonFX aux = new TalonFX(Ports.Shooter.AUX);
    private final UnitModel unitModel = new UnitModel(Constants.Shooter.TICKS_PER_ROTATION);

    private final LinearSystemLoop<N1, N1, N1> stateSpacePredictor;

    public Shooter() {
        main.setInverted(Ports.Shooter.MAIN_INVERTED);
        aux.setInverted(Ports.Shooter.AUX_INVERTED);
        main.setSensorPhase(Ports.Shooter.IS_SENSOR_INVERTED);

        main.setNeutralMode(NeutralMode.Coast);
        aux.setNeutralMode(NeutralMode.Coast);

        main.configForwardLimitSwitchSource(LimitSwitchSource.Deactivated, LimitSwitchNormal.Disabled);
        main.configReverseLimitSwitchSource(LimitSwitchSource.Deactivated, LimitSwitchNormal.Disabled);

        main.enableVoltageCompensation(true);
        aux.enableVoltageCompensation(true);

        main.configVoltageCompSaturation(Constants.MAXIMAL_VOLTAGE);
        aux.configVoltageCompSaturation(Constants.MAXIMAL_VOLTAGE);

        aux.follow(main);

        // https://file.tavsys.net/control/controls-engineering-in-frc.pdf Page 76
        Vector<N1> A = VecBuilder.fill(-Math.pow(GEAR_RATIO, 2) * Kt / (Kv * OMEGA * J)); //Change the amount of cells and rows
        Vector<N1> B = VecBuilder.fill(GEAR_RATIO * Kt / (OMEGA * J));
        LinearSystem<N1, N1, N1> stateSpace = new LinearSystem<>(A, B, Matrix.eye(Nat.N1()), new Matrix<>(Nat.N1(), Nat.N1()));
        KalmanFilter<N1, N1, N1> kalman = new KalmanFilter<>(Nat.N1(), Nat.N1(), stateSpace,
                VecBuilder.fill(MODEL_TOLERANCE),
                VecBuilder.fill(ENCODER_TOLERANCE),
                Constants.ROBOT_TIMEOUT
        );
        LinearQuadraticRegulator<N1, N1, N1> lqr = new LinearQuadraticRegulator<>(stateSpace, VecBuilder.fill(VELOCITY_TOLERANCE),
                VecBuilder.fill(Constants.MAXIMAL_VOLTAGE),
                Constants.ROBOT_TIMEOUT // time between loops, DON'T CHANGE
        );
        this.stateSpacePredictor = new LinearSystemLoop<>(stateSpace, lqr, kalman, Constants.MAXIMAL_VOLTAGE, Constants.ROBOT_TIMEOUT);
    }

    /**
     * @return the velocity of the shooter. [RPS]
     * @see #setVelocity(double)
     */
    public double getVelocity() {
        return unitModel.toVelocity(main.getSelectedSensorVelocity());
    }

    /**
     * Set the velocity to apply by the motor.
     *
     * @param velocity the desired velocity at which the motor will rotate. [RPS]
     * @see #setPower(double)
     */
    public void setVelocity(double velocity) {
        stateSpacePredictor.setNextR(VecBuilder.fill(velocity)); //r = reference
        stateSpacePredictor.correct(VecBuilder.fill(getVelocity()));
        stateSpacePredictor.predict(Constants.ROBOT_TIMEOUT); //every 20 ms

        double voltageToApply = stateSpacePredictor.getU(0); // u = input, calculated by the input.
        // returns the voltage to apply (between 0 and 12)
        setPower(voltageToApply / Constants.MAXIMAL_VOLTAGE); // map to be between 0 and 1
    }

    /**
     * Set the power to apply by the motor.
     *
     * @param power the power at which the motor will rotate. [percentage, between 0 and 1]
     * @see #stop()
     */
    public void setPower(double power) {
        main.set(TalonFXControlMode.PercentOutput, power, DemandType.ArbitraryFeedForward, Constants.Shooter.ARBITRARY_FEED_FORWARD);
    }

    /**
     * Estimate the velocity that the shooter should apply in order to reach the target.
     * The function created by running an experiment of different velocities for the "most used" distances.
     * See the experiment results in (TODO: Add the path).
     *
     * @param distance the distance from the target.
     * @return the velocity that should be applied by the shooter in order to reach the target.[RPS]
     */
    public double estimateVelocity(double distance) {
        throw new UnsupportedOperationException("The function estimateVelocity not implemented yet");
    }

    /**
     * Get whether the flywheel has reached the desired velocity in order to reach the target.
     * Also, this function checks whether the flywheel has enough velocity in order to move the motor in first place.
     *
     * @param desiredVelocity the desired velocity at the motor will rotate. [RPS]
     * @return whether the flywheel reaches the desired velocity.
     */
    public boolean isReady(double desiredVelocity) {
        return Math.abs(getVelocity() - desiredVelocity) < Constants.Shooter.VELOCITY_TOLERANCE && getVelocity() > Constants.Shooter.MINIMAL_VELOCITY;
    }

    /**
     * Stop the shooter.
     */
    public void stop() {
        setPower(0);
    }
}
