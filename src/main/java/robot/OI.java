package robot;

import edu.wpi.first.wpilibj.Joystick;

public class OI {

    public static Joystick joystick = new Joystick(1);

    public static double getJoystickX() {
        return joystick.getX();
    }

    public static double getJoystickY() {
        return joystick.getY();
    }

    public static double getJoystickZ() {
        return joystick.getZ();
    }
}
