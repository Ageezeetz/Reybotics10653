package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.CoralSubsystem;

public class CoralRollOut extends Command {
    private final CoralSubsystem coralSubsystem;

    public CoralRollOut(CoralSubsystem coralSubsystem) {
        this.coralSubsystem = coralSubsystem;
        addRequirements(coralSubsystem);
    }

    @Override
    public void initialize() {
        coralSubsystem.rollOut();
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
