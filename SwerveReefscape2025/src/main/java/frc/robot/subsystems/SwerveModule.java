/*
 * Helper class that creates each swerve module (each bundle of motors for each corner)
 */
package frc.robot.subsystems;

import static frc.robot.Constants.drivetrain.GEAR_RATIO;
import static frc.robot.Constants.drivetrain.WHEEL_DIAMETER_INCHES;

import static frc.robot.Constants.drivetrain.VORTEX_TICKS_PER_REV;

import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModuleState;

import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;

public class SwerveModule {
    private final SparkMax driveMotor;
    private final SparkMax turnMotor; 
    private final RelativeEncoder driveEncoder;
    private final AbsoluteEncoder turnEncoder;
    private final SparkMaxConfig driveMotorConfig = new SparkMaxConfig();
    private final SparkMaxConfig turnMotorConfig = new SparkMaxConfig();

    private final double distanceInInches = ((Math.PI * WHEEL_DIAMETER_INCHES) / (VORTEX_TICKS_PER_REV * GEAR_RATIO));


    public SwerveModule(int driveMotorID, int turnMotorID) {
        driveMotor = new SparkMax(driveMotorID, MotorType.kBrushless);
        turnMotor = new SparkMax(turnMotorID, MotorType.kBrushless);

        driveEncoder = driveMotor.getEncoder();
        turnEncoder = turnMotor.getAbsoluteEncoder();

        driveMotorConfig.smartCurrentLimit(75);
        driveMotorConfig.secondaryCurrentLimit(100);
        driveMotorConfig.voltageCompensation(12);
        driveMotor.configure(driveMotorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

        turnMotorConfig.smartCurrentLimit(25);
        turnMotorConfig.secondaryCurrentLimit(40);
        turnMotorConfig.voltageCompensation(12);
        turnMotor.configure(turnMotorConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }

    public SwerveModuleState getState() { //not used yet
        return new SwerveModuleState(driveEncoder.getVelocity(), new Rotation2d(Math.toRadians(turnEncoder.getPosition()))); //not sure if math to radians is needed
    }

    public void resetEncoder() { //used to reset driveMotor encoder
        driveEncoder.setPosition(0);
    }

    public double getPosition() { //used for autonomous; turns encoder position into inches instead of ticks
        return driveEncoder.getPosition() * distanceInInches;
    }

    public void setState(SwerveModuleState state) { //used 
        driveMotor.set(state.speedMetersPerSecond / 5.0);
        turnMotor.set(state.angle.getRadians());
    }
}