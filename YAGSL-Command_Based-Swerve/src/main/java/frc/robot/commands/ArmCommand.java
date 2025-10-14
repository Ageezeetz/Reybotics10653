/*package frc.robot.commands;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ArmSubsystem;

public class ArmCommand extends Command {
    private final ArmSubsystem arm;
    private final XboxController controller;


    public ArmCommand(ArmSubsystem arm, XboxController controller) { //uses argument DriveSubsystem and xbox controller
        this.arm = arm; //constant
        this.controller = controller; //constant
        addRequirements(arm); //prevents other commands from using subsystem given while this is running
    }

    @Override
    public void execute() { //called repeatedly every 20ms
        boolean controllerX = controller.getXButton();
        boolean controllerB = controller.getBButton();
        if (controllerX == true) {
            arm.lowerArm();
        }
        else if (controllerB == true) {
            arm.raiseArm();
        }
        else {
            arm.stop();
        }
    }

    @Override
    public void end(boolean interrupted) {
        arm.stop();
    }
}
*/
