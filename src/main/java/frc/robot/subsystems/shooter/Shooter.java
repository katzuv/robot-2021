package frc.robot.subsystems.shooter;

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

import static frc.robot.Constants.Shooter.*;
import static frc.robot.Ports.Shooter.*;

public class Shooter extends SubsystemBase {
    private final FlywheelModule main = new FlywheelModule(MOTOR_1, MOTOR_1_INVERTED, MOTOR_1_SENSOR_INVERTED);
    private final FlywheelModule aux = new FlywheelModule(MOTOR_2, MOTOR_2_INVERTED, MOTOR_2_SENSOR_INVERTED);
    private final LinearSystemLoop<N1, N1, N1> stateSpacePredictor;

    public Shooter() {
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
        aux.follow(main);
    }

    public void setVelocity(double velocity) {
        stateSpacePredictor.setNextR(VecBuilder.fill(velocity)); //r = reference
        stateSpacePredictor.correct(VecBuilder.fill(main.getVelocity()));
        stateSpacePredictor.predict(Constants.ROBOT_TIMEOUT); //every 20 ms

        double voltage = stateSpacePredictor.getU(0); // u = input, calculated by the input.
        // returns the voltage to apply (between 0 and 12)
        setPower(voltage / 12); // map to be between 0 and 1
    }

    public void setPower(double power) {
        main.setPower(power);
    }

    public double estimateVelocity(double distance) {
        return distance;
    }

    public double getVelocity() {
        return main.getVelocity();
    }

    public boolean isReady(double desiredVelocity) {
        return main.isReady(desiredVelocity);
    }

    public void stop() {
        main.stop();
    }
}
