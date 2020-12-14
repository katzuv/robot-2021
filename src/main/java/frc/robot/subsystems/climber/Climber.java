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
    private final Solenoid gearboxPiston = new Solenoid(Ports.Climber.GEARBOX_PISTON);

    public Climber() {
        slave.follow(master);

        master.setInverted(Ports.Climber.MASTER_INVERTED);
        slave.setInverted(Ports.Climber.SLAVE_INVERTED);

        master.setSensorPhase(Ports.Climber.MASTER_SENSOR_PHASE_INVERTED);
        slave.setSensorPhase(Ports.Climber.SLAVE_SENSOR_PHASE_INVERTED);

        master.config_kP(0, Constants.Climber.kP);
        master.config_kI(0, Constants.Climber.kI);
        master.config_kD(0, Constants.Climber.kD);

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
        return unitModel.toUnits(master.getSelectedSensorPosition());
    }

    /**
     * Set the climber's height.
     *
     * @param height height [double].
     */
    public void setHeight(double height) {
        master.set(ControlMode.Position, unitModel.toTicks(height));
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
     * Get the gearboxPiston mode.
     *
     * @return the gearboxPiston mode [PistonMode].
     */
    public PistonMode getGearboxMode() {
        if (gearboxPiston.get())
            return PistonMode.OPEN;
        else
            return PistonMode.CLOSED;
    }

    /**
     * Set the stopper mode.
     *
     * @param mode the stopper mode [boolean].
     */
    public void setStopperMode(PistonMode mode) {
        stopper.set(mode.getValue());
    }

    /**
     * Set the gearboxPiston mode.
     *
     * @param mode the gearboxPiston mode [boolean].
     */
    public void setGearboxMode(PistonMode mode) {
        gearboxPiston.set(mode.getValue());
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

    public void toggleGearboxMode() {
        gearboxPiston.set(!gearboxPiston.get());
    }

    public void toggleStopperMode() {
        stopper.set(!stopper.get());
    }

}
