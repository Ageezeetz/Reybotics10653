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

  final SparkMaxConfig driveConfig = new SparkMaxConfig(); //creating setups for driving and roller 
  final SparkMaxConfig rollerConfig = new SparkMaxConfig();

  final Timer timer1 = new Timer(); //new timer
  final Timer sleepTime = new Timer();

  final double ROLLER_STRENGTH = 0.25; //variable for roller strength
  double driveSpeed = 0; //variable for boost drive speed
  boolean opposite = false; //variable for default robot direction
  double speedRate = 0;
  double setpoint = 0;
  
  final XboxController driverGamepad = new XboxController(0);
  final XboxController operatorGamepad = new XboxController(1);

  RelativeEncoder leftEncoder = leftLeader.getEncoder();
  RelativeEncoder rightEncoder = rightLeader.getEncoder();

  double rightEncoderPos = rightEncoder.getPosition();
  double leftEncoderPos = leftEncoder.getPosition();
  double encoderPositions = (leftEncoderPos + rightEncoderPos) / 2;

  PIDController controller = new PIDController(0.1, 0, 0);
  double step = 0; //used to determine which step of auto robot is on

  boolean safetyBool = true; //safety switch for watchdog/differential drive error



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

    // 2 * PI * wheel radius in conversion unit / gearing
    EncoderConfig encoderConfig = new EncoderConfig().positionConversionFactor(2 * Math.PI * 3 / 8.45); //convertepion factor now in inches?
    driveConfig.encoder.apply(encoderConfig);

    System.out.println("Robot started!");

    myDrive.setSafetyEnabled(safetyBool);
  }



  /*
   * Functions
   */
  private void centerCoralAuto() {
    step++;
    if (step == 1) {
      controller.setSetpoint(10); //sets the destination in INCHES
      double output = controller.calculate(encoderPositions, controller.getSetpoint()); //change setpoint depending on destination dist
      myDrive.tankDrive(output, -output);
      if (controller.atSetpoint()) {
        step++;
      }
    }
    else if (step == 2) {
      sleepTime.start();
      rollerMotor.set(-ROLLER_STRENGTH); //deposit
      if (sleepTime.get() > 1) {
        step++;
      }
    }
    else {
      myDrive.arcadeDrive(0, 0); //stop
      rollerMotor.set(0);
    }
  }



  @Override
  public void robotPeriodic() {
    rightEncoderPos = rightEncoder.getPosition();
    leftEncoderPos = leftEncoder.getPosition();
    encoderPositions = (leftEncoderPos + rightEncoderPos) / 2;

    /*
     * Prints
     */
    SmartDashboard.putNumber("Left Encoder Position", leftEncoder.getPosition());
    SmartDashboard.putNumber("Right Encoder Position", rightEncoder.getPosition());
    SmartDashboard.putNumber("Encoder Positions", encoderPositions);
    SmartDashboard.putBoolean("Controller at Target", controller.atSetpoint());
  }



  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
    //m_autoSelected = SmartDashboard.getString("Auto Selector", kDefaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);

    timer1.reset();
    sleepTime.reset();
    timer1.start();
    sleepTime.start();

    leftEncoder.setPosition(0);
    rightEncoder.setPosition(0);
    encoderPositions = 0;
    }



  @Override
  public void autonomousPeriodic() { //function runs many times during auto period
    myDrive.feed();
    /*
    * Selections for auto modes \o/
    */
    switch (m_autoSelected) { //allows switching between modes in SmartDashboard
      case kCenterCoral: //score coral when center of starting line
      default:
        centerCoralAuto();
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
    }
  }



//Called once at beginning of teleop period
  @Override
  public void teleopInit() {
    
  }



//Called repeatedly during teleop period
  @Override
  public void teleopPeriodic() {
    myDrive.feed();
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
      opposite = !opposite;
      // if (opposite == false) {
      //   opposite = true;
      // }
      // else {
      //   opposite = false;
      // }

    /*
     * Drive Controls
     */
    if (opposite) { //opposite == true
      myDrive.arcadeDrive(driverGamepad.getRightX()*driveSpeed/1.25, -driverGamepad.getLeftY()*driveSpeed);
    }
    else {
      myDrive.arcadeDrive(driverGamepad.getRightX()*driveSpeed/1.25, driverGamepad.getLeftY()*driveSpeed);
    }


    /*
     * Operator Controls
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
