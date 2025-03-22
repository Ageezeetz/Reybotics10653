/*
 * Helper class that creates each swerve module (each bundle of motors for each corner)
 */
package frc.robot.subsystems;

// import static frc.robot.Constants.drivetrain.GEAR_RATIO;
// import static frc.robot.Constants.drivetrain.WHEEL_DIAMETER_INCHES;

import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkMax;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModuleState;

import com.revrobotics.spark.SparkLowLevel.MotorType;

public class SwerveModule {
    private final SparkMax driveMotor;
    private final SparkMax turnMotor; 
    private final RelativeEncoder driveEncoder;
    private final AbsoluteEncoder turnEncoder;


    public SwerveModule(int driveMotorID, int turnMotorID) {
        driveMotor = new SparkMax(driveMotorID, MotorType.kBrushless);
        turnMotor = new SparkMax(turnMotorID, MotorType.kBrushless);

        driveEncoder = driveMotor.getEncoder();
        turnEncoder = turnMotor.getAbsoluteEncoder();

        // double inchesPerTick = ((Math.PI * WHEEL_DIAMETER_INCHES) / GEAR_RATIO);
        // driveEncoder.setPositionConversionFactor(inchesPerTick);
    }

    public SwerveModuleState getState() { //not used yet
        return new SwerveModuleState(driveEncoder.getVelocity(), new Rotation2d(turnEncoder.getPosition()));
    }

    public void resetEncoder() { //used to reset driveMotor encoder
        driveEncoder.setPosition(0);
    }

    public double getPosition() { //used for autonomous
        return driveEncoder.getPosition();
    }

    public void setState(SwerveModuleState state) { //used 
        driveMotor.set(state.speedMetersPerSecond / 5.0);
        turnMotor.set(state.angle.getRadians());
    }
}
