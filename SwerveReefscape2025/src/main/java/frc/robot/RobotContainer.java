package frc.robot;

import frc.robot.commands.Autos.StraightAuto;
import frc.robot.commands.CoralRollIn;
import frc.robot.commands.CoralRollOut;
import frc.robot.subsystems.CoralSubsystem;
import frc.robot.subsystems.DriveSubsystem;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import edu.wpi.first.wpilibj.XboxController;
import static frc.robot.Constants.driver.DRIVER_CONTROLLER_PORT;
import static frc.robot.Constants.operator.OPERATOR_CONTROLLER_PORT;

public class RobotContainer {
  private final CoralSubsystem coralSubsystem = new CoralSubsystem();

  private final DriveSubsystem driveSubsystem = new DriveSubsystem();
  private final XboxController driverController = new XboxController(DRIVER_CONTROLLER_PORT);
  private final XboxController operatorController = new XboxController(OPERATOR_CONTROLLER_PORT);

  public RobotContainer() {
    configureBindings();
  }

  private void configureBindings() {
    driveSubsystem.setDefaultCommand(
      new RunCommand(() -> driveSubsystem.driveFromController(driverController), driveSubsystem));

    new Trigger(() -> operatorController.getPOV() == 0)
      .whileTrue(new CoralRollOut(coralSubsystem));

    new Trigger(() -> operatorController.getPOV() == 180)
      .whileTrue(new CoralRollIn(coralSubsystem));
  }

  public Command getAutonomousCommand() {
    return new StraightAuto(driveSubsystem, coralSubsystem, 10.0);
  }
}
