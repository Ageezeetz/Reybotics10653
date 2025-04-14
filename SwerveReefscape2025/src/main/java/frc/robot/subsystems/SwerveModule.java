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

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.controller.PIDController;
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

    private final PIDController pidcontroller = new PIDController(0.05, 0, 0.001);


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

        pidcontroller.setTolerance(0.1);
        pidcontroller.enableContinuousInput(-Math.PI, Math.PI);
    }

    public SwerveModuleState getState() { //not used yet
        return new SwerveModuleState(driveEncoder.getVelocity(), new Rotation2d(turnEncoder.getPosition()));
    }

    public void resetEncoder() { //used to reset driveMotor encoder
        driveEncoder.setPosition(0);
    }

    public double getPosition() { //used for autonomous; turns encoder position into inches instead of ticks
        return driveEncoder.getPosition() * distanceInInches;
    }

    public double getAngle() { //returns angle for each wheel when asked
        return turnEncoder.getPosition() * 2 * Math.PI;
    }

    public void setState(SwerveModuleState state) { //returns speedMetersPerSecond and angle
        if (Math.abs(state.speedMetersPerSecond) < 0.001) {
            stop();
            return;
        }
        //sets desiredState to optimize turning for rotation
        state.optimize(Rotation2d.fromDegrees(getAngle()));
        //optimize inverts output to make turning quicker; if wheel degrees is 10, and wanted degrees from controller input is 190,
        //the wheels will invert the direction of the wheels to go in that direction instead

        //sets drive motor first
        driveMotor.set(state.speedMetersPerSecond / 5.0);

        //turns the SwerveModuleState from radians to degrees
        double desiredAngle = Math.toDegrees(state.angle.getRadians()); //rotation2d initially gives values in radians, have to convert
        //sets setpoint from contoller input, and current location to getAngle(), which gives current turnEncoder position
        double output = pidcontroller.calculate(getAngle(), desiredAngle);

        output = MathUtil.clamp(output, -0.5, 0.5); //makes sure that the speed is between abs 50%, no higher or lower (-) than that
        
        //sets turn motor
        if (pidcontroller.atSetpoint()) {
            turnMotor.set(0);
        } 
        else {
            turnMotor.set(output);
        }
    }

    public void stop() {
        driveMotor.stopMotor();
        turnMotor.stopMotor();
    }
}