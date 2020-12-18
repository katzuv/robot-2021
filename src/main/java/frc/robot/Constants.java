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
    public static final int TALON_TIMEOUT = 10; // [ms]
    public static final double ROBOT_TIMEOUT = 0.02; // [s]

    //TODO: change to real values
    public static final class Shooter {
        public static final int TICKS_PER_ROTATION = 4096;

        public static final double VELOCITY_TOLERANCE = 3; //[rps]
        public static final double MODEL_TOLERANCE = 3;
        public static final double ENCODER_TOLERANCE = 0.1; //[ticks]
        public static final double MINIMAL_VELOCITY = 2;//[rps] the minimal velocity where the wheel would actually move

        public static final double NOMINAL_VOLTAGE_VOLTS = 12;
        public static final double STALL_CURRENT_AMPS = 134;
        public static final double STALL_TORQUE_NEWTON_METERS = 0.71;

        public static final double FREE_CURRENT_AMPS = 0.7;
        public static final double FREE_SPEED_RAD_PER_SEC = Units.rotationsPerMinuteToRadiansPerSecond(18730);

        public static final double G = 12.0 / 20; //Gear Ratio
        public static final double Kt = STALL_TORQUE_NEWTON_METERS / STALL_CURRENT_AMPS;// took from FRC examples
        public static final double OMEGA = NOMINAL_VOLTAGE_VOLTS / STALL_CURRENT_AMPS; // [Om]
        public static final double Kv = FREE_SPEED_RAD_PER_SEC / (NOMINAL_VOLTAGE_VOLTS - OMEGA * FREE_CURRENT_AMPS);// took from FRC examples
        public static final double J = 0.00032; //moment of inertia [kg * m^2]
    }
}
