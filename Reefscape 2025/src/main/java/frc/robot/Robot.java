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
  //custom modes for SmartDashboard
  private static final String kCenterCoral = "Center and Coral";
  private static final String kJustDrive = "Only Drive";
  private static final String kRightCoral = "Right Side Coral";
  private static final String kLeftCoral = "Left Side Coral";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  final SparkMax rollerMotor = new SparkMax(5, MotorType.kBrushless); //creates roller 
  final SparkMax climberMotor = new SparkMax(6, MotorType.kBrushless); //creates climber
  // final SparkMax algaeMotor = new SparkMax(7, MotorType.kBrushed); //creates algae
  // final SparkMax algaeWheels = new SparkMax(8, MotorType.kBrushed); //creates algae wheels

  final SparkMax leftLeader = new SparkMax(2, MotorType.kBrushless); //creates wheel variables
  final SparkMax leftFollower = new SparkMax(3, MotorType.kBrushless);
  final SparkMax rightLeader = new SparkMax(1, MotorType.kBrushless);
  final SparkMax rightFollower = new SparkMax(4, MotorType.kBrushless);

  final DifferentialDrive myDrive = new DifferentialDrive(leftLeader, rightLeader); //used for motor calls

  final SparkMaxConfig driveConfig = new SparkMaxConfig(); //creating setups for wheel, roller, and climber SparkMaxes 
  final SparkMaxConfig rollerConfig = new SparkMaxConfig();
  final SparkMaxConfig climberConfig = new SparkMaxConfig();
  // final SparkMaxConfig algaeConfig = new SparkMaxConfig();
  final EncoderConfig encoderConfig = new EncoderConfig().positionConversionFactor(2 * Math.PI * 3 / 8.45); //robot moves 2.23 in per rev

  final Timer timer1 = new Timer(); //new timer
  final Timer sleepTime = new Timer();

  final double ROLLER_SPEED = 0.25; //variable for roller strength
  final double CLIMBER_STRENGTH = 0.75; //variable for climber strength
  // final double ALGAE_STRENGTH = 0.75; //variable for algae speed when pulling/releasing
  // final double ALGAE_WHEEL_STRENGTH = 0.25; //variable for algae wheel speed
  double driveSpeed = 0; //variable for boost drive speed
  double opposite = -1; //variable for default robot direction
  double speedRate = 0;
  double setpoint = 0;
  boolean climbing = false;
  // boolean algaeMovementCheck = false;
  // boolean algaeMovementY = false;
  // boolean algaeMovementA = false;
  


  final XboxController driverGamepad = new XboxController(0);
  final XboxController operatorGamepad = new XboxController(1);

  RelativeEncoder leftEncoder = leftLeader.getEncoder();
  RelativeEncoder rightEncoder = rightLeader.getEncoder();
  RelativeEncoder climberEncoder = climberMotor.getEncoder();
  // RelativeEncoder algaeEncoder = algaeMotor.getEncoder();
  // RelativeEncoder algaeWheelEncoder = algaeWheels.getEncoder();

  double rightEncoderPos = rightEncoder.getPosition();
  double leftEncoderPos = leftEncoder.getPosition();
  double climberEncoderPos = climberEncoder.getPosition();
  // double algaeEncoderPos = algaeEncoder.getPosition();

  PIDController controller = new PIDController(0.02, 0, 0.005);
  double step = 1; //used to determine which step of auto robot is on

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

    driveConfig.encoder.apply(encoderConfig); 
    // algaeConfig.encoder.apply(encoderConfig);

    driveConfig.smartCurrentLimit(60); //sets amp limit for each motor
    driveConfig.voltageCompensation(12); //sends half of given voltage to motor to help keep driving consistent

    driveConfig.follow(leftLeader); //sets backleft motor to match top left motor
    leftFollower.configure(driveConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters); //configures back left motor

    driveConfig.follow(rightLeader); //sets backright motor to match top right motor
    rightFollower.configure(driveConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters); //configures back right motor

    driveConfig.disableFollowerMode();
    rightLeader.configure(driveConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters); //configures front right motor
    leftLeader.configure(driveConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters); //configures front left motor
    driveConfig.inverted(true); //flips motor number values

    rollerConfig.smartCurrentLimit(60);
    rollerConfig.voltageCompensation(10);
    rollerMotor.configure(rollerConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters); //configures roller motor

    climberConfig.smartCurrentLimit(60);
    climberConfig.voltageCompensation(10);
    climberMotor.configure(climberConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters); //configures climber motor

    // algaeConfig.smartCurrentLimit(60);
    // algaeConfig.voltageCompensation(10);
    // algaeMotor.configure(algaeConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters); //configures algae motor
    // algaeWheels.configure(algaeConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters); //configures algae wheel motor

    myDrive.setSafetyEnabled(safetyBool);
    myDrive.setExpiration(0.5);

    //if robot overshoots or jitters near target, increase; if robot stops too far from target, decrease
    controller.setTolerance(0.75); //adds a little bit of error in case the robot doesn't fully reach setpoint
  } 



  /*
   * Functions
   */
  private double getEncoderPositions() {
    return (leftEncoder.getPosition() + -(rightEncoder.getPosition())) / 2;
  }

  private void resetEncoders() {
    leftEncoder.setPosition(0);
    rightEncoder.setPosition(0);
    climberEncoder.setPosition(0);
  }
  
  private void boostToggle() {
    if (speedRate == 1) {
      driveSpeed = 0.95;
    }
    else {
      driveSpeed = 0.85;
    }
  }

  private void straightCoralAuto(double distanceToSetpoint) {
    if (step == 1) {
      sleepTime.start();
      if (sleepTime.get() > 8) {
        step++;
        sleepTime.stop();
        sleepTime.reset();
      }
      myDrive.feed();
    }
    else if (step == 2) {
      controller.setSetpoint(distanceToSetpoint); //setpoint is direction of roller
      double output = controller.calculate(getEncoderPositions());
      myDrive.tankDrive(output, -output); //goes in the direction of the roller 
      myDrive.feed();
      sleepTime.start();
      if (controller.atSetpoint() || sleepTime.get() > 3) {
        step++;
        sleepTime.stop();
        sleepTime.reset();
      }
    }
    else if (step == 3) {
      sleepTime.start();
      myDrive.tankDrive(0, 0); //stop moving
      myDrive.feed();
      rollerMotor.set(-ROLLER_SPEED); //deposit
      if (sleepTime.get() > 1.5) {
        step++;
        rollerMotor.set(0); //stop once the roller has ran for 5 seconds
        sleepTime.stop();
        sleepTime.reset();
      }
    }
    else {
      myDrive.arcadeDrive(0, 0); //stop
      rollerMotor.set(0);
      myDrive.feed();
    }
  }

  // private void algaeIntakeMovement() {
  //   if (operatorGamepad.getYButtonPressed()) {
  //     algaeMovementY = true;
  //   }
  //   else if (operatorGamepad.getAButtonPressed()) {
  //     algaeMovementA = true;
  //   }

  //   if (algaeMovementY) {
  //     if (algaeEncoderPos != 10) { //if intake has not gone down
  //       algaeMotor.set(ALGAE_STRENGTH); //roll down
  //     }
  //     else if (algaeEncoderPos >= 10) { //if intake is down or lower
  //       algaeMotor.set(0); //stop
  //       algaeMovementY = false;
  //     }
  //     if (operatorGamepad.getAButtonPressed()) {
  //       algaeMovementY = false;
  //     }
  //   }
  //   if (algaeMovementA) {
  //     if (algaeEncoderPos != 0) { //if intake is not fully up
  //       algaeMotor.set(-ALGAE_STRENGTH); //roll up
  //     }
  //     else if (algaeEncoderPos <= 0) { //else if intake is fully up or more
  //       algaeMotor.set(0); //stop
  //       algaeMovementA = false;
  //     }
  //     if (operatorGamepad.getYButtonPressed()) {
  //       algaeMovementA = false;
  //     }
  //   }
  // }



  //   if (operatorGamepad.getYButtonPressed()) {
  //     // if (algaeIntakeDirection.equals("up")) { //if algae intake is up
  //       if (algaeEncoderPos != 10) { //if intake has not gone down
  //         algaeMotor.set(ALGAE_STRENGTH); //roll down
  //       }
  //       else if (algaeEncoderPos >= 10) { //else if intake is down or lower
  //         algaeMotor.set(0); //stop
  //       }
  //     // }
  //   }
  //   else if (operatorGamepad.getAButtonPressed()) {
  //     // if (algaeIntakeDirection.equals("down")) { //if algae intake is down
  //       if (algaeEncoderPos != 0) { //if intake is not fully up
  //         algaeMotor.set(-ALGAE_STRENGTH); //roll up
  //       }
  //       else if (algaeEncoderPos <= 0) { //else if intake is fully up or more
  //         algaeMotor.set(0); //stop
  //       }
  //     // }
  //   }
  // }



  @Override
  public void robotPeriodic() {
    /*
     * Prints
     */
    SmartDashboard.putNumber("Left Encoder Position", leftEncoder.getPosition()); //left encoder
    SmartDashboard.putNumber("Right Encoder Position", -(rightEncoder.getPosition())); //right encoder
    SmartDashboard.putNumber("Encoder Positions", getEncoderPositions()); //average of both encoders
    SmartDashboard.putNumber("PID Output", controller.calculate(getEncoderPositions())); //power output of PID
    SmartDashboard.putBoolean("Controller at Target", controller.atSetpoint()); //true or false
    // SmartDashboard.putNumber("Algae Intake Position", algaeEncoder.getPosition()); //algae intake encoder value
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
    /*
    * Selections for auto modes \o/
    */
    // if (algaeEncoderPos > 0) { //if algae intake is out, reel it back in to protect from hits
    //   algaeMotor.set(ALGAE_STRENGTH); //roll in
    // }
    // else { //if algae intake is in
    //   algaeMotor.set(0); //stop
    // }
    

    switch (m_autoSelected) { //allows switching between modes in SmartDashboard
      case kCenterCoral: //score coral when center of starting line
      default:
        straightCoralAuto(-76);
        break;

      case kRightCoral: //score coral when right side of starting line
      // default:
        straightCoralAuto(-125); //prolly 150
        break;

      case kLeftCoral: //score coral when left side of starting line
        straightCoralAuto(-125); //prolly 150
        break;

      case kJustDrive: //gets out of starting zone
        straightCoralAuto(-25);
        break;
    }
  }



//Called once at beginning of teleop period
  @Override
  public void teleopInit() {}



//Called repeatedly during teleop period
  @Override
  public void teleopPeriodic() {
    /*
     * Speed Change
     */
    if (driverGamepad.getLeftBumperButtonPressed()) { //if left bumper is pressed
      if (speedRate == 1) { //if speedrate is 1
        speedRate--; //disable boost
        System.out.println("Boost is disabled");
      }
      else { //if speedrate is 0
        speedRate++; //enable boost
        System.out.println("Boost is enabled!");
      }
    }

    boostToggle();



    /*
     * Flip Direction Toggle
     */
    if (driverGamepad.getRightBumperButtonPressed()) { //if right bumper is pressed
      opposite *= -1; //used to reverse robot drive direction (inverts opposite)

      //random prints
      if (opposite == 1) {
        System.out.println("Climber is forward");
      }
      else {
        System.out.println("Roller is forward");
      }
    }


    
    /*
     * Roller Controls
     */
    if (operatorGamepad.getPOV() == 180) { //bottom D-pad button pressed
      rollerMotor.set(ROLLER_SPEED); //roll in
    }
    else if (operatorGamepad.getPOV() == 0) { //top D-pad button pressed
      rollerMotor.set(-ROLLER_SPEED); //roll out
    }
    else {
      rollerMotor.set(0);
    }



    /*
    * Climber Controls
    */
    if (-operatorGamepad.getRightY() < -0.1) { //if joystick is down,
      climberMotor.set(-CLIMBER_STRENGTH); //pull climber
    }
    else if (-operatorGamepad.getRightY() > 0.1) { //if joystick is up,
      climberMotor.set(CLIMBER_STRENGTH); //lower climber
    }
    else {
      climberMotor.set(0);
    }



    /*
    * Algae Intake Movement Controls
    */
    // algaeIntakeMovement();



    /*
     * Algae Wheel Controls
     */
    // if (operatorGamepad.getRightTriggerAxis() >= 0.1) { //if the right trigger is pressed
    //   algaeWheels.set(-ALGAE_WHEEL_STRENGTH); //push out algae
    // }
    // else if (operatorGamepad.getLeftTriggerAxis() >= 0.1) { //if left trigger is pressed
    //   algaeWheels.set(ALGAE_WHEEL_STRENGTH); //suck in algae
    // }
    // else { //if nothing is pressed
    //   algaeWheels.set(0); //stop wheels
    // }


    /*
     * Drive Controls
     */
    myDrive.arcadeDrive(driverGamepad.getRightX()*driveSpeed/1.25, driverGamepad.getLeftY()*driveSpeed*opposite);
    myDrive.feed();
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
