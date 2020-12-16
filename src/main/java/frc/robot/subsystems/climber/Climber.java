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

        master.setSensorPhase(Ports.Climber.IS_MASTER_SENSOR_PHASE_INVERTED);
        slave.setSensorPhase(Ports.Climber.IS_SLAVE_SENSOR_PHASE_INVERTED);

        master.config_kP(0, Constants.Climber.kP, Constants.TALON_TIMEOUT);
        master.config_kI(0, Constants.Climber.kI, Constants.TALON_TIMEOUT);
        master.config_kD(0, Constants.Climber.kD, Constants.TALON_TIMEOUT);


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
     * Climb height meters up with the robot.
     *
     * @param height requested height to climb [m].
     */
    public void setHeight(double height) {
        master.set(ControlMode.MotionMagic, unitModel.toTicks(height));
    }

    /**
     * Get whether or not the stopper is engaged.
     *
     * @return whether or not the stopper is engaged.
     */
    public boolean isStoppedEngaged() {
        return stopper.get();
    }

    /**
     * Get whether or not the gearbox shifter is engaged.
     *
     * @return whether or not the gearbox shifter is engaged.
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

    public void toggleGear() {
        gearboxShifter.set(!gearboxShifter.get());
    }

    public void toggleStopper() {
        stopper.set(!stopper.get());
    }

}
