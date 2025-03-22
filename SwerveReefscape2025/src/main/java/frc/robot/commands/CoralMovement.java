package frc.robot.commands;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.CoralSubsystem;

public class CoralMovement extends Command {
    private final XboxController controller;
    private final CoralSubsystem coralSubsystem;

    public CoralMovement(CoralSubsystem coralSubsystem, XboxController controller) {
        this.coralSubsystem = coralSubsystem;
        this.controller = controller;
        addRequirements(coralSubsystem);
    }

    public void execute() {
        if (controller.getPOV() == 0) {
            coralSubsystem.rollOut();
        }
        else if (controller.getPort() == 180) {
            coralSubsystem.rollIn();
        }
        else {
            coralSubsystem.stop();
        }
    }
}
