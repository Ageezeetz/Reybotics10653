package frc.robot.commands;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.SwerveSubsystem;

public class AutoDriveForward extends Command {
    private final SwerveSubsystem swerve; 
    private final Timer timer = new Timer();

    public AutoDriveForward(SwerveSubsystem swerve) {
        this.swerve = swerve; //needed to apply drivetrain values to change speeds (similar to myDrive.tankdrive(forward, rotation))
        addRequirements(swerve);
    }

    @Override
    public void initialize() { //resets and starts timer at beginning of command
        timer.reset();
        timer.start();
    }

    @Override
    public void execute() {
        if (timer.get() < 5) { //if timer is below 5 seconds, wait
            swerve.driveFieldOriented(new ChassisSpeeds(0, 0, 0)); //"myDrive.tankdrive(0,0)"
        }
        else if (timer.get() < 8) { //if timer is between 5 and 8 seconds, drive forward
            swerve.driveFieldOriented(new ChassisSpeeds(1, 0, 0)); //"myDrive.tankdrive(1,0)"
        }
        else { //if timer is above 8 seconds, stop
            swerve.driveFieldOriented(new ChassisSpeeds(0, 0, 0)); //"myDrive.tankdrive(0,0)"
        }
    }

    @Override
    public void end(boolean interrupted) {
        swerve.driveFieldOriented(new ChassisSpeeds(0, 0, 0)); //stop robot when command ends
    }

    @Override
    public boolean isFinished() {
        return timer.get() > 10; //stop when timer is above 15 seconds
    }
}
