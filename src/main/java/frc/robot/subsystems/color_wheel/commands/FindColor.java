package frc.robot.subsystems.color_wheel.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.color_wheel.ColorWheel;

/**
 * Spin the wheel until you reach the desired color.
 */
public class FindColor extends CommandBase {
    private ColorWheel colorWheel;
    private String color;
    private double power;
    private String[] colors = new String[]{"YELLOW", "BLUE", "GREEN", "RED"};
    private String tempColor;
    private int resultColorDis;//Amount of colors away from initial color to target color.
    private int previousColor;
    private boolean isNewColorSeen = false;

    public FindColor(ColorWheel colorWheel, String color, double power) {
        this.colorWheel = colorWheel;
        this.color = color;
        this.power = power;
        addRequirements(colorWheel);
    }

    @Override
    public void initialize() {
        colorWheel.updateSensor();
        tempColor = colorWheel.getColorString();
        int targetIndex = 0, currentIndex = 0;
        for (int i = 0; i < colors.length; i++) {
            if (colors[i].equals(color))
                targetIndex = i;
            if (colors[i].equals(tempColor))
                currentIndex = i;
        }
        int clockDis = 0, antiDis = 0;
        if (targetIndex < currentIndex) {
            clockDis = targetIndex + colors.length - currentIndex;
            antiDis = currentIndex - targetIndex;
        } else {
            clockDis = targetIndex - currentIndex;
            antiDis = colors.length - targetIndex + currentIndex;
        }
        if (clockDis < antiDis) {
            colorWheel.power(power);
            resultColorDis = clockDis;
        } else {
            colorWheel.power(-power);
            resultColorDis = antiDis;
        }
    }

    @Override
    public void execute() {
        colorWheel.updateSensor();
        if (colorWheel.getColorString() != tempColor) {
            tempColor = colorWheel.getColorString();
            previousColor--;
            isNewColorSeen = true;
        }
        if (isNewColorSeen) {
            colorWheel.power(power - 0.1 * (resultColorDis - previousColor));
            isNewColorSeen = false;
        }
    }

    @Override
    public boolean isFinished() {
        return colorWheel.getColorString().equals(color);
    }

    @Override
    public void end(boolean interrupted) {
        colorWheel.power(0);
    }
}
