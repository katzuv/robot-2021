package frc.robot.subsystems.color_wheel;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.ColorMatch;
import com.revrobotics.ColorMatchResult;
import com.revrobotics.ColorSensorV3;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Ports;

public class ColorWheel extends SubsystemBase {

    private final TalonSRX motor = new TalonSRX(Ports.ColorWheel.MOTOR);

    private final I2C.Port port = I2C.Port.kOnboard;

    private final ColorSensorV3 colorSensorV3 = new ColorSensorV3(port);

    private final ColorMatch colorMatch = new ColorMatch();

    private String colorString = " ";

    private final Color RedTarget = ColorMatch.makeColor(Constants.ColorWheel.RED[0], Constants.ColorWheel.RED[1], Constants.ColorWheel.RED[2]);
    private final Color GreenTarget = ColorMatch.makeColor(Constants.ColorWheel.GREEN[0], Constants.ColorWheel.GREEN[1], Constants.ColorWheel.GREEN[2]);
    private final Color BlueTarget = ColorMatch.makeColor(Constants.ColorWheel.BLUE[0], Constants.ColorWheel.BLUE[1], Constants.ColorWheel.BLUE[2]);
    private final Color YellowTarget = ColorMatch.makeColor(Constants.ColorWheel.YELLOW[0], Constants.ColorWheel.YELLOW[1], Constants.ColorWheel.YELLOW[2]);

    private ColorMatchResult matchResult;

    public ColorWheel() {
        motor.setInverted(Ports.ColorWheel.MOTOR_INVERTED);
        motor.setSensorPhase(Ports.ColorWheel.MOTOR_SENSOR_PHASE_INVERTED);

        motor.config_kP(0, Constants.ColorWheel.kP, Constants.TALON_TIMEOUT);
        motor.config_kI(0, Constants.ColorWheel.kI, Constants.TALON_TIMEOUT);
        motor.config_kD(0, Constants.ColorWheel.kD, Constants.TALON_TIMEOUT);

        colorMatch.addColorMatch(RedTarget);
        colorMatch.addColorMatch(GreenTarget);
        colorMatch.addColorMatch(BlueTarget);
        colorMatch.addColorMatch(YellowTarget);
    }

    /**
     * Set power for the Color Wheel.
     *
     * @param percent designated percentages for the Color Wheel's power.
     */
    public void power(double percent) {
        motor.set(ControlMode.PercentOutput, percent);
    }

    /**
     * Convert the color seen by the color sensor to String.
     *
     * @return the color seen by the color sensor in String.
     */
    public String colorToString() {
        if (RedTarget.equals(matchResult.color)) {
            return "RED";
        } else if (GreenTarget.equals(matchResult.color)) {
            return "GREEN";
        } else if (BlueTarget.equals(matchResult.color)) {
            return "BLUE";
        } else if (YellowTarget.equals(matchResult.color)) {
            return "YELLOW";
        } else {
            return "UNKNOWN";
        }
    }

    /**
     * Match the color seen by the color sensor to a color from the list and set private String colorString to the String of the color.
     */
    public void updateSensor() {
        matchResult = colorMatch.matchClosestColor(colorSensorV3.getColor());
        colorString = colorToString();
    }

    /**
     * Get the color of the closest color in String.
     *
     * @return the color of the closest color in String.
     */
    public String getColorString() {
        return colorString;
    }


}

