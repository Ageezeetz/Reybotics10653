/*
 * Autonomous commands
 */

package frc.robot.commands;

import frc.robot.subsystems.CoralSubsystem;
import frc.robot.subsystems.DriveSubsystem;
import edu.wpi.first.wpilibj2.command.Command;

public final class Autos {
  /** Example static factory for an autonomous command. */
  public static Command straightCoralAuto(DriveSubsystem driveSubsystem, CoralSubsystem coralSubsystem, double distance) {
    return new StraightAutoCommand(driveSubsystem, coralSubsystem, distance);
  }

  private Autos() {
    throw new UnsupportedOperationException("This is a utility class!");
  }
}