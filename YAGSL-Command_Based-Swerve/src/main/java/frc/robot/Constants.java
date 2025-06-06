package frc.robot;

import edu.wpi.first.math.util.Units;


public final class Constants {
  public static class swerve {
    public static final double MAX_SPEED = Units.feetToMeters(6);
  }

  public static class controllers {
    public static final int driverPort = 0;
    public static final int operatorPort = 1;
    public static final double DEADBAND = 0.05;
  }

  public static class intake {
    public static final double ROLLER_SPEED = 0.5;
    public static final double STOP_MOTOR = 0;
  }

  public static class climber {
    public static final double CLIMBER_STRENGTH = 0.75;
    public static final double STOP_MOTOR = 0;
  }
}
