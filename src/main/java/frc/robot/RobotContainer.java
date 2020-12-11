
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.Button;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.subsystems.drivetrain.SwerveDrive;
import frc.robot.subsystems.drivetrain.commands.*;
import frc.robot.valuetuner.ValueTuner;
import org.techfire225.webapp.Webserver;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class RobotContainer {
    SendableChooser<Command> m_chooser = new SendableChooser<>();

    public static XboxController xbox = new XboxController(2);
    Button b = new JoystickButton(xbox, 1);
    Button x = new JoystickButton(xbox, 3);
    Button y = new JoystickButton(xbox, 4);
    private JoystickButton a = new JoystickButton(xbox, XboxController.Button.kA.value);
    private JoystickButton c = new JoystickButton(OI.joystick, 3);

    public SwerveDrive swerveDrive = new SwerveDrive(false);
//    public HolonomicDrive holonomicDrive = new HolonomicDrive(swerveDrive);

    public RobotContainer(){
        configureButtonBindings();

        //m_chooser.addOption("Example Auto 1", new DriveStraight());
        //m_chooser.addOption("Example Auto 2", new ExampleCommand());
        //m_chooser.setDefaultOption();
        Shuffleboard.getTab("Autonomous").add(m_chooser);
        if (Robot.debug) {
            startValueTuner();
            startFireLog();
        }
    }



    private void configureButtonBindings() {
        // Grab the hatch when the 'A' button is pressed.
//        a.whenPressed(new HolonomicDrive(swerveDrive));
//        swerveDrive.setDefaultCommand(new TurnInPlace(swerveDrive));
        swerveDrive.setDefaultCommand(new DriveForward(swerveDrive));

//        swerveDrive.setDefaultCommand(new TankDrive(swerveDrive));
        c.whenPressed(new ResetPositions(swerveDrive));
        //new JoystickButton(m_driverController, Button.kB.value).whenPressed(new ExampleCommand());
    }


    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomous() {
        return m_chooser.getSelected();
    }

    public Command getAutonomousCommand() {
        return new HolonomicDrive(swerveDrive);
    }


    /**
     * Initiates the value tuner.
     */
    private void startValueTuner() {
        new ValueTuner().start();
    }

    /**
     * Initiates the port of team 225s Fire-Logger.
     */
    private void startFireLog() {
        try {
            new Webserver();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}