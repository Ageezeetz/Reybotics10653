package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide numerical or boolean
 * constants. This class should not be used for any other purpose. All constants should be declared
 * globally (i.e. public static). Do not put anything functional in this class.
 *
 * <p>It is advised to statically import this class (or one of its inner classes) wherever the
 * constants are needed, to reduce verbosity.
 */
public final class Constants {
  public static final int DRIVER_CONTROLLER_PORT = 0;
  public static final int OPERATOR_CONTROLLER_PORT = 1;
  public final static int STOP_MOTOR = 0;
  public final static double CENTER_AUTO_DISTANCE = -76;
  public final static double CORNER_AUTO_DISTANCE = -125;

  public static class driveConstants {
    public static final double NORMAL_SPEED = 0.85;
    public static final double BOOST_SPEED = 0.95;
  }

  public static class coralConstants {
    public final static double ROLLER_SPEED = 0.25;
  }

  public static class climberConstants {
    public final static double CLIMBER_STRENGTH = 0.75;
  }
  
  public static class algaeConstants {
    public final static double ALGAE_STRENGTH = 0.50;
    public final static double ALGAE_WHEEL_STRENGTH = 0.40; 
  }
}
