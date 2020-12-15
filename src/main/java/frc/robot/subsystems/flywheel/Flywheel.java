package frc.robot.subsystems.flywheel;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

import static frc.robot.Constants.Flywheel.*;
import static frc.robot.Ports.Flywheel.*;

public class Flywheel extends SubsystemBase {
    private final FlywheelModule[] flywheelModules = new FlywheelModule[2]; // Maybe the quantity will change

    public Flywheel() {
        flywheelModules[0] = new FlywheelModule(MOTOR_1, MOTOR_1_INVERTED, MOTOR_1_SENSOR_INVERTED);
        flywheelModules[1] = new FlywheelModule(MOTOR_2, MOTOR_2_INVERTED, MOTOR_2_SENSOR_INVERTED);

    }

}
