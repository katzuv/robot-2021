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
    private String[] colours = new String[]{"YELLOW", "BLUE", "GREEN", "RED"};
    private String tempColour;
    private int resultColorDis;//Amount of colors away from initial color to target color.
    private int previousColor;
    private boolean isNewColorSeen = false;

    public FindColour(ColourWheel colourWheel, String colour, double power) {
        this.colourWheel = colourWheel;
        this.colour = colour;
        this.power = power;
        addRequirements(colourWheel);
    }

    @Override
    public void initialize() {
        colourWheel.updateSensor();
        tempColour = colourWheel.getColorString();
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
            resultColorDis = clockDis;
        } else {
            colourWheel.power(-power);
            resultColorDis = antiDis;
        }
    }

    @Override
    public void execute() {
        colourWheel.updateSensor();
        if (colourWheel.getColorString() != tempColour) {
            tempColour = colourWheel.getColorString();
            previousColor--;
            isNewColorSeen = true;
        }
        if (isNewColorSeen) {
            colourWheel.power(power - 0.1 * (resultColorDis - previousColor));
            isNewColorSeen = false;
        }
    }

    @Override
    public boolean isFinished() {
        return colourWheel.getColorString().equals(colour);
    }

    @Override
    public void end(boolean interrupted) {
        colourWheel.power(0);
    }
}
