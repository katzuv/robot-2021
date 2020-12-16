package frc.robot.subsystems.colour_wheel.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.colour_wheel.ColourWheel;

/**
 * Spin the wheel 3 times.
 */
public class Spin extends CommandBase {

    private final ColourWheel colourWheel;
    private double power;
    private String initColour;
    private int spinCount;
    boolean flag = true;
    boolean flag2 = true;

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
            flag = true;
        else {
            if (flag) {
                flag = false;
                spinCount++;
            }
        }
        if (spinCount == 5 && flag2) {
            colourWheel.power(0.5 * power);
            flag2 = false;
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
