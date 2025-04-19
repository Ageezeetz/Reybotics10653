package frc.robot;

import frc.robot.Constants.OperatorConstants;
import frc.robot.commands.Autos;
import frc.robot.commands.ExampleCommand;
import frc.robot.subsystems.ExampleSubsystem;
import frc.robot.subsystems.SwerveSubsystem;
import swervelib.SwerveInputStream;
import swervelib.telemetry.SwerveDriveTelemetry;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;

public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  private final ExampleSubsystem m_exampleSubsystem = new ExampleSubsystem();
  private final SwerveSubsystem drivebase = new SwerveSubsystem();

  // Replace with CommandPS4Controller or CommandJoystick if needed
  private final CommandXboxController m_driverController =
      new CommandXboxController(OperatorConstants.kDriverControllerPort);

  /** The container for the robot. Contains subsystems, OI devices, and commands. */
  public RobotContainer() {
    SwerveDriveTelemetry.verbosity = SwerveDriveTelemetry.TelemetryVerbosity.HIGH;

    drivebase.zeroGyro();

    configureBindings();
    drivebase.setDefaultCommand(driveFieldOrientedAngularVelocity); //makes sure it's always reading controller input and changing robot speeds
  }

  //calculates speed for robot depending on left joystick inputs and rotation values while scaling it down and applying alliance relativity
  SwerveInputStream driveAngularVelocity = SwerveInputStream.of(drivebase.getSwerveDrive(),
                                                                () -> m_driverController.getLeftY(), //gets controller left y input
                                                                () -> m_driverController.getLeftX()) //gets controller left x input
                                                                .withControllerRotationAxis(() -> m_driverController.getRightX() * -1)
                                                                .deadband(OperatorConstants.DEADBAND) //applies deadband
                                                                .scaleTranslation(0.7)
                                                                .scaleRotation(0.5)
                                                                .allianceRelativeControl(true);
                                                                //depending on which alliance the bot is on is the direction the bot will move by default

  //gets angle for robot depending on left joystick input; speed is not needed because it is already calculated above, just the angle
  SwerveInputStream driveDirectAngle = driveAngularVelocity.copy().withControllerHeadingAxis(() -> m_driverController.getRightX(), 
                                                                                             () -> m_driverController.getRightY())
                                                                                             .headingWhile(true);

  Command driveFieldOrientedDirectAngle = drivebase.driveFieldOriented(driveDirectAngle); //sends values to the driveFieldOriented function

  Command driveFieldOrientedAngularVelocity = drivebase.driveFieldOriented(driveAngularVelocity); //sends values to the driveFieldOriented function


  private void configureBindings() {
    // Schedule `ExampleCommand` when `exampleCondition` changes to `true`
    new Trigger(m_exampleSubsystem::exampleCondition)
        .onTrue(new ExampleCommand(m_exampleSubsystem));

    // Schedule `exampleMethodCommand` when the Xbox controller's B button is pressed,
    // cancelling on release.
    m_driverController.b().whileTrue(m_exampleSubsystem.exampleMethodCommand());
  }

  /**
   * Use this to pass the autonomous command to the main {@link Robot} class.
   *
   * @return the command to run in autonomous
   */
  public Command getAutonomousCommand() {
    // An example command will be run in autonomous
    return Autos.exampleAuto(m_exampleSubsystem);
  }
}
