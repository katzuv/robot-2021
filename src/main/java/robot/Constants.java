package robot;

/**
 * A class holding all of the constants of every mechanism on the robot.
 * Place global constants in this class, and mechanism-specific constants inside their respective mechanism subclass.
 * When accessing a mechanism-specific port, call Constants.[MECHANISM].[CONSTANT]
 */
public class Constants {
    //All general constants go here
    //public static final double TIME_STEP = 0.02;

    public static final int TALON_TIMEOUT = 10;

    public static class Drivetrain {
        public static final double TICKS_PER_METER = 256 / (4 * 0.0254 * Math.PI);
        public static final double TICKS_PER_DEGREE = 1; // TODO: change to real value

        public static final double MAX_VEL = 3;// in m/s
        public static final double TIME_STEP = 0.02;
        public static final double MAX_ACCELERATION = 0.4;// in m/s^2 (currently not the correct number)
        public static final int MAX_CURRENT = 35;
    }

    public static class SwerveDrive {
        // TODO: not actual measurements and constants
        public static final double ROBOT_LENGTH = 0;
        public static final double ROBOT_WIDTH = 0;

        // the speed of the robot, this constant multiplies the speed outputs from the joysticks
        public static final double SPEED_MULTIPLIER = 0.7;

        // the rotational speed of the robot, this constant multiplies the rotation output of the joystick
        public static final double ROTATION_MULTIPLIER = 2 * Math.PI;

        public static final double JOYSTICK_THRESHOLD = 0.05;
    }

    public static class SwerveModule {
        // TODO: set PIDF
        public static final double KP = 0;
        public static final double KI = 0;
        public static final double KD = 0;
        public static final double KF = 0;
    }



    public static class ExampleSubsystem1 {
        //All of the Subsystem specific constants go here,and need to be static.

        //public static final double TICKS_PER_METER = 256 / (4*0.0254*Math.PI);
        //public static final double MAX_VELOCITY = 5;
    }
}