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
    private int direction = 1;
    public PIDController controller = new PIDController(0.02, 0, 0.005);
    public double step = 1; //used to determine which step of auto robot is on

    private final Timer autoTimer = new Timer();

    private CoralSubsystem coralSubsystem = new CoralSubsystem();


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

        //if robot overshoots or jitters near target, increase; if robot stops too far from target, decrease
        controller.setTolerance(0.75); //adds a little bit of error in case the robot doesn't fully reach setpoint
    }

    public void boostMode() {
        boostMode = !boostMode;
    }

    public void robotDirection() {
        direction *= -1;  
    }

    public void arcadeDrive(double forward, double rotation) {
        double boostSpeedChange = boostMode ? Constants.driveConstants.BOOST_SPEED : Constants.driveConstants.NORMAL_SPEED;
        tankdrive.arcadeDrive(forward * boostSpeedChange * direction, rotation * boostSpeedChange / 1.25);
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
        return (leftEncoderPos + rightEncoderPos) / 2;
    }

    public void straightCoralAuto(double distance) {
        if (step == 1) {
            startAutoTimer();
            if (getAutoTimer() > 8) {
                step++;
                stopAutoTimer();
            }
        }
        else if (step == 2) {
            controller.setSetpoint(distance);
            double output = controller.calculate(getEncoderPositions());
            tankdrive.tankDrive(output, -output);
            startAutoTimer();
            if (controller.atSetpoint() || getAutoTimer() > 3) {
                step++;
                stopAutoTimer();
            }
        }
        else if (step == 3) {
            startAutoTimer();
            tankdrive.tankDrive(Constants.STOP_MOTOR, Constants.STOP_MOTOR);
            coralSubsystem.dropCoral();
            if (getAutoTimer() > 1.5) {
                step++;
                coralSubsystem.stopMotor();
                stopAutoTimer();
            }
        }
        else {
            coralSubsystem.stopMotor();
            tankdrive.tankDrive(Constants.STOP_MOTOR, Constants.STOP_MOTOR);
        }
    }

    public void stop() {
        tankdrive.stopMotor();
    }
}