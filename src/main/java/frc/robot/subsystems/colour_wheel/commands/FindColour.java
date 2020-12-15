package frc.robot.subsystems.colour_wheel.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.colour_wheel.ColourWheel;

public class FindColour extends CommandBase {
    private ColourWheel colourWheel;
    private String colour;
    private double power;
    private String tempColour;
    private String[] colours;
    int finalDis;
    int finalTemp;


    public FindColour(ColourWheel colourWheel, String colour, double power) {
        this.colourWheel = colourWheel;
        this.colour = colour;
        this.power = power;
        colours = new String[]{"YELLOW", "BLUE", "GREEN", "RED"};
    }

    @Override
    public void initialize() {
        tempColour = colourWheel.getColorString();
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
            clockDis = targetIndex;
            antiDis = colours.length - targetIndex + currentIndex;
        }
        if (clockDis < antiDis) {
            colourWheel.setPower(power);
            finalDis = clockDis;
        } else {
            colourWheel.setPower(-power);
            finalDis = antiDis;
        }
    }

    @Override
    public void execute() {
        colourWheel.updateSensor();
        if (colourWheel.getColorString() != tempColour) {
            tempColour = colourWheel.getColorString();
            finalTemp--;
        }
        power = power - 0.1 * (finalDis - finalTemp);
    }

    @Override
    public void end(boolean interrupted) {
        colourWheel.setPower(0);
    }

    @Override
    public boolean isFinished() {
        return colourWheel.getColorString().equals(colour);
    }
}
