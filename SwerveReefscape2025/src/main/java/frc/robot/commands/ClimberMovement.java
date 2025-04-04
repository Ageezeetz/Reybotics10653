package frc.robot.commands;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.ClimberSubsystem;

public class ClimberMovement extends Command {
    private final XboxController controller;
    private final ClimberSubsystem climberSubsystem;

    public ClimberMovement(ClimberSubsystem climberSubsystem, XboxController controller) {
        this.climberSubsystem = climberSubsystem;
        this.controller = controller;
        addRequirements(climberSubsystem);
    }

    public void execute() {
        if (controller.getRightY() > 0.1) { //right joy pointed up
            climberSubsystem.raiseClimber();
        }
        else if (controller.getRightY() < -0.1) { //right joy pointed down
            climberSubsystem.lowerClimber();
        }
        else {
            climberSubsystem.stop();
        }
    }
}
