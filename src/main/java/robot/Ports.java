package robot;

/**
 * A class holding all of the ports on the robot.
 * Place mechanism-specific ports inside their own sub-class.
 * When accessing a mechanism-specific port, call Ports.[MECHANISM].[PORT_NAME]
 */
public class Ports {
    public static class ExampleSubsystem1 {
        //public static int TALON_PORT = 1;
    }

    public static class SwerveDrive {
        public static final int frontRightDrive = 11;
        public static final int frontRightAngle = 12;
        public static final int frontLeftDrive = 21;
        public static final int frontLeftAngle = 22;
        public static final int backRightDrive = 31;
        public static final int backRightAngle = 32;
        public static final int backLeftDrive = 41;
        public static final int backLeftAngle = 42;
    }

}
