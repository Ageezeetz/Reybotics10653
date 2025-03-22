package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.CoralSubsystem;

public class CoralRollIn extends Command {
    private final CoralSubsystem coralSubsystem;

    public CoralRollIn(CoralSubsystem coralSubsystem) {
        this.coralSubsystem = coralSubsystem;
        addRequirements(coralSubsystem);
    }

    @Override
    public void initialize() {
        coralSubsystem.rollIn();
    }

    @Override
    public void end(boolean interrupted) {
        coralSubsystem.stop();
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
