/*
 * Autonomous center position command
 */

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.CoralSubsystem;
import frc.robot.subsystems.DriveSubsystem;

public class StraightAutoCommand extends Command {
    private final DriveSubsystem driveSubsystem;
    private CoralSubsystem coralSubsystem = new CoralSubsystem();

    private final double setpoint;

    public StraightAutoCommand(DriveSubsystem driveSubsystem, double setpoint) {
        this.driveSubsystem = driveSubsystem;
        this.setpoint = setpoint;
        addRequirements(driveSubsystem);
    }

    @Override
    public void execute() { //called repeatedly every 20ms
        driveSubsystem.straightCoralAuto(setpoint);
    }

    @Override
    public void end(boolean interrupted) {
        driveSubsystem.stop();
        coralSubsystem.stopMotor();
    }
}