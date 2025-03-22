package frc.robot.commands;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.AlgaeSubsystem;

public class AlgaeMovement extends Command {
    private final XboxController controller;
    private final AlgaeSubsystem algaeSubsystem;

    public AlgaeMovement(AlgaeSubsystem algaeSubsystem, XboxController controller) {
        this.algaeSubsystem = algaeSubsystem;
        this.controller = controller;
        addRequirements(algaeSubsystem);
    }

    public void execute() {
        if (controller.getRightY() > 0.1) {
            algaeSubsystem.algaeDown();
        }
        else if (controller.getRightY() < -0.1) {
            algaeSubsystem.algaeUp();
        }
        else {
            algaeSubsystem.stop();
        }
    }
}
