package frc.robot.subsystems.color_wheel.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.color_wheel.ColorWheel;

/**
 * Spin the wheel until you reach the desired color.
 */
public class FindColor extends CommandBase {
    private ColorWheel colorWheel;
    private String color;
    private double power;
    private String tempColor;
    private int targetColorDistance; //amount of colors away from initial color to target color.
    private int previousColor;
    private boolean isNewColorSeen = false;
    private int initColorIndex;
    private int targetColorIndex;

    public FindColor(ColorWheel colorWheel, String color, double power) {
        this.colorWheel = colorWheel;
        this.color = color;
        this.power = power;
        addRequirements(colorWheel);
    }

    @Override
    public void initialize() {
        findDistanceToTargetAndSetPower();
    }

    @Override
    public void execute() {
        colorWheel.updateSensor();
        if (!colorWheel.getColorString().equals(tempColor)) {
            tempColor = colorWheel.getColorString();
            previousColor--;
            isNewColorSeen = true;
        }
        if (isNewColorSeen) {
            colorWheel.power(power - 0.1 * (targetColorDistance - previousColor));
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

    public void findDistanceToTargetAndSetPower() {
        colorWheel.updateSensor();
        findInitialAndTargetColorPosition();
        int clockDistance, antiDistance;
        if (targetColorIndex < initColorIndex) {
            clockDistance = targetColorIndex + Constants.ColorWheel.COLORS.length - initColorIndex;
            antiDistance = initColorIndex - targetColorIndex;
        } else {
            clockDistance = targetColorIndex - initColorIndex;
            antiDistance = Constants.ColorWheel.COLORS.length - targetColorIndex + initColorIndex;
        }
        if (clockDistance < antiDistance) {
            colorWheel.power(power);
            targetColorDistance = clockDistance;
        } else {
            colorWheel.power(-power);
            targetColorDistance = antiDistance;
        }
    }

    private void findInitialAndTargetColorPosition() {
        tempColor = colorWheel.getColorString();
        initColorIndex = 0;
        targetColorIndex = 0;
        for (int i = 0; i < Constants.ColorWheel.COLORS.length; i++) {
            if (Constants.ColorWheel.COLORS[i].equals(tempColor))
                initColorIndex = i;
            if (Constants.ColorWheel.COLORS[i].equals(color))
                targetColorIndex = i;
        }
    }
}
