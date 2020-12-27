package frc.robot;

import edu.wpi.first.wpilibj.Joystick;

public class OI {

    public static Joystick rightJoystick = new Joystick(0);

    /**
     * @return the X value of the joystick
     */
    public static double getJoystickX() {
        return rightJoystick.getX();
    }

    /**
     * @return the Y value of the joystick
     */
    public static double getJoystickY() {
        return rightJoystick.getY();
    }

    /**
     * @return the Z value of the joystick
     */
    public static double getJoystickZ() {
        return rightJoystick.getZ();
    }
}
