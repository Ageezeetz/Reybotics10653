/*
 * Robot Movement Subsystem
 */

package frc.robot.subsystems;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.EncoderConfig;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;


public class DriveSubsystem extends SubsystemBase {
    private final SparkMax leftLeader = new SparkMax(2, MotorType.kBrushless);
    private final SparkMax rightLeader = new SparkMax(1, MotorType.kBrushless);
    private final SparkMax leftFollower = new SparkMax(3, MotorType.kBrushless);
    private final SparkMax rightFollower = new SparkMax(4, MotorType.kBrushless);
    private final DifferentialDrive tankdrive = new DifferentialDrive(leftLeader, rightLeader);

    public RelativeEncoder leftEncoder = leftLeader.getEncoder();
    public RelativeEncoder rightEncoder = rightLeader.getEncoder();
    public double leftEncoderPos = leftEncoder.getPosition();
    public double rightEncoderPos = rightEncoder.getPosition();

    private boolean boostMode = false;
    private int direction = -1;
    public PIDController controller = new PIDController(0.02, 0, 0.005);
    public double step = 1; //used to determine which step of auto robot is on

    private final Timer autoTimer = new Timer();

    private final CoralSubsystem coralSubsystem;


    public DriveSubsystem(CoralSubsystem coralSubsystem) {
        this.coralSubsystem = coralSubsystem;

        EncoderConfig encoderConfig = new EncoderConfig().positionConversionFactor(2 * Math.PI * 3 / 8.45); //robot moves 2.23 in per rev
        SparkMaxConfig driveConfig = new SparkMaxConfig();

        driveConfig.encoder.apply(encoderConfig);

        driveConfig.smartCurrentLimit(60);
        driveConfig.voltageCompensation(12);

        driveConfig.follow(leftLeader);
        leftFollower.configure(driveConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

        driveConfig.follow(rightLeader);
        rightFollower.configure(driveConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

        driveConfig.disableFollowerMode();
        leftLeader.configure(driveConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        rightLeader.configure(driveConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        // driveConfig.inverted(true); //not sure if this does anything after configuring the motors?

        tankdrive.setSafetyEnabled(true);
        tankdrive.setExpiration(0.5);    

        //if robot overshoots or jitters near target, increase; if robot stops too far from target, decrease
        controller.setTolerance(0.75); //adds a little bit of error in case the robot doesn't fully reach setpoint
    }

    public void boostMode() {
        boostMode = !boostMode;
        if (boostMode) {System.out.println("Boost mode enabled!");}
        else {System.out.println("Boost mode disabled!");}
    }

    public void robotDirection() {
        direction *= -1;
        if (direction == 1) {System.out.println("Roller is forward");}
        else {System.out.println("Climber is forward");}
    }

    public void arcadeDrive(double forward, double rotation) {
        double boostSpeedChange = boostMode ? Constants.drive.BOOST_SPEED : Constants.drive.NORMAL_SPEED;

        tankdrive.arcadeDrive(rotation * boostSpeedChange / 1.25, forward * boostSpeedChange * direction);
        tankdrive.feed();
    }

    public void tankDrive(double leftSpeed, double rightSpeed) {
        tankdrive.tankDrive(leftSpeed, rightSpeed);

    }

    public void startAutoTimer() {
        autoTimer.reset();
        autoTimer.start();
    }

    public double getAutoTimer() {
        return autoTimer.get();
    }

    public void stopAutoTimer() {
        autoTimer.stop();
    }

    public double getEncoderPositions() {
        return (leftEncoder.getPosition() + rightEncoder.getPosition()) / 2;
    }

    public void stop() {
        tankdrive.stopMotor();
    }
}