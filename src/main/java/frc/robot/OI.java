package frc.robot;

import edu.wpi.first.wpilibj.Joystick;

public class OI {

    public static Joystick joystick = new Joystick(1);

    /**
     * @return the X value of the joystick
     */
    public static double getJoystickX() {
        return joystick.getX();
    }

    /**
     * @return the Y value of the joystick
     */
    public static double getJoystickY() {
        return joystick.getY();
    }

    /**
     * @return the Z value of the joystick
     */
    public static double getJoystickZ() {
        return joystick.getZ();
    }
}
