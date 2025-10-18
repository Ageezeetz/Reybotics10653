package frc.robot;

import frc.robot.Constants.controllers;
import frc.robot.commands.Autos;
import frc.robot.commands.ClimberCommand;
// import frc.robot.commands.ExampleCommand;
import frc.robot.commands.IntakeCommand;
import frc.robot.subsystems.ArmSubsystem;
import frc.robot.subsystems.ClimberSubsystem;
import frc.robot.subsystems.ExampleSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.SwerveSubsystem;
import swervelib.SwerveInputStream;
// import swervelib.telemetry.SwerveDriveTelemetry;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj2.command.Command;

public class RobotContainer {
  private final IntakeSubsystem intakeSubsystem = new IntakeSubsystem(); //coral intake
  private final ClimberSubsystem climberSubsystem = new ClimberSubsystem(); //climber
  public final ArmSubsystem armSubsystem = new ArmSubsystem(); //climber
  public final XboxController driverController = new XboxController(controllers.driverPort); //driver controller
  public final XboxController operatorController = new XboxController(controllers.operatorPort); //operator controller
  
  private final ExampleSubsystem m_exampleSubsystem = new ExampleSubsystem();
  public final SwerveSubsystem drivebase = new SwerveSubsystem();

  public RobotContainer() {
    // SwerveDriveTelemetry.verbosity = SwerveDriveTelemetry.TelemetryVerbosity.HIGH;
    drivebase.zeroGyro();
  
    configureBindings();
  }

  //calculates speed for robot depending on left joystick inputs and rotation values while scaling it down and applying alliance relativity
  SwerveInputStream driveAngularVelocity = SwerveInputStream.of(drivebase.getSwerveDrive(),
                                                                () -> driverController.getLeftY(), //gets controller left y input
                                                                () -> driverController.getLeftX()) //gets controller left x input
                                                                .withControllerRotationAxis(() -> driverController.getRightX() * -1)
                                                                .deadband(controllers.DEADBAND) //applies deadband
                                                                .scaleTranslation(0.3)
                                                                .scaleRotation(0.2)
                                                                .allianceRelativeControl(true);
                                                                //depending on which alliance the bot is on is the direction the bot will move by default

  //gets angle for robot depending on left joystick input; speed is not needed because it is already calculated above, just the angle
  SwerveInputStream driveDirectAngle = driveAngularVelocity.copy().withControllerHeadingAxis(() -> driverController.getRightX(), 
                                                                                             () -> driverController.getRightY())
                                                                                             .headingWhile(true);

  Command driveFieldOrientedDirectAngle = drivebase.driveFieldOriented(driveDirectAngle); //sends values to the driveFieldOriented function

  Command driveFieldOrientedAngularVelocity = drivebase.driveFieldOriented(driveAngularVelocity); //sends values to the driveFieldOriented function


  private void configureBindings() {
  //  armSubsystem.setDefaultCommand(new armCommand(armSubsystem, driverController)); //command for arm
    intakeSubsystem.setDefaultCommand(new IntakeCommand(intakeSubsystem, operatorController)); //command for coral intake
    climberSubsystem.setDefaultCommand(new ClimberCommand(climberSubsystem, operatorController)); //command for climber
    drivebase.setDefaultCommand(driveFieldOrientedAngularVelocity); //makes sure it's always reading controller input and changing robot speeds
  }

  public Command getAutonomousCommand() {
    // An example command will be run in autonomous
    return Autos.exampleAuto(m_exampleSubsystem);
  }
}
