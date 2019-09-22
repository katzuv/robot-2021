package robot.subsystems;

public class DrivetrainConstants {
    public static final double TICKS_PER_METER = 256 / (4*0.0254*Math.PI);
    public static final double MAX_VEL = 3;// in m/s
    public static final double TIME_STEP = 0.02;
    public static final double MAX_ACCELERATION = 0.4;// in m/s^2 (currently not the correct number)
    public static final int MAX_CURRENT = 35;
}
