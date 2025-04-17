package frc.robot.subsystems;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

import java.io.File;
import java.util.function.Supplier;

import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import swervelib.parser.SwerveParser;
import swervelib.SwerveDrive;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;

import static edu.wpi.first.units.Units.Meter;

public class SwerveSubsystem extends SubsystemBase {
  File swerveDirectory = new File(Filesystem.getDeployDirectory(), "swerve");
  SwerveDrive swerveDrive;

  public SwerveSubsystem() {
        try
    {
      swerveDrive = new SwerveParser(swerveDirectory).createSwerveDrive(Constants.MAX_SPEED, 
                                                                        new Pose2d(new Translation2d(Meter.of(0), 
                                                                                                     Meter.of(2)),
                                                                                  Rotation2d.fromDegrees(0)));
      //alternative method if you don't want to supply the conversion factor via JSON files.
      // swerveDrive = new SwerveParser(directory).createSwerveDrive(maximumSpeed, angleConversionFactor, driveConversionFactor);
    }
    catch (Exception e)
    {
      throw new RuntimeException(e);
    }
    // swerveDrive.setHeadingCorrection(false); //heading correction should only be used while controlling the robot via angle
    // swerveDrive.setCosineCompensator(false); //disables cosine compensation for simulations since it causes discrepancies not seen in real life
    // swerveDrive.setAngularVelocityCompensation(true,
    //                                            true,
    //                                            0.1); //Correct for skew that gets worse as angular velocity increases. Start with a coefficient of 0.1.
    // swerveDrive.setModuleEncoderAutoSynchronize(false,
    //                                             1); // Enable if you want to resynchronize your absolute encoders and motor encoders periodically when they are not moving.
    // //deprecated swerveDrive.pushOffsetsToEncoders(); // Set the absolute encoder to be used over the internal encoder and push the offsets onto it. Throws warning if not possible

  }


  @Override
  public void periodic() {
    SmartDashboard.putNumber("Drive Encoder Values", swerveDrive.getFieldVelocity().vxMetersPerSecond);
    SmartDashboard.putNumber("Angle Encoder Value", swerveDrive.getMaximumChassisAngularVelocity());
    // SmartDashboard.putNumber("Absolute Encoder Value", swerveDrive.get);
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
}
