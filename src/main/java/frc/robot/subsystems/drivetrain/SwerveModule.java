package frc.robot.subsystems.drivetrain;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.SupplyCurrentLimitConfiguration;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Ports;
import frc.robot.Utils;

public class SwerveModule extends SubsystemBase {
    private SupplyCurrentLimitConfiguration currLimitConfig = new SupplyCurrentLimitConfiguration(true, Constants.Drivetrain.MAX_CURRENT, 5, 0.02);
    public final TalonFX driveMotor;
    private final TalonSRX angleMotor;
    private final int wheel;

    private UnitModel unitDrive = new UnitModel(Constants.Drivetrain.TICKS_PER_METER);
    private UnitModel unitAngle = new UnitModel(Constants.Drivetrain.TICKS_PER_RAD);

    public SwerveModule(int wheel, TalonFX driveMotor, TalonSRX angleMotor, boolean[] inverted) {
        // configure feedback sensors
        angleMotor.configSelectedFeedbackSensor(FeedbackDevice.Analog, wheel, Constants.TALON_TIMEOUT);
        angleMotor.configFeedbackNotContinuous(Ports.SwerveDrive.IS_NOT_CONTINUOUS_FEEDBACK, Constants.TALON_TIMEOUT);

        angleMotor.setNeutralMode(NeutralMode.Brake);

        angleMotor.setSelectedSensorPosition(0);

        // set inversions
        angleMotor.setInverted(inverted[0]);
        driveMotor.setInverted(inverted[1]);

        angleMotor.setSensorPhase(inverted[2]);
        driveMotor.setSensorPhase(inverted[3]);

        // Set amperage limits
        driveMotor.configSupplyCurrentLimit(currLimitConfig);

        angleMotor.configContinuousCurrentLimit(Constants.Drivetrain.MAX_CURRENT);
        angleMotor.enableCurrentLimit(true);

        // set PIDF - angle motor
        angleMotor.config_kP(0, Constants.SwerveModule.KP.get(), Constants.TALON_TIMEOUT);
        angleMotor.config_kI(0, Constants.SwerveModule.KI.get(), Constants.TALON_TIMEOUT);
        angleMotor.config_kD(0, Constants.SwerveModule.KD.get(), Constants.TALON_TIMEOUT);
        angleMotor.config_kF(0, Constants.SwerveModule.KF.get(), Constants.TALON_TIMEOUT);

        // set PIDF - drive motor
        driveMotor.config_kP(0, Constants.SwerveModule.KP_DRIVE.get(), Constants.TALON_TIMEOUT);
        driveMotor.config_kI(0, Constants.SwerveModule.KI_DRIVE.get(), Constants.TALON_TIMEOUT);
        driveMotor.config_kD(0, Constants.SwerveModule.KD_DRIVE.get(), Constants.TALON_TIMEOUT);
        driveMotor.config_kF(0, Constants.SwerveModule.KF_DRIVE.get(), Constants.TALON_TIMEOUT);


        // set voltage compensation and saturation
        driveMotor.enableVoltageCompensation(true);
        driveMotor.configVoltageCompSaturation(12);

        angleMotor.enableVoltageCompensation(true);
        angleMotor.configVoltageCompSaturation(12);

        angleMotor.selectProfileSlot(0, 0);
        driveMotor.selectProfileSlot(0, 0);

        this.driveMotor = driveMotor;
        this.angleMotor = angleMotor;
        this.wheel = wheel;
    }


    /**
     * @return the speed of the wheel in [m/s]
     */
    public double getSpeed() {
        return unitDrive.toVelocity(driveMotor.getSelectedSensorVelocity());
    }

    /**
     * @return the angle of the wheel in radians
     */
    public double getAngle() {
        return unitAngle.toUnits(angleMotor.getSelectedSensorPosition() - Constants.SwerveModule.ZERO_POSITION[wheel]);
    }

    /**
     * sets the speed of the the wheel in ticks per 100ms
     * @param speed the speed of the wheel in [m/s]
     */
    public void setSpeed(double speed) {
       driveMotor.set(ControlMode.Velocity, unitDrive.toTicks100ms(speed));
    }
    /**
     * sets the angle of the wheel, in consideration of the shortest path to the target angle
     * @param angle the target angle in radians
     */
    public void setAngle(double angle) {
        //double targetAngle = getTargetAngle(angle, getAngle());
        int targetTicks = getTargetTicks(angle);
        angleMotor.set(ControlMode.Position, targetTicks);
    }

    public int getTargetTicks(double targetAngle) { 
        int currEnc = angleMotor.getSelectedSensorPosition() + Constants.SwerveModule.ZERO_POSITION[wheel];
        int curr = currEnc % Constants.Drivetrain.TICKS_IN_ENCODER;
        int angleTicks = unitAngle.toTicks(targetAngle) + Constants.SwerveModule.ZERO_POSITION[wheel];
        int delta = angleTicks - curr;
        int targetTicks = currEnc + delta;
        return targetTicks;
    }


    /**
     * finds the target angle of the wheel based on the shortest distance from the current position
     * @param angle the current target angle
     * @param currentAngle the current angle of the wheel
     * @return the target angle
     */
    public double getTargetAngle(double angle, double currentAngle) {
        // makes sure the value is between -pi and pi
        angle = Utils.floorMod(angle, Math.PI);
        double[] angles = {angle - 2 * Math.PI, angle, angle + 2 * Math.PI}; // An array of all possible target angles
        double targetAngle = currentAngle;
        double shortestDistance = Double.MAX_VALUE;
        for (double target : angles) { // for each possible angle
            if (Math.abs(target - currentAngle) < shortestDistance) // if the calculated distance is less than the current shortest distance
            {
                shortestDistance = Math.abs(target - currentAngle);
                targetAngle = target;
            }
        }

        return targetAngle;
    }

    /**
     * stops the angle motor
     */
    public void stopAngleMotor() {
        angleMotor.set(ControlMode.PercentOutput, 0);
    }

    public void resetAngle(){
        angleMotor.setSelectedSensorPosition(0);
    }


    @Override
    public void periodic(){
        // set PIDF - angle motor
        angleMotor.config_kP(0, Constants.SwerveModule.KP.get(), Constants.TALON_TIMEOUT);
        angleMotor.config_kI(0, Constants.SwerveModule.KI.get(), Constants.TALON_TIMEOUT);
        angleMotor.config_kD(0, Constants.SwerveModule.KD.get(), Constants.TALON_TIMEOUT);
        angleMotor.config_kF(0, Constants.SwerveModule.KF.get(), Constants.TALON_TIMEOUT);

        // set PIDF - drive motor
        driveMotor.config_kP(0, Constants.SwerveModule.KP_DRIVE.get(), Constants.TALON_TIMEOUT);
        driveMotor.config_kI(0, Constants.SwerveModule.KI_DRIVE.get(), Constants.TALON_TIMEOUT);
        driveMotor.config_kD(0, Constants.SwerveModule.KD_DRIVE.get(), Constants.TALON_TIMEOUT);
        driveMotor.config_kF(0, Constants.SwerveModule.KF_DRIVE.get(), Constants.TALON_TIMEOUT);
    }
}
