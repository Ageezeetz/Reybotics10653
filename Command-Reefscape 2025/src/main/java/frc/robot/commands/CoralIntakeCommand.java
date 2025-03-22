/*
* Coral intake commands
*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.CoralSubsystem;

public class CoralIntakeCommand extends Command {
    private final CoralSubsystem roller;
    private final XboxController controller;


    public CoralIntakeCommand(CoralSubsystem coralSubsystem, XboxController controller) { //uses the CoralSubsystem and an xbox controller
        this.roller = coralSubsystem; //constant
        this.controller = controller; //constant
        addRequirements(roller); //prevents other commands from using subsystem given while this is running
    }

    @Override
    public void execute() { //called repeatedly every 20ms
        double controllerPOV = controller.getPOV();
        if (controllerPOV == 0) {
            roller.dropCoral();
        }
        else if (controllerPOV == 180) {
            roller.rollInCoral();
        }
        else {
            roller.stopMotor();
        }
    }

    @Override
    public void end(boolean interrupted) {
        roller.stopMotor();
    }
}