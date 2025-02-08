package frc.robot;

//imports
import edu.wpi.first.wpilibj.TimedRobot; 
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.Timer;

import edu.wpi.first.wpilibj.XboxController;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;

import edu.wpi.first.math.controller.PIDController;


// If the name of this public class is changed, remember to change as well in the Main.java file
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCenterCoral = "Center and Coral"; //custom modes for SmartDashboard
  private static final String kJustDrive = "Only Drive";
  private static final String kRightCoral = "Right Side Coral";
  private static final String kLeftCoral = "Left Side Coral";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  private final SparkMax rollerMotor = new SparkMax(5, MotorType.kBrushless); //creates roller variable

  private final SparkMax leftLeader = new SparkMax(2, MotorType.kBrushless); //creates sparkmax variables
  private final SparkMax leftFollower = new SparkMax(3, MotorType.kBrushless);
  private final SparkMax rightLeader = new SparkMax(1, MotorType.kBrushless);
  private final SparkMax rightFollower = new SparkMax(4, MotorType.kBrushless);

  private final DifferentialDrive myDrive = new DifferentialDrive(leftLeader, rightLeader); //used for motor calls

  private final SparkMaxConfig driveConfig = new SparkMaxConfig(); //creating setups for driving and roller 
  private final SparkMaxConfig rollerConfig = new SparkMaxConfig();

  private final Timer timer1 = new Timer(); //new timer

  private final double ROLLER_STRENGTH = 0.25; //variable for roller strength
  private double driveSpeed = 0.5; //variable for drive speed
  private Integer speedRate;
  private boolean opposite = false;
  private double setpoint = 0;
  
  private final XboxController driverGamepad = new XboxController(0);
  private final XboxController gamepadOperator = new XboxController(1);

  private Encoder leftEncoder = new Encoder(0, 10, true, EncodingType.k4X); //unsure about channels, type and direction
  private Encoder rightEncoder = new Encoder(0, 11, true, EncodingType.k4X); //unsure about channels, type and direction
  // convert known # of ticks into rotations, then multiply gear ratio, then convert into inches from the diameter of the wheels, then convert into feet
  // FEET = CONSTANT * TICKS
  private final double encoderConstant = (1.0 / 128) * ((6 * Math.PI) / 1) / 12;
  private final double leftEncoderPos = leftEncoder.get() * encoderConstant;
  private final double rightEncoderPos = rightEncoder.get() * encoderConstant;

  private final double kP = 0;  //change from 0.05, 0.5, 1
  private final double error = setpoint - ((leftEncoderPos + rightEncoderPos) / 2); //setpoint minus average of encoders from both sides
  private final double outputSpeed = kP * error;



  //startup for all robot code during startup
  public Robot() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("Center and Coral", kCenterCoral);
    m_chooser.addOption("Only Drive", kJustDrive);
    m_chooser.addOption("Right Side Coral", kRightCoral);
    m_chooser.addOption("Left Side Coral", kLeftCoral);

    SmartDashboard.putData("Auto choices", m_chooser);

    driveConfig.smartCurrentLimit(60); //sets amp limit for each motor
    driveConfig.voltageCompensation(12); //sends half of given voltage to motor to help keep driving consistent

    driveConfig.follow(leftLeader); //sets backleft motor to match top left motor
    leftFollower.configure(driveConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters); //motor 3

    driveConfig.follow(rightLeader); //sets backright motor to match top right motor
    rightFollower.configure(driveConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters); //motor 4

    driveConfig.disableFollowerMode();
    rightLeader.configure(driveConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters); //motor 1
    leftLeader.configure(driveConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters); //motor 2
    driveConfig.inverted(true); //flips motor number values

    rollerConfig.smartCurrentLimit(60);
    rollerConfig.voltageCompensation(10);
    rollerMotor.configure(rollerConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

    timer1.start(); //starts timer
  }



  @Override
  public void robotPeriodic() {
    SmartDashboard.putNumber("Left Side Encoder Value", leftEncoderPos * outputSpeed);
    SmartDashboard.putNumber("Right Side Encoder Value", rightEncoderPos * outputSpeed);

  }



  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    //m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);

    timer1.restart();
    leftEncoder.reset();
    rightEncoder.reset();

    speedRate = 0;
    setpoint = 0;
  }



  @Override
  public void autonomousPeriodic() { //function runs many times during auto period
    if (driverGamepad.getAButton()) { //if A button is pressed, robot should move forward 10 feet?
      setpoint = 10;
    }
    else {
      setpoint = 0;
    }
 
    leftLeader.set(outputSpeed);
    rightLeader.set(outputSpeed);


    switch (m_autoSelected) { //allows switching between modes in SmartDashboard
      case kCenterCoral: //score coral when center of starting line
        if (timer1.get() < 1) { //drive forward
          myDrive.tankDrive(0.5, -0.5);
        }
        else if (timer1.get() < 1.5) {
          myDrive.tankDrive(0, 0);
        }
        else if (timer1.get() < 2) { //deposit coral
          myDrive.tankDrive(0, 0);
          rollerMotor.set(-ROLLER_STRENGTH);
        }
        else { //stop all
          myDrive.tankDrive(0, 0);
          rollerMotor.set(0);
        }
        break;
      
      case kRightCoral: //score coral when right side of starting line
        if (timer1.get() < 1.5) { //drive forward
          myDrive.tankDrive(0.5, -0.5);
        }
        else if (timer1.get() < 2) { //rotate left
          myDrive.tankDrive(-0.5, 0.5);
        }
        else if (timer1.get() < 2.4) { //drive to reef
          myDrive.tankDrive(0.5, 0.5);
        }
        else if (timer1.get() < 2.8) { //deposit coral
          rollerMotor.set(ROLLER_STRENGTH);
        }
        else { //stop all
          myDrive.tankDrive(0, 0);
          rollerMotor.set(0);
        }
        break;

      case kLeftCoral: //score coral when left side of starting line
      if (timer1.get() < 1.5) { //drive forward
        myDrive.tankDrive(0.5, 0.5);
      }
      else if (timer1.get() < 2) { //rotate right
        myDrive.tankDrive(0.5, -0.5);
      }
      else if (timer1.get() < 2.4) { //drive to reef
        myDrive.tankDrive(0.5, 0.5);
      }
      else if (timer1.get() < 2.8) { //deposit coral
        rollerMotor.set(ROLLER_STRENGTH);
      }
      else { //stop all
        myDrive.tankDrive(0, 0);
        rollerMotor.set(0);
      }
      break;

      case kJustDrive: //get out of starting zone 
        if (timer1.get() < 0.9) { //drive forward
          myDrive.tankDrive(0.5, 0.5);
        }
        else { //stop all 
          myDrive.tankDrive(0, 0);
        }

      case kDefaultAuto:
      default:
        myDrive.tankDrive(0.1, 0.1);
        break;
    }
  }



//Called once at beginning of teleop period
  @Override
  public void teleopInit() {}



//Called repeatedly during teleop period
  @Override
  public void teleopPeriodic() {
    // Boost button
    if (driverGamepad.getLeftBumperButtonPressed()) {
      if (speedRate == 1) {
        speedRate--;
      }
      else {
        speedRate++;
      }
    }

    // Robot speed change w/ boost button
    if (speedRate == 1) {
      driveSpeed = 0.75;
    }
    else {
      driveSpeed = 0.5;
    }

    // Flips robot direction
    if (driverGamepad.getRightBumperButtonPressed()) {
      if (opposite == false) {
        opposite = true;
      }
      else {
        opposite = false;
      }


    //Uncomment below for tank style controls
    // if (opposite == true) {
    //   myDrive.tankDrive(driverGamepad.getLeftY()*driveSpeed, -driverGamepad.getRightY()*driveSpeed);
    // }
    // else {
    //   myDrive.tankDrive(-driverGamepad.getLeftY()*driveSpeed, driverGamepad.getRightY()*driveSpeed);
    // }

    //Uncomment below for arcade style controls
    if (opposite == true) {
      myDrive.arcadeDrive(driverGamepad.getRightX()*driveSpeed/1.25, -driverGamepad.getLeftY()*driveSpeed);  //left = forward/backward, right = turning
    }
    else {
      myDrive.arcadeDrive(driverGamepad.getRightX()*driveSpeed/1.25, driverGamepad.getLeftY()*driveSpeed);
    }

    //Uncomment below for single joystick controls (left joystick)
    // if (opposite == true) {
    //   myDrive.arcadeDrive(driverGamepad.getLeftX()*driveSpeed/1.25, -driverGamepad.getLeftY()*driveSpeed); //left = forward/backward/left/right
    // }
    // else {
    //   myDrive.arcadeDrive(driverGamepad.getLeftX()*driveSpeed/1.25, driverGamepad.getLeftY()*driveSpeed);
    // }
    

    if (gamepadOperator.getPOV() == 180) { //roller contorl on op controller with D-pad 
      rollerMotor.set(ROLLER_STRENGTH);
    }
    else if (gamepadOperator.getPOV() == 0) {
      rollerMotor.set(-ROLLER_STRENGTH);
    }
    else {
      rollerMotor.set(0);
    }
  }
}



//Called once when the robot is disabled
  @Override
  public void disabledInit() {}



//Called repeatedly when robot is disabled
  @Override
  public void disabledPeriodic() {}



  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {}



  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {}



  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {}



  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {}
}
