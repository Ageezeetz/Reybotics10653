package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import java.io.File;
import java.util.function.Supplier;

// import edu.wpi.first.wpilibj.smartdashboard.Field2d;

import edu.wpi.first.wpilibj.Filesystem;
// import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import swervelib.parser.SwerveParser;
import swervelib.SwerveDrive;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import frc.robot.Constants.swerve;

import static edu.wpi.first.units.Units.Meter;

public class SwerveSubsystem extends SubsystemBase {
  File swerveDirectory = new File(Filesystem.getDeployDirectory(), "swerve");
  SwerveDrive swerveDrive;

  // private final Field2d field = new Field2d();



  public SwerveSubsystem() {
    try
    {
      swerveDrive = new SwerveParser(swerveDirectory).createSwerveDrive(swerve.MAX_SPEED, 
                                                                        new Pose2d(new Translation2d(Meter.of(0), 
                                                                                                     Meter.of(2)),
                                                                                  Rotation2d.fromDegrees(0)));
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
    // swerveDrive.setHeadingCorrection(false); //heading correction should only be used while controlling the robot via angle
    // swerveDrive.setCosineCompensator(false); //disables cosine compensation for simulations since it causes discrepancies not seen in real life
    swerveDrive.setAngularVelocityCompensation(true,
                                               true,
                                               0.1); //Correct for skew that gets worse as angular velocity increases. Start with a coefficient of 0.1.
    swerveDrive.setModuleEncoderAutoSynchronize(false,
                                                1); // Enable if you want to resynchronize your absolute encoders and motor encoders periodically when they are not moving.
  }


  @Override
  public void periodic() {
    // swerveDrive.updateOdometry(); //updates the robot's position and angle on the field

    // field.setRobotPose(swerveDrive.getPose()); //updates robot's position on sim
    // SmartDashboard.putData("Field", field); //sends to dashboard

    // SmartDashboard.putNumber("Robot X Position", swerveDrive.getPose().getX());
    // SmartDashboard.putNumber("Robot Y Position", swerveDrive.getPose().getY());
    // SmartDashboard.putNumber("Robot Heading", swerveDrive.getPose().getRotation().getDegrees());
    // SmartDashboard.putNumber("Drive Encoder Values", swerveDrive.getFieldVelocity().vxMetersPerSecond);
    // SmartDashboard.putNumber("Angle Encoder Value", swerveDrive.getMaximumChassisAngularVelocity()); 
  }

  public SwerveDrive getSwerveDrive() {
    return swerveDrive;
  }

  public void driveFieldOriented(ChassisSpeeds velocity) {
    swerveDrive.driveFieldOriented(velocity);
  }

  public Command driveFieldOriented(Supplier<ChassisSpeeds> velocity) { //receives both values regarding speed and angle
    return run(() -> {
      swerveDrive.driveFieldOriented(velocity.get());
    });
  }

  public void zeroGyro() {
    swerveDrive.zeroGyro();
  }
}
