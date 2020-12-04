package robot.subsystems.drivetrain;

import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import frc.robot.Ports;
import frc.robot.subsystems.drivetrain.SwerveModule;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SwerveModuleTest {

    private SwerveModule swerveModule;
    private double delta = 0.01;

    @Before
    public void setUp() {
        swerveModule = new SwerveModule(0, new TalonFX(0), new TalonSRX(1), new boolean[]{false, false, false, false});
    }

    @Test
    public void getTargetAngle() {
        double targetAngle = swerveModule.getTargetAngle(-1.5 * Math.PI, -Math.PI / 4);
        double expectedAngle = Math.PI / 2;

        Assert.assertEquals(expectedAngle, targetAngle, delta);
    }
}