package frc.robot.commands;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.IntakeSubsystem;

public class IntakeCommand extends Command {
    private final IntakeSubsystem roller;
    private final XboxController controller;


    public IntakeCommand(IntakeSubsystem intakeSubsystem, XboxController controller) { //uses the CoralSubsystem and an xbox controller
        this.roller = intakeSubsystem; //constant
        this.controller = controller; //constant
        addRequirements(roller); //prevents other commands from using subsystem given while this is running
    }

    @Override
    public void execute() { //called repeatedly every 20ms
        if (controller.getPOV() == 0) {
            roller.rollOut();
        }
        else if (controller.getPOV() == 180) {
            roller.rollIn();
        }
        else {
            roller.stop();
        }
    }

    @Override
    public void end(boolean interrupted) {
        roller.stop();
    }
}