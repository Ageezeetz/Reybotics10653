/*
 * Main robot file
 */

package frc.robot;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import frc.robot.subsystems.DriveSubsystem;
import frc.robot.commands.TeleopDriveCommand;

import frc.robot.commands.Autos;
import frc.robot.commands.ClimberCommand;
import frc.robot.commands.CoralIntakeCommand;
import frc.robot.subsystems.ClimberSubsystem;
import frc.robot.subsystems.CoralSubsystem;
// import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
// import edu.wpi.first.wpilibj2.command.button.JoystickButton;
// import edu.wpi.first.wpilibj2.command.button.POVButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;



/*
 * Robot Declarations (calls/inits)
 */
public class RobotContainer {
  private final CoralSubsystem coralSubsystem = new CoralSubsystem(); //coral intake
  private final DriveSubsystem driveSubsystem = new DriveSubsystem(coralSubsystem); //movement + auto
  private final ClimberSubsystem climberSubsystem = new ClimberSubsystem(); //climber
  private final XboxController driverController = new XboxController(Constants.DRIVER_CONTROLLER_PORT); //driver controller
  private final XboxController operatorController = new XboxController(Constants.OPERATOR_CONTROLLER_PORT); //operator controller


  //Subsystems and Commands
  public RobotContainer() {
    driveSubsystem.setDefaultCommand(new TeleopDriveCommand(driveSubsystem, driverController)); //command for movement, always runs
    coralSubsystem.setDefaultCommand(new CoralIntakeCommand(coralSubsystem, operatorController)); //command for coral intake
    climberSubsystem.setDefaultCommand(new ClimberCommand(climberSubsystem, operatorController)); //command for climber

    configureBindings();
  }


   /*
    * Configure controller -> commands bindings
    */
  private void configureBindings() {
    new Trigger(() -> driverController.getRightBumper()) //if right bumper is pressed
        .onTrue(new InstantCommand(() -> driveSubsystem.robotDirection())); //switch directions


    new Trigger(() -> driverController.getLeftBumper()) //if left bumper is pressed
        .onTrue(new InstantCommand(() -> driveSubsystem.boostMode())); //toggle boost mode
        

    //new JoystickButton(driverController, XboxController.Button.kA.value)
    //new Trigger(() -> driverController.getRightBumper())
    //new POVButton(driverController, 0)

    //.onTrue = when pressed (happens once)
    //.onFalse = when released
    //.whileTrue = when held (happens repeatedly)
    //.whileFalse = when not held
  }



  public Command getAutonomousCommand() {
    return Autos.straightCoralAuto(driveSubsystem, coralSubsystem, Constants.CENTER_AUTO_DISTANCE);
  }
}
