package frc.robot.subsystems.shooter;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.LimitSwitchNormal;
import com.ctre.phoenix.motorcontrol.LimitSwitchSource;
import com.ctre.phoenix.motorcontrol.NeutralMode;
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
import frc.robot.subsystems.UnitModel;

import static frc.robot.Constants.Shooter.*;

/**
 * The flywheel module class represents one wheel of the flywheel subsystem, and the class itself is a subsystem.
 * In case when you have a single-wheel flywheel you can (and should) use this class as the subsystem of the flywheel.
 * The purpose of the flywheel is to provide a set of functions to handle a shooting situation in a game.
 *
 * @author Barel Shlidor
 * @version 1.0
 * @using TalonFX
 * @since 2021
 */
public class FlywheelModule extends SubsystemBase {
    private final TalonFX motor;
    private final UnitModel rpsUnitModel = new UnitModel(Constants.Shooter.TICKS_PER_ROTATION);
    private final LinearSystemLoop<N1, N1, N1> stateSpacePredictor;

    /**
     * Initialize the Flywheel module with default values.
     *
     * @param port        the port of the motor.
     * @param inverted    whether the motor is inverted.
     * @param sensorPhase whether the (internal) sensor is inverted
     */
    public FlywheelModule(int port, boolean inverted, boolean sensorPhase) {
        motor = new TalonFX(port);

        motor.setInverted(inverted);
        motor.setSensorPhase(sensorPhase);

        motor.setNeutralMode(NeutralMode.Coast);

        motor.configForwardLimitSwitchSource(LimitSwitchSource.Deactivated, LimitSwitchNormal.Disabled);
        motor.configReverseLimitSwitchSource(LimitSwitchSource.Deactivated, LimitSwitchNormal.Disabled);

        motor.enableVoltageCompensation(true);
        motor.configVoltageCompSaturation(12);

        Vector<N1> A = VecBuilder.fill(-Math.pow(G, 2) * Kt / (Kv * OMEGA * J)); //Change the amount of cells and rows
        Vector<N1> B = VecBuilder.fill(G * Kt / (OMEGA * J));
        LinearSystem<N1, N1, N1> stateSpace = new LinearSystem<>(A, B, Matrix.eye(Nat.N1()), new Matrix<>(Nat.N1(), Nat.N1()));
        KalmanFilter<N1, N1, N1> kalman = new KalmanFilter<>(Nat.N1(), Nat.N1(), stateSpace,
                VecBuilder.fill(MODEL_TOLERANCE),
                VecBuilder.fill(ENCODER_TOLERANCE),
                Constants.ROBOT_TIMEOUT
        );
        LinearQuadraticRegulator<N1, N1, N1> lqr = new LinearQuadraticRegulator<>(stateSpace, VecBuilder.fill(VELOCITY_TOLERANCE),
                VecBuilder.fill(12), // voltage
                Constants.ROBOT_TIMEOUT // time between loops, DON'T CHANGE
        );
        this.stateSpacePredictor = new LinearSystemLoop<>(stateSpace, lqr, kalman, 12, Constants.ROBOT_TIMEOUT); // the last two are the voltage, and the time between loops

    }

    /**
     * Get the velocity of the motor in rps.
     *
     * @return the velocity applied by the motor. [rps]
     * @see #setVelocity(double)
     */
    public double getVelocity() {
        return rpsUnitModel.toVelocity(motor.getSelectedSensorVelocity());
    }

    /**
     * Set the velocity to apply by the motor.
     *
     * @param velocity the desired velocity at which the motor will rotate. [rps]
     * @see #setPower(double)
     */
    public void setVelocity(double velocity) {
        stateSpacePredictor.setNextR(VecBuilder.fill(velocity)); //r = reference
        stateSpacePredictor.correct(VecBuilder.fill(getVelocity()));
        stateSpacePredictor.predict(Constants.ROBOT_TIMEOUT); //every 20 ms

        double voltage = stateSpacePredictor.getU(0); // u = input, calculated by the input.
        // returns the voltage to apply (between 0 and 12)

        System.out.println("Voltage: " + voltage);
        System.out.println("Percentage: " + (voltage / 12));
        setPower(voltage / 12); // map to be between 0 and 1

//        motor.set(ControlMode.Velocity, rpsUnitModel.toTicks100ms(velocity));
    }

    /**
     * Set the power to apply by the motor.
     *
     * @param power the power at which the motor will rotate. [percentage, between 0 and 1]
     * @see #stop()
     */
    public void setPower(double power) {
        motor.set(ControlMode.PercentOutput, power);
    }

    /**
     * Get whether the flywheel has reached the desired velocity in order to reach the target.
     * Also, we check whether the flywheel has enough velocity in order to move the motor in first place.
     *
     * @param desiredVelocity the desired velocity at the motor will rotate. [rps]
     * @return whether the flywheel reaches the desired velocity.
     */
    public boolean isReady(double desiredVelocity) {
        return Math.abs(getVelocity() - desiredVelocity) < Constants.Shooter.VELOCITY_TOLERANCE && getVelocity() > Constants.Shooter.MINIMAL_VELOCITY;
    }

    /**
     * Stop the motor from moving.
     */
    public void stop() {
        setPower(0);
    }

    /**
     * Set the control mode and output value so that this motor controller will
     * follow another motor controller.
     *
     * @param module Motor Controller to follow.
     */
    public void follow(FlywheelModule module) {
        motor.follow(module.motor);
    }
}
