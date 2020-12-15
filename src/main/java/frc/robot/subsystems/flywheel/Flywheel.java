package frc.robot.subsystems.flywheel;

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

import static frc.robot.Constants.Flywheel.*;
import static frc.robot.Ports.Flywheel.*;

public class Flywheel extends SubsystemBase {
    private final FlywheelModule[] flywheelModules = new FlywheelModule[2]; // Maybe the quantity will change
    private final boolean useStateSpace;

    private final LinearSystemLoop<N1, N1, N1> stateSpacePredictor;

    public Flywheel(boolean useStateSpace) {
        this.useStateSpace = useStateSpace;
        if (!useStateSpace) {
            this.stateSpacePredictor = null;
        } else {
            Vector<N1> A = VecBuilder.fill(-Math.pow(G, 2) * Kt / (Kv * RADIUS * J)); //Change the amount of cells and rows
            Vector<N1> B = VecBuilder.fill(G * Kt / (RADIUS * J));
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

        flywheelModules[0] = new FlywheelModule(MOTOR_1, MOTOR_1_INVERTED, MOTOR_1_SENSOR_INVERTED);
        flywheelModules[1] = new FlywheelModule(MOTOR_2, MOTOR_2_INVERTED, MOTOR_2_SENSOR_INVERTED);
    }

    public void setVelocity(double velocity) {
        if (useStateSpace) {
            stateSpacePredictor.setNextR(VecBuilder.fill(velocity)); //r = reference
            stateSpacePredictor.correct(VecBuilder.fill(flywheelModules[0].getVelocity()));
            stateSpacePredictor.predict(Constants.ROBOT_TIMEOUT); //every 20 ms

            double voltage = stateSpacePredictor.getU(0); // u = input, calculated by the input.
            // returns the voltage to apply (between 0 and 12)
            setPower(voltage / 12); // map to be between 0 and 1
        } else
            for (FlywheelModule module : flywheelModules)
                module.setVelocity(velocity);
    }

    public void setPower(double power) {
        for (FlywheelModule module : flywheelModules) {
            module.setPower(power);
        }
    }

    public void stop() {
        for (FlywheelModule module : flywheelModules) {
            module.stop();
        }
    }
}
