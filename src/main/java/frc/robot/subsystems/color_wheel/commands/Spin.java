package frc.robot.subsystems.color_wheel.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.color_wheel.ColorWheel;

/**
 * Spin the wheel 3 times.
 */
public class Spin extends CommandBase {

    private final ColorWheel colorWheel;
    private double power;
    private String initColor;//The first color the sensor sees.
    private int spinCount;//Amount of times the sensor has seen the first color.
    boolean isDifferentColor = true;//Whether the sensor has seen a different color than the initial color.
    boolean isPowerDecreased = true;//Whether the power decrease has been executed already.

    public Spin(ColorWheel colorWheel, double power) {
        this.colorWheel = colorWheel;
        this.power = power;
        addRequirements(colorWheel);
    }

    @Override
    public void initialize() {
        colorWheel.updateSensor();
        initColor = colorWheel.getColorString();
        spinCount = 0;
        colorWheel.power(power);
    }

    @Override
    public void execute() {
        colorWheel.updateSensor();
        if (!colorWheel.getColorString().equals(initColor))
            isDifferentColor = true;
        else {
            if (isDifferentColor) {
                isDifferentColor = false;
                spinCount++;
            }
        }
        if (spinCount == 5 && isPowerDecreased) {
            colorWheel.power(0.5 * power);
            isPowerDecreased = false;
        }
    }

    @Override
    public boolean isFinished() {
        return spinCount == 6;
    }

    @Override
    public void end(boolean interrupted) {
        colorWheel.power(0);
    }
}
