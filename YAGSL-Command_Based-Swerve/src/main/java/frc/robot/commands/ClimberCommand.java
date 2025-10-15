package frc.robot.commands;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ClimberSubsystem;

public class ClimberCommand extends Command {
    private final ClimberSubsystem climber;
    private final XboxController controller;


    public ClimberCommand(ClimberSubsystem climber, XboxController controller) { //uses argument DriveSubsystem and xbox controller
        this.climber = climber; //constant
        this.controller = controller; //constant
        addRequirements(climber); //prevents other commands from using subsystem given while this is running
    }

    @Override
    public void execute() { //called repeatedly every 20ms
        double controllerRightTrig = controller.getRightTriggerAxis();
        double controllerLeftTrig = controller.getLeftTriggerAxis();
        if (controllerRightTrig > 0.02) { //if controller right trigger is pressed
            climber.lowerClimber(); //lower climber
        }
        else if (controllerLeftTrig > 0.02) { //if controller left trigger is pressed
            climber.raiseClimber(); //raise climber
        }
        else {
            climber.stop();
        }
    }

    @Override
    public void end(boolean interrupted) {
        climber.stop();
    }
}
