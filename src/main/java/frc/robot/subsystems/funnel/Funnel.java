package frc.robot.subsystems.funnel;

import edu.wpi.first.wpilibj.Victor;
import frc.robot.Ports;

public class Funnel {
    private Victor motor = new Victor(Ports.Funnel.MOTOR);

    public Funnel(){
        motor.setInverted(Ports.Funnel.IS_INVERTED);
    }
}
