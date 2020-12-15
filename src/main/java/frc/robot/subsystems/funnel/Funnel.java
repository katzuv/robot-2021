package frc.robot.subsystems.funnel;

import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Ports;

/**
 * this subsystem transfers balls from intake to conveyor
 */
public class Funnel extends SubsystemBase{
    private Victor motor = new Victor(Ports.Funnel.MOTOR);

    public Funnel() {
        motor.setInverted(Ports.Funnel.IS_INVERTED);
    }

    /**
     * @param velocity - the target velocity (m/s)
     * this function sets the motor's velocity
     */
    public void setVelocity(double velocity) {
        motor.set(velocity);
    }
}
