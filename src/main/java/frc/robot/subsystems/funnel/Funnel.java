package frc.robot.subsystems.funnel;

import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Ports;

public class Funnel extends SubsystemBase {
    private Victor motor = new Victor(Ports.Funnel.MOTOR);

    public Funnel() {
        motor.setInverted(Ports.Funnel.IS_INVERTED);
    }

    public void setVelocity(double velocity) {
        motor.set(velocity);
    }
}
