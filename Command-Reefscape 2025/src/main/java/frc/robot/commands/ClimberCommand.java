/*
* Climber commands
*/

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
        double climberMovement = controller.getRightY();
        if (climberMovement > 0.1) {
            climber.lowerClimber();
        }
        else if (climberMovement < -0.1) {
            climber.raiseClimber();
        }
        climber.stopMotor();
    }

    @Override
    public void end(boolean interrupted) {
        climber.stopMotor();
    }
}
