package frc.robot.subsystems.color_wheel.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.subsystems.color_wheel.ColorWheel;

import java.util.ConcurrentModificationException;

/**
 * Spin the wheel 3 times.
 */
public class Spin extends CommandBase {

    private final ColorWheel colorWheel;
    private double power;
    private String initColor;//The first color the sensor sees.
    private int initColorSeenCount;//Amount of times the sensor has seen the first color.
    boolean isDifferentColor = false;//Whether the sensor has seen a different color than the initial color.
    boolean isPowerDecreased = false;//Whether the power decrease has been executed already.

    public Spin(ColorWheel colorWheel, double power) {
        this.colorWheel = colorWheel;
        this.power = power;
        addRequirements(colorWheel);
    }

    @Override
    public void initialize() {
        colorWheel.updateSensor();
        initColor = colorWheel.getColorString();
        initColorSeenCount = 1;
        colorWheel.power(power);
    }

    @Override
    public void execute() {
        colorWheel.updateSensor();
        hasSeenInitColor();
        moderatePower();
    }

    @Override
    public boolean isFinished() {
        return initColorSeenCount == Constants.ColorWheel.REDUCE_POWER_BY;
    }

    @Override
    public void end(boolean interrupted) {
        colorWheel.power(0);
    }

    public void hasSeenInitColor() {
        if (!colorWheel.getColorString().equals(initColor))
            isDifferentColor = true;
        else {
            if (isDifferentColor) {
                isDifferentColor = false;
                initColorSeenCount++;
            }
        }
    }

    public void moderatePower() {
        if (initColorSeenCount == Constants.ColorWheel.MAX_HALF_SPINS - 1 && !isPowerDecreased) {
            colorWheel.power(Constants.ColorWheel.REDUCE_POWER_BY * power);
            isPowerDecreased = true;
        }
    }
}
