package frc.robot.subsystems.climber;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Ports;
import frc.robot.subsystems.UnitModel;

public class Climber extends SubsystemBase {
    private final TalonFX master = new TalonFX(Ports.Climber.MASTER);
    private final TalonFX slave = new TalonFX(Ports.Climber.SLAVE);
    private final UnitModel unitModel = new UnitModel(Constants.Climber.TICKS_PER_METER);
    private final Solenoid stopper = new Solenoid(Ports.Climber.STOPPER);
    private final Solenoid gearboxShifter = new Solenoid(Ports.Climber.GEARBOX_SHIFTER);

    public Climber() {
        slave.follow(master);

        master.setInverted(Ports.Climber.IS_MASTER_INVERTED);
        slave.setInverted(Ports.Climber.IS_SLAVE_INVERTED);

        master.setSensorPhase(Ports.Climber.IS_SENSOR_PHASE_INVERTED);

        master.configMotionCruiseVelocity(Constants.Climber.CRUISE_VELOCITY, Constants.TALON_TIMEOUT);
        master.configMotionAcceleration(Constants.Climber.ACCELERATION, Constants.TALON_TIMEOUT);

        master.config_kP(0, Constants.Climber.KP, Constants.TALON_TIMEOUT);
        master.config_kI(0, Constants.Climber.KI, Constants.TALON_TIMEOUT);
        master.config_kD(0, Constants.Climber.KD, Constants.TALON_TIMEOUT);
        master.config_kF(0, Constants.Climber.KF, Constants.TALON_TIMEOUT);
    }

    /**
     * Get the climber's elevation relative to the ground.
     *
     * @return the climber's height [m].
     */
    public double getHeight() {
        return unitModel.toUnits(master.getSelectedSensorPosition());
    }

    /**
     * climb height meters up with the robot.
     *
     * @param height requested height to climb [m].
     */
    public void setHeight(double height) {
        master.set(ControlMode.MotionMagic, unitModel.toTicks(height));
    }

    /**
     * Get whether the stopper is engaged.
     *
     * @return whether the stopper is engaged.
     */
    public boolean isStoppedEngaged() {
        return stopper.get();
    }

    /**
     * Get whether the gearbox shifter is engaged.
     *
     * @return whether the gearbox shifter is engaged.
     */
    public boolean isGearboxEngaged() {
        return gearboxShifter.get();
    }

    /**
     * Set the stopper mode.
     *
     * @param mode the stopper mode.
     */
    public void setStopperMode(PistonMode mode) {
        stopper.set(mode.getValue());
    }

    /**
     * Set the gearbox shifter mode to a given mode.
     *
     * @param mode the wanted gearbox shifter mode.
     */
    public void setGearboxMode(PistonMode mode) {
        gearboxShifter.set(mode.getValue());
    }

    /**
     * Enum for piston mode, OPEN is true, CLOSED is false.
     */
    public enum PistonMode {
        OPEN(true), CLOSED(false);
        final boolean on;

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

    /**
     * Toggle the piston mode of the piston responsible for the gearbox.
     */
    public void toggleGear() {
        gearboxShifter.set(!gearboxShifter.get());
    }

    /**
     * Toggle the piston mode of the piston responsible for the stopper.
     */
    public void toggleStopper() {
        stopper.set(!stopper.get());
    }

}
