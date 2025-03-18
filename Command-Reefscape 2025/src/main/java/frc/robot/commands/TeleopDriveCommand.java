/*
 * Robot movement commands
 */

package frc.robot.commands;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.DriveSubsystem;

public class TeleopDriveCommand extends Command {
    private final DriveSubsystem drive;
    private final XboxController controller;


    public TeleopDriveCommand(DriveSubsystem drive, XboxController controller) { //uses argument DriveSubsystem and xbox controller
        this.drive = drive; //constant
        this.controller = controller; //constant
        addRequirements(drive); //prevents other commands from using subsystem given while this is running
    }

    @Override
    public void execute() { //called repeatedly every 20ms
        double forward = controller.getLeftY();
        double rotation = controller.getRightX();
        drive.arcadeDrive(rotation, forward);
    }

    @Override
    public void end(boolean interrupted) {
        drive.stop();
    }
}
