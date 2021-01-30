/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.util.Units;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants.  This class should not be used for any other purpose.  All constants should be
 * declared globally (i.e. public static).  Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
    public static final double LOOP_PERIOD = 0.02; // [s]
    public static final double NOMINAL_VOLTAGE = 12; // [volts]

    //TODO: change to real values
    public static final class Shooter {
        public static final int TICKS_PER_ROTATION = 4096;

        // NOTE: these are the only constants you need to change.
        // TODO: Calibrate
        public static final double VELOCITY_TOLERANCE = 3; // [RPS]
        public static final double MODEL_TOLERANCE = 3;
        public static final double ENCODER_TOLERANCE = 0.1; // [ticks]
        public static final double J = 0; //moment of inertia [kg * m^2]
        public static final double ARBITRARY_FEED_FORWARD = 0;


        // TODO: Note that we might need to change the values here using the frc-characterizations if the model won't satisfy our needs.
        public static final double STALL_CURRENT = 257; // [amps]
        public static final double STALL_TORQUE = 4.69; // [N*meters]

        public static final double FREE_CURRENT = 1.5; // [amps]
        public static final double FREE_SPEED = Units.rotationsPerMinuteToRadiansPerSecond(6380); // [rad/sec]

        public static final double GEAR_RATIO = 0; //TODO: Choose real value.
        public static final double kT = STALL_TORQUE / STALL_CURRENT;// took from FRC examples.
        public static final double OMEGA = NOMINAL_VOLTAGE / STALL_CURRENT; // [Ohm]
        public static final double kV = FREE_SPEED / (NOMINAL_VOLTAGE - OMEGA * FREE_CURRENT);// took from FRC examples.

        public static final String PATH_TO_CSV = "/Shooting.csv";

        public static final double ALLOWED_ERROR = 0.03;// [m]
    }
}
