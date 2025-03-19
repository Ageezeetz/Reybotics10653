package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.CoralSubsystem;
import frc.robot.subsystems.DriveSubsystem;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.math.controller.PIDController;

public class StraightAutoCommand extends Command {
    private final DriveSubsystem driveSubsystem;
    private final CoralSubsystem coralSubsystem;
    private final PIDController controller;
    private final Timer autoTimer = new Timer();

    private final double setpoint;
    private int step = 1;

    public StraightAutoCommand(DriveSubsystem driveSubsystem, CoralSubsystem coralSubsystem, double setpoint) {
        this.driveSubsystem = driveSubsystem;
        this.coralSubsystem = coralSubsystem;
        this.setpoint = setpoint;
        this.controller = new PIDController(0.02, 0, 0.005);
        controller.setTolerance(0.75);
        
        addRequirements(driveSubsystem, coralSubsystem);
    }

    @Override
    public void initialize() {
        autoTimer.reset();
        autoTimer.start();
    }

    @Override
    public void execute() {
        switch (step) {
            case 1:
                if (autoTimer.get() > 8) {
                    step++;
                    autoTimer.reset();
                }
                break;
            case 2:
                controller.setSetpoint(setpoint);
                double output = controller.calculate(driveSubsystem.getEncoderPositions());
                driveSubsystem.tankDrive(output, -output);
                if (controller.atSetpoint() || autoTimer.get() > 3) {
                    step++;
                    autoTimer.reset();
                }
                break;
            case 3:
                driveSubsystem.stop();
                coralSubsystem.dropCoral();
                if (autoTimer.get() > 1.5) {
                    step++;
                    coralSubsystem.stopMotor();
                    autoTimer.stop();
                }
                break;
            default:
                coralSubsystem.stopMotor();
                driveSubsystem.stop();
                break;
        }
    }

    @Override
    public void end(boolean interrupted) {
        driveSubsystem.stop();
        coralSubsystem.stopMotor();
    }
}
