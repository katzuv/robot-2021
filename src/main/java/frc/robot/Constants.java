/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants.  This class should not be used for any other purpose.  All constants should be
 * declared globally (i.e. public static).  Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
    public static final int TALON_TIMEOUT = 10; // [ms]
    public static final double ROBOT_TIMEOUT = 0.02; // [s]

    //TODO: change to real values
    public static final class Flywheel {
        public static final int TICKS_PER_ROTATION = 0;

        public static final double VELOCITY_TOLERANCE = 0; //[rps]
        public static final double MODEL_TOLERANCE = 0;
        public static final double ENCODER_TOLERANCE = 0; //[ticks]
        public static final double MINIMAL_VELOCITY = 0;//[rps] the minimal velocity where the wheel would actually move

        public static final double[] PIDF = {0, 0, 0, 0};

        public static final double G = 0; //Gear Ratio
        public static final double Kt = 0;
        public static final double Kv = 0;
        public static final double RADIUS = 0;
        public static final double J = 0; //moment of inertia
    }
}
