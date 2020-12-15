package frc.robot.subsystems.colour_wheel.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.colour_wheel.ColourWheel;

public class Spin extends CommandBase {

    private final ColourWheel colourWheel;
    private double power;
    private String initColour;
    private int spinCount;

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
        colourWheel.setPower(power);
    }

    @Override
    public void execute() {
        colourWheel.updateSensor();
        boolean flag = true;
        if (!colourWheel.getColorString().equals(initColour))
            flag = false;
        else {
            if (!flag) {
                flag = true;
                spinCount++;
            }
        }
        if (spinCount == 5)
            colourWheel.setPower(0.5 * power);
    }

    @Override
    public boolean isFinished() {
        return spinCount == 6;
    }

    @Override
    public void end(boolean interrupted) {
        colourWheel.setPower(0);
    }
}
