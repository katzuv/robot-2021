package frc.robot.subsystems.climber;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Ports;
import frc.robot.subsystems.UnitModel;

public class Climber extends SubsystemBase {
    private final TalonFX motor = new TalonFX(Ports.Climber.MOTOR);
    private final TalonFX slave = new TalonFX(Ports.Climber.SLAVE);
    private final UnitModel unitModel = new UnitModel(Constants.Climber.TICKS_PER_METER);
    private final Solenoid stopper = new Solenoid(Ports.Climber.STOPPER);
    private final Solenoid gearbox = new Solenoid(Ports.Climber.GEARBOX);

    public Climber() {
        slave.follow(motor);

        motor.setInverted(Ports.Climber.MOTOR_INVERTED);
        slave.setInverted(Ports.Climber.SLAVE_INVERTED);

        motor.setSensorPhase(Ports.Climber.MOTOR_SENSOR_PHASE_INVERTED);
        slave.setSensorPhase(Ports.Climber.SLAVE_SENSOR_PHASE_INVERTED);

        motor.config_kP(0, Constants.Climber.kP);
        motor.config_kI(0, Constants.Climber.kI);
        motor.config_kD(0, Constants.Climber.kD);

        slave.config_kP(0, Constants.Climber.kP);
        slave.config_kI(0, Constants.Climber.kI);
        slave.config_kD(0, Constants.Climber.kD);
    }

    /**
     * Get the climber's height.
     *
     * @return the climber's height [double].
     */
    public double getHeight() {
        return unitModel.toUnits(motor.getSelectedSensorPosition());
    }

    /**
     * Set the climber's height.
     *
     * @param height height [double].
     */
    public void setHeight(double height) {
        motor.set(ControlMode.Position, unitModel.toTicks(height));
    }

    /**
     * Get the stopper mode.
     *
     * @return the stopper mode [PistonMode].
     */
    public PistonMode getStopperMode() {
        if (stopper.get())
            return PistonMode.OPEN;
        else
            return PistonMode.CLOSED;
    }

    /**
     * Get the gearbox mode.
     *
     * @return the gearbox mode [PistonMode].
     */
    public PistonMode getGearboxMode() {
        if (gearbox.get())
            return PistonMode.OPEN;
        else
            return PistonMode.CLOSED;
    }

    /**
     * Set the stopper mode.
     *
     * @param mode the stopper mode [boolean].
     */
    public void setStopperMode(boolean mode) {
        stopper.set(mode);
    }

    /**
     * Set the gearbox mode.
     *
     * @param mode the gearbox mode [boolean].
     */
    public void setGearboxMode(boolean mode) {
        gearbox.set(mode);
    }

    /**
     * Enum for piston mode, OPEN is true, CLOSED is false.
     */
    public enum PistonMode {
        OPEN(true), CLOSED(false);
        private boolean on;

        PistonMode(boolean on) {
            this.on = on;
        }

        /**
         * Get the value of the piston mode.
         *
         * @return the value of the piston mode [boolean].
         */
        public boolean getValue() {
            return on;
        }
    }

    @Override
    public void periodic() {

    }
}
