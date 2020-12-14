package frc.robot.subsystems;

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

public class ColourWheel extends SubsystemBase {

    private final TalonSRX motor = new TalonSRX(Ports.ColourWheel.MOTOR);

    private final I2C.Port port = I2C.Port.kOnboard;

    private final ColorSensorV3 colorSensorV3 = new ColorSensorV3(port);

    private final ColorMatch colorMatch = new ColorMatch();

    private String colorString = " ";

    private final Color RedTarget = ColorMatch.makeColor(Constants.ColourWheel.RED[0], Constants.ColourWheel.RED[1], Constants.ColourWheel.RED[2]);
    private final Color GreenTarget = ColorMatch.makeColor(Constants.ColourWheel.GREEN[0], Constants.ColourWheel.GREEN[1], Constants.ColourWheel.GREEN[2]);
    private final Color BlueTarget = ColorMatch.makeColor(Constants.ColourWheel.BLUE[0], Constants.ColourWheel.BLUE[1], Constants.ColourWheel.BLUE[2]);
    private final Color YellowTarget = ColorMatch.makeColor(Constants.ColourWheel.YELLOW[0], Constants.ColourWheel.YELLOW[1], Constants.ColourWheel.YELLOW[2]);

    private ColorMatchResult matchResult;

    public ColourWheel() {
        motor.setInverted(Ports.ColourWheel.MOTOR_INVERTED);
        motor.setSensorPhase(Ports.ColourWheel.MOTOR_SENSOR_PHASE_INVERTED);
        motor.config_kP(0, Constants.ColourWheel.kP);
        motor.config_kI(0, Constants.ColourWheel.kI);
        motor.config_kD(0, Constants.ColourWheel.kD);
        colorMatch.addColorMatch(RedTarget);
        colorMatch.addColorMatch(GreenTarget);
        colorMatch.addColorMatch(BlueTarget);
        colorMatch.addColorMatch(YellowTarget);
    }

    public void setPower(double percent) {
        motor.set(ControlMode.PercentOutput, percent);
    }

    public String getColorString() {
        return colorString;
    }

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


    public void updateSensor() {
        matchResult = colorMatch.matchClosestColor(colorSensorV3.getColor());
        colorString = colorToString();
    }

}

