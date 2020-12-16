package frc.robot.subsystems.colour_wheel.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.colour_wheel.ColourWheel;

/**
 * Spin the wheel 3 times.
 */
public class Spin extends CommandBase {

    private final ColourWheel colourWheel;
    private double power;
    private String initColour;//The first color the sensor sees.
    private int spinCount;//Amount of times the sensor has seen the first color.
    boolean isDifferentColor = true;//Whether the sensor has seen a different color than the initial color.
    boolean isPowerDecreased = true;//Whether the power decrease has been executed already.

    public Spin(ColourWheel colourWheel, double power) {
        this.colourWheel = colourWheel;
        this.power = power;
        addRequirements(colourWheel);
    }

    @Override
    public void initialize() {
        colourWheel.updateSensor();
        initColour = colourWheel.getColorString();
        spinCount = 0;
        colourWheel.power(power);
    }

    @Override
    public void execute() {
        colourWheel.updateSensor();
        if (!colourWheel.getColorString().equals(initColour))
            isDifferentColor = true;
        else {
            if (isDifferentColor) {
                isDifferentColor = false;
                spinCount++;
            }
        }
        if (spinCount == 5 && isPowerDecreased) {
            colourWheel.power(0.5 * power);
            isPowerDecreased = false;
        }
    }

    @Override
    public boolean isFinished() {
        return spinCount == 6;
    }

    @Override
    public void end(boolean interrupted) {
        colourWheel.power(0);
    }
}
