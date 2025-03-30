/*
 * Drive subsystem to take care of all driving mechanics
 */
package frc.robot.subsystems;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModuleState;
// import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import static frc.robot.Constants.drivetrain;
import static frc.robot.Constants.drivetrain.MAX_SPEED;
import static frc.robot.Constants.drivetrain.MAX_ROTATION_SPEED;

public class DriveSubsystem extends SubsystemBase {
  //assigns sparkmaxes to specific modules as drive and turn motors (drive, turn)
  private final SwerveModule frontRight = new SwerveModule(drivetrain.SPARKMAX_ID_TOPRIGHT_DRIVE, drivetrain.SPARKMAX_ID_TOPRIGHT_TURN);
  private final SwerveModule frontLeft = new SwerveModule(drivetrain.SPARKMAX_ID_TOPLEFT_DRIVE, drivetrain.SPARKMAX_ID_TOPLEFT_TURN);
  private final SwerveModule backRight = new SwerveModule(drivetrain.SPARKMAX_ID_BOTTOMRIGHT_DRIVE, drivetrain.SPARKMAX_ID_BOTTOMRIGHT_TURN);
  private final SwerveModule backLeft = new SwerveModule(drivetrain.SPARKMAX_ID_BOTTOMLEFT_DRIVE, drivetrain.SPARKMAX_ID_BOTTOMLEFT_TURN);

  // private final AHRS gyro = new AHRS(SPI.Port.kMXP);


  //creates a plane and plots locations of all four modules to calculate perfect turning based on their locations
  private final SwerveDriveKinematics kinematics = new SwerveDriveKinematics( //grid top left = +x, +y
    new Translation2d(drivetrain.WHEEL_BASE / 2, drivetrain.TRACK_WIDTH / 2), //front left
    new Translation2d(drivetrain.WHEEL_BASE / 2, -drivetrain.TRACK_WIDTH / 2), //front right
    new Translation2d(-drivetrain.WHEEL_BASE / 2, drivetrain.TRACK_WIDTH / 2), //back left
    new Translation2d(-drivetrain.WHEEL_BASE / 2, -drivetrain.TRACK_WIDTH / 2) //back right
  );

  public void resetEncoders() { //used for auto
    frontRight.resetEncoder();
    frontLeft.resetEncoder();
    backRight.resetEncoder();
    backLeft.resetEncoder();
  }


  public double getAverageEncoderDistance() { //used for auto
    return (frontRight.getPosition() + 
            frontLeft.getPosition() + 
            backRight.getPosition() +
            backLeft.getPosition()) / 4.0;
  }


  public void drive(SwerveModuleState[] states) {
    //setState sets the correct speed and angle based on the calculations below
    frontLeft.setState(states[0]); //state 0 is front left wheel
    frontRight.setState(states[1]); //state 1 is front right wheel
    backLeft.setState(states[2]); //state 2 is back left wheel
    backRight.setState(states[3]); //state 3 is back right wheel

    //since SwerveModuleState[] makes an array, when assigning it a value, you have to call it in a similar array format like below
  }


  public void stop() { //sets the speed and rotation to 0 for all modules
    drive(new SwerveModuleState[] { //configures everything inside the {} as an array, with their indexes being the specified modules above
        new SwerveModuleState(0, new Rotation2d(0)), //speed and rotation value for frontLeft module
        new SwerveModuleState(0, new Rotation2d(0)), //speed and rotation value for frontRight module
        new SwerveModuleState(0, new Rotation2d(0)), //speed and rotation value for backLeft module
        new SwerveModuleState(0, new Rotation2d(0)) //speed and rotation value for backRight module
    });
  }


  public void driveFromController(XboxController controller) {
    double xSpeed = -controller.getLeftY();
    double ySpeed = controller.getLeftX();
    double rotationSpeed = controller.getRightX();

      //calculates speed and rotation based on the left y (speed), left x (side2side movement), and right x (rotation)
      //and limits it to the percentage from MAX_SPEED and MAX_ROTATION_SPEED
      SwerveModuleState[] states = kinematics.toSwerveModuleStates(
      new ChassisSpeeds(xSpeed * MAX_SPEED, ySpeed * MAX_SPEED, rotationSpeed * MAX_ROTATION_SPEED)
      //applies the calculations and formats them to be used in x and y instead of x, y, and z
    );
    //module state values are now that of ChassisSpeeds, which includes forward, side, and rotation speeds, all turned into speed and rotation

    SwerveDriveKinematics.desaturateWheelSpeeds(states, MAX_SPEED); //ensures that all wheels don't exceed max speed
    //if one wheel is above max speed, all wheels slow down so that it is below or equal to max speed while still going the same direction
    
    drive(states); //sends the new speed and rotation calculated values to the modules
    //modules are set to only accept two values; speed and rotation, which are calculated above
  }


  @Override
  public void periodic() {
    SmartDashboard.putNumber("Left Side Encoder Positions", (frontLeft.getPosition() + backLeft.getPosition()) / 2);
    SmartDashboard.putNumber("Right Side Encoder Positions", (frontRight.getPosition() + backRight.getPosition()) / 2);
    SmartDashboard.putNumber("Average Encoder Positions", getAverageEncoderDistance());
  }


  @Override
  public void simulationPeriodic() {

  }
}
