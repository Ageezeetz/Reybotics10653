/*
 * Drive subsystem to take care of all driving mechanics
 */
package frc.robot.subsystems;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import static frc.robot.Constants.drivetrain.WHEEL_BASE;
import static frc.robot.Constants.drivetrain.TRACK_WIDTH;
import static frc.robot.Constants.drivetrain.MAX_SPEED;
import static frc.robot.Constants.drivetrain.MAX_ROTATION_SPEED;

public class DriveSubsystem extends SubsystemBase {
  //assigns sparkmaxes to specific modules as drive and turn motors (drive, turn)
  private final SwerveModule frontRight = new SwerveModule(1, 5);
  private final SwerveModule frontLeft = new SwerveModule(2, 6);
  private final SwerveModule backRight = new SwerveModule(3, 7);
  private final SwerveModule backLeft = new SwerveModule(4, 8);

  // private final AHRS gyro = new AHRS(SPI.Port.kMXP);


  //creates a plane and plots locations of all four modules to calculate perfect turning based on their locations
  private final SwerveDriveKinematics kinematics = new SwerveDriveKinematics(
    new Translation2d(WHEEL_BASE / 2, TRACK_WIDTH / 2), //front left
    new Translation2d(WHEEL_BASE / 2, -TRACK_WIDTH / 2), //front right
    new Translation2d(-WHEEL_BASE / 2, TRACK_WIDTH / 2), //back left
    new Translation2d(-WHEEL_BASE / 2, -TRACK_WIDTH / 2) //back right
  );

  public void resetEncoders() {
    frontRight.resetEncoder();
    frontLeft.resetEncoder();
    backRight.resetEncoder();
    backLeft.resetEncoder();
  }


  public double getAverageEncoderDistance() {
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
  }

  public void stop() { //sets the speed and rotation to 0 for all modules
    drive(new SwerveModuleState[] {
        new SwerveModuleState(0, new Rotation2d(0)),
        new SwerveModuleState(0, new Rotation2d(0)),
        new SwerveModuleState(0, new Rotation2d(0)),
        new SwerveModuleState(0, new Rotation2d(0))
    });
  }


  public void driveFromController(XboxController controller) {
    double xSpeed = -controller.getLeftY();
    double ySpeed = controller.getLeftX();
    double rotationSpeed = controller.getRightX();

    SwerveModuleState[] states = kinematics.toSwerveModuleStates( //calculates how each wheel should move to achieve correct motion
      new ChassisSpeeds(xSpeed * MAX_SPEED, ySpeed * MAX_SPEED, rotationSpeed * MAX_ROTATION_SPEED) //overall motion from the robot
    );
    //assigns "states" the value of speed and rotation, which is then applied to the modules

    SwerveDriveKinematics.desaturateWheelSpeeds(states, MAX_SPEED); //ensures that all wheels don't exceed max speed
    //if one wheel is above max speed, all wheels slow down so that it is below or equal to max speed while still going the same direction
    
    drive(states); //sends the new calculated speed values to the modules, changing their speed and rotation states
  }


  @Override
  public void periodic() {

  }


  @Override
  public void simulationPeriodic() {

  }
}
