package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.ColorMatch;
import com.revrobotics.ColorSensorV3;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import frc.robot.Constants;
import frc.robot.Ports;

import javax.sound.sampled.Port;
import java.awt.*;

public class ColourWheel extends SubsystemBase {

    private final TalonSRX motor = new TalonSRX(Ports.ColourWheel.MOTOR);

    private final I2C.Port port = I2C.Port.kOnboard;

    private final ColorSensorV3 colorSensorV3 = new ColorSensorV3(port);

    private final ColorMatch colorMatch = new ColorMatch();

    private final Color RedTarget = new Color(Constants.ColourWheel.RED[0], Constants.ColourWheel.RED[1], Constants.ColourWheel.RED[2]);
    private final Color GreenTarget = new Color(Constants.ColourWheel.GREEN[0], Constants.ColourWheel.GREEN[1], Constants.ColourWheel.GREEN[2]);
    private final Color BlueTarget = new Color(Constants.ColourWheel.BLUE[0], Constants.ColourWheel.BLUE[1], Constants.ColourWheel.BLUE[2]);
    private final Color YellowTarget = new Color(Constants.ColourWheel.YELLOW[0], Constants.ColourWheel.YELLOW[1], Constants.ColourWheel.YELLOW[2]);


}
