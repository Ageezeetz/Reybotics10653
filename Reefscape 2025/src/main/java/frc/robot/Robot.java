package frc.robot;

/*
 * Imports
 */
import edu.wpi.first.wpilibj.TimedRobot; 
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.revrobotics.RelativeEncoder;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.EncoderConfig;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.Timer;

import edu.wpi.first.wpilibj.XboxController;

import edu.wpi.first.math.controller.PIDController;



/*
 * Main Robot Class
 */
// If the name of this public class is changed, remember to change as well in the Main.java file
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCenterCoral = "Center and Coral"; //custom modes for SmartDashboard
  private static final String kJustDrive = "Only Drive";
  private static final String kRightCoral = "Right Side Coral";
  private static final String kLeftCoral = "Left Side Coral";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  final SparkMax rollerMotor = new SparkMax(5, MotorType.kBrushless); //creates roller variable

  final SparkMax leftLeader = new SparkMax(2, MotorType.kBrushless); //creates sparkmax variables
  final SparkMax leftFollower = new SparkMax(3, MotorType.kBrushless);
  final SparkMax rightLeader = new SparkMax(1, MotorType.kBrushless);
  final SparkMax rightFollower = new SparkMax(4, MotorType.kBrushless);

  final DifferentialDrive myDrive = new DifferentialDrive(leftLeader, rightLeader); //used for motor calls

  final SparkMaxConfig driveConfig = new SparkMaxConfig(); //creating setups for wheel, roller, and climber SparkMaxes 
  final SparkMaxConfig rollerConfig = new SparkMaxConfig();
  final EncoderConfig encoderConfig = new EncoderConfig().positionConversionFactor(2 * Math.PI * 3 / 8.45);


  final Timer timer1 = new Timer(); //new timer
  final Timer sleepTime = new Timer();

  final double ROLLER_STRENGTH = 0.25; //variable for roller strength
  double driveSpeed = 0; //variable for boost drive speed
  double opposite = 1; //variable for default robot direction
  double speedRate = 0;
  double setpoint = 0;
  
  final XboxController driverGamepad = new XboxController(0);
  final XboxController operatorGamepad = new XboxController(1);

  RelativeEncoder leftEncoder = leftLeader.getEncoder();
  RelativeEncoder rightEncoder = rightLeader.getEncoder();

  double rightEncoderPos = rightEncoder.getPosition();
  double leftEncoderPos = leftEncoder.getPosition();

  PIDController controller = new PIDController(0.05, 0, 0);
  double step = 1; //used to determine which step of auto robot is on

  boolean safetyBool = true; //safety switch for watchdog/differential drive error (possible fix)



  /*
  * Robot Initialization
  */
  public Robot() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("Center and Coral", kCenterCoral);
    m_chooser.addOption("Only Drive", kJustDrive);
    m_chooser.addOption("Right Side Coral", kRightCoral);
    m_chooser.addOption("Left Side Coral", kLeftCoral);

    SmartDashboard.putData("Auto choices", m_chooser);

    driveConfig.encoder.apply(encoderConfig);

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

    myDrive.setSafetyEnabled(safetyBool);
    myDrive.setExpiration(0.5);
  }



  /*
   * Functions
   */
  private double getEncoderPositions() {
    return (leftEncoder.getPosition() + rightEncoder.getPosition()) / 2;
  }

  private void resetEncoders() {
    leftEncoder.setPosition(0);
    rightEncoder.setPosition(0);
  }

  private void leftCoralAuto() {
    resetEncoders();
    if (step == 1) {
      controller.setSetpoint(10); //sets the destination in INCHES
      double output = controller.calculate(getEncoderPositions()); //change setpoint depending on destination dist
      myDrive.tankDrive(output, -output);
      myDrive.feed();
      if (controller.atSetpoint()) {
        step++;
      }
    }
    else if (step == 2) {
      double degrees = 90; //set amount of degrees to turn
      double rotations = degrees / 360;

      controller.setSetpoint(rotations);
      double output = controller.calculate(getEncoderPositions(), controller.getSetpoint());
      myDrive.tankDrive(output, output); //turns right
      myDrive.feed();
      if (controller.atSetpoint()) {
        step++;
      }
    }
    else if (step == 3) {
      controller.setSetpoint(10);
      double output = controller.calculate(getEncoderPositions(), controller.getSetpoint());
      myDrive.tankDrive(output, -output);
      myDrive.feed();
      if (controller.atSetpoint()) {
        step++;
      }
    }
    else if (step == 4) {
      sleepTime.start();
      rollerMotor.set(ROLLER_STRENGTH); //deposit
      if (sleepTime.get() > 1) {
        step++;
      }
    }
    else {
      myDrive.arcadeDrive(0, 0); //stop
      myDrive.feed();
      rollerMotor.set(0);
    }
  }




  @Override
  public void robotPeriodic() {
    rightEncoderPos = rightEncoder.getPosition();
    leftEncoderPos = leftEncoder.getPosition();
    getEncoderPositions();

    /*
     * Prints
     */
    SmartDashboard.putNumber("Left Encoder Position", leftEncoder.getPosition()); //left encoder
    SmartDashboard.putNumber("Right Encoder Position", rightEncoder.getPosition()); //right encoder
    SmartDashboard.putNumber("Encoder Positions", getEncoderPositions()); //average of both encoders
    SmartDashboard.putNumber("PID Output", controller.calculate(getEncoderPositions())); //output of PID
    SmartDashboard.putBoolean("Controller at Target", controller.atSetpoint()); //true or false
  }



  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    //m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);

    timer1.reset();
    sleepTime.reset();
    timer1.start();

    resetEncoders();
    }



  @Override
  public void autonomousPeriodic() { //function runs many times during auto period
    // myDrive.feed();
    /*
    * Selections for auto modes \o/
    */
    switch (m_autoSelected) { //allows switching between modes in SmartDashboard
      case kCenterCoral: //score coral when center of starting line
      default:
        resetEncoders();
        getEncoderPositions();
        if (step == 1) {
          controller.setSetpoint(10); //sets the destination in INCHES
          double output = controller.calculate(getEncoderPositions()); //change setpoint depending on destination dist
          myDrive.tankDrive(output, -output);
          myDrive.feed();
          if (controller.atSetpoint()) {
            step++;
          }
        }
        else if (step == 2) {
          sleepTime.start();
          myDrive.tankDrive(0, 0);
          myDrive.feed();
          rollerMotor.set(ROLLER_STRENGTH); //deposit
          if (sleepTime.get() > 5) {
            step++;
          }
        }
        else {
          myDrive.arcadeDrive(0, 0); //stop
          myDrive.feed();
          rollerMotor.set(0);
        }
        break;

      case kRightCoral: //score coral when right side of starting line
        resetEncoders();
        getEncoderPositions();
        rollerMotor.set(0);
        if (timer1.get() < 1.5) { //drive forward
          myDrive.tankDrive(0.5, -0.5);
          myDrive.feed();
        }
        else if (timer1.get() < 2) { //rotate left
          myDrive.tankDrive(-0.5, 0.5);
          myDrive.feed();
        }
        else if (timer1.get() < 2.4) { //drive to reef
          myDrive.tankDrive(0.5, 0.5);
          myDrive.feed();
        }
        else if (timer1.get() < 2.8) { //deposit coral
          rollerMotor.set(ROLLER_STRENGTH);
        }
        else { //stop all
          myDrive.tankDrive(0, 0);
          myDrive.feed();
          rollerMotor.set(0);
        }
        break;

      case kLeftCoral: //score coral when left side of starting line
        leftCoralAuto();
        break;

      case kJustDrive: //get out of starting zone 
        resetEncoders();
        getEncoderPositions();
        if (step == 1) {
          controller.setSetpoint(10);
          double output = controller.calculate(getEncoderPositions());
          myDrive.tankDrive(output, output);
          myDrive.feed();
          if (controller.atSetpoint()) {
            step++;
          }
        }
        break;
    }
  }



//Called once at beginning of teleop period
  @Override
  public void teleopInit() {
    speedRate = 0;

    resetEncoders();
  }



//Called repeatedly during teleop period
  @Override
  public void teleopPeriodic() {
    // myDrive.feed();
    /*
    * Boost Toggle
    */
    if (driverGamepad.getLeftBumperButtonPressed()) { //toggle
      if (speedRate == 1) {
        speedRate--;
      }
      else {
        speedRate++;
      }
    }

    if (speedRate == 1) { //speed change
      driveSpeed = 0.75;
    }
    else {
      driveSpeed = 0.5;
    }


    /*
     * Flip Direction Toggle
     */
    if (driverGamepad.getRightBumperButtonPressed()) {
      // opposite *= -1;
      if (opposite == 1) {
        opposite = -1;
      }
      else {
        opposite = 1;
      }
    }


    /*
     * Drive Controls
     */
    myDrive.arcadeDrive(driverGamepad.getRightX()*driveSpeed/1.25, driverGamepad.getLeftY()*driveSpeed*opposite);
    myDrive.feed();

    /*
     * Roller Controls
     */
    if (operatorGamepad.getPOV() == 180) { //bottom D-pad button pressed
      rollerMotor.set(ROLLER_STRENGTH); //roll in
    }
    else if (operatorGamepad.getPOV() == 0) { //top D-pad button pressed
      rollerMotor.set(-ROLLER_STRENGTH); //roll out
    }
    else {
      rollerMotor.set(0);
    }
  }



//Called once when the robot is disabled
  @Override
  public void disabledInit() {

  }



//Called repeatedly when robot is disabled
  @Override
  public void disabledPeriodic() {

  }



  /** This function is called once when test mode is enabled. */
  @Override
  public void testInit() {

  }



  /** This function is called periodically during test mode. */
  @Override
  public void testPeriodic() {

  }



  /** This function is called once when the robot is first started up. */
  @Override
  public void simulationInit() {
    
  }



  /** This function is called periodically whilst in simulation. */
  @Override
  public void simulationPeriodic() {

  }
}
