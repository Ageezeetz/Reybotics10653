package frc.robot.commands.Autos;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.CoralSubsystem;
import frc.robot.subsystems.DriveSubsystem;

public class StraightAuto extends Command {
    private final DriveSubsystem swerveDrive;
    private final CoralSubsystem coralSubsystem;
    private final PIDController pidcontroller;
    private final Timer waitTimer = new Timer();

    private final double distance;
    private int step = 1;

    public StraightAuto(DriveSubsystem swerveDrive, CoralSubsystem coralSubsystem, double distance) {
        this.swerveDrive = swerveDrive;
        this.coralSubsystem = coralSubsystem;
        this.distance = distance;
        this.pidcontroller = new PIDController(0.5, 0, 0.02);
        pidcontroller.setTolerance(0.1);

        addRequirements(swerveDrive, coralSubsystem);
    }

    public void initialize() {
        waitTimer.reset();
        waitTimer.start();
        swerveDrive.resetEncoders();
    }

    public void execute() {
        switch (step) {
            case 1: 
                if (waitTimer.get() > 3) {
                    step++;
                    waitTimer.reset();
                }
                break;
            case 2:
                pidcontroller.setSetpoint(distance);
                double speed = pidcontroller.calculate(swerveDrive.getAverageEncoderDistance());
                speed = Math.max(Math.min(speed, 0.75), Math.min(speed, -0.75)); //sets speed limit of 0.75
                SwerveModuleState[] states = new SwerveModuleState[] { //sets speed and rotation for each wheel
                    new SwerveModuleState(speed, new Rotation2d(0)),
                    new SwerveModuleState(speed, new Rotation2d(0)),
                    new SwerveModuleState(speed, new Rotation2d(0)),
                    new SwerveModuleState(speed, new Rotation2d(0))
                };

                swerveDrive.drive(states); //applies the new speeds and rotations

                if (pidcontroller.atSetpoint() || waitTimer.get() > 4) {
                    step++;
                    waitTimer.reset();
                }
                break;
            case 3:
                swerveDrive.stop();
                coralSubsystem.rollOut();

                if (waitTimer.get() > 2) {
                    step++;
                    coralSubsystem.stop();
                    waitTimer.stop();
                }
                break;
            default:
                swerveDrive.stop();
                coralSubsystem.stop();
                break;
        }
    }

    public void end(boolean interrupted) {
        swerveDrive.stop();
        coralSubsystem.stop();
    }

    public boolean isFinished() {
        return step > 3;
    }
}
