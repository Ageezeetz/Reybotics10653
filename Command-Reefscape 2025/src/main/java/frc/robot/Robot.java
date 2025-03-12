package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

/**
 * The methods in this class are called automatically corresponding to each mode, as described in
 * the TimedRobot documentation. If you change the name of this class or the package after creating
 * this project, you must also update the Main.java file in the project.
 */
public class Robot extends TimedRobot {
  private Command m_autonomousCommand;

  private final RobotContainer m_robotContainer;

  /*
   * Robot init
   */
  public Robot() {
    // Instantiate our RobotContainer.  This will perform all our button bindings, and put our
    // autonomous chooser on the dashboard.sqd
    m_robotContainer = new RobotContainer();
  }

  /*
   * Robot periodic (mainly for prints/diagnostics)
   */
  @Override
  public void robotPeriodic() {
    // Runs the Scheduler.  This is responsible for polling buttons, adding newly-scheduled
    // commands, running already-scheduled commands, removing finished or interrupted commands,
    // and running subsystem periodic() methods.  This must be called from the robot's periodic
    // block in order for anything in the Command-based framework to work.
    CommandScheduler.getInstance().run();
  }

  /* 
   * Disabled init
   */
  @Override
  public void disabledInit() {}

  /*
   * Disabled periodic
   */
  @Override
  public void disabledPeriodic() {}

  /*
   * Autonomous init
   */
  /** This autonomous runs the autonomous command selected by your {@link RobotContainer} class. */
  @Override
  public void autonomousInit() {
    m_autonomousCommand = m_robotContainer.getAutonomousCommand();

    // schedule the autonomous command (example)
    if (m_autonomousCommand != null) {
      m_autonomousCommand.schedule();
    }
  }

  /*
  * Autonomous periodic
  */
  @Override
  public void autonomousPeriodic() {}

  /*
   * Teleop init
   */
  @Override
  public void teleopInit() {
    // This makes sure that the autonomous stops running when
    // teleop starts running. If you want the autonomous to
    // continue until interrupted by another command, remove
    // this line or comment it out.
    if (m_autonomousCommand != null) {
      m_autonomousCommand.cancel();
    }
  }

  /*
   * Teleop periodic
   */
  @Override
  public void teleopPeriodic() {}

  /*
   * Test init
   */
  @Override
  public void testInit() {
    // Cancels all running commands at the start of test mode.
    CommandScheduler.getInstance().cancelAll();
  }

  /*
   * Test periodic
   */
  @Override
  public void testPeriodic() {}

  /*
   * Simulation init
   */
  @Override
  public void simulationInit() {}

  /*
   * Simulation periodic
   */
  @Override
  public void simulationPeriodic() {}
}
