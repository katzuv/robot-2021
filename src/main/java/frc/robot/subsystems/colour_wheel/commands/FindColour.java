package frc.robot.subsystems.colour_wheel.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.colour_wheel.ColourWheel;

/**
 * Spin the wheel until you reach the desired color.
 */
public class FindColour extends CommandBase {
    private ColourWheel colourWheel;
    private String colour;
    private double power;
    private String tempColour;
    private String[] colours;
    int finalDis;
    int tempDis;
    boolean flag = false;

    public FindColour(ColourWheel colourWheel, String colour, double power) {
        this.colourWheel = colourWheel;
        this.colour = colour;
        this.power = power;
        colours = new String[]{"YELLOW", "BLUE", "GREEN", "RED"};
        addRequirements(colourWheel);
    }

    @Override
    public void initialize() {
        tempColour = colourWheel.getColorString();
        colourWheel.updateSensor();
        int targetIndex = 0, currentIndex = 0;
        for (int i = 0; i < colours.length; i++) {
            if (colours[i].equals(colour))
                targetIndex = i;
            if (colours[i].equals(tempColour))
                currentIndex = i;
        }
        int clockDis = 0, antiDis = 0;
        if (targetIndex < currentIndex) {
            clockDis = targetIndex + colours.length - currentIndex;
            antiDis = currentIndex - targetIndex;
        } else {
            clockDis = targetIndex - currentIndex;
            antiDis = colours.length - targetIndex + currentIndex;
        }
        if (clockDis < antiDis) {
            colourWheel.power(power);
            finalDis = clockDis;
        } else {
            colourWheel.power(-power);
            finalDis = antiDis;
        }
    }

    @Override
    public void execute() {
        colourWheel.updateSensor();
        if (colourWheel.getColorString() != tempColour) {
            tempColour = colourWheel.getColorString();
            tempDis--;
            flag = true;
        }
        if (flag) {
            colourWheel.power(power - 0.1 * (finalDis - tempDis));
        }
    }

    @Override
    public void end(boolean interrupted) {
        colourWheel.power(0);
    }

    @Override
    public boolean isFinished() {
        return colourWheel.getColorString().equals(colour);
    }
}
