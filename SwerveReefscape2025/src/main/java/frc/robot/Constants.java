package frc.robot;

public final class Constants {

  public static class drivetrain {
    public static final double TRACK_WIDTH = 0.5 * 0.0254; //distance from center of left and right modules from inches to meters
    public static final double WHEEL_BASE = 0.5 * 0.0254; //distance from center of front and back modules from inches to meters
    public static final double MAX_SPEED = 3.0; //meters per second
    public static final double MAX_ROTATION_SPEED = 2.0;
    public static final double WHEEL_DIAMETER_INCHES = 3;
    public static final int GEAR_RATIO = 1;

  }
  public static class driver {
    public static final int DRIVER_CONTROLLER_PORT = 0;
  }
  public static class operator {
    public static final int OPERATOR_CONTROLLER_PORT = 1;
  }
  public static class coral {
    public static final double ROLLER_SPEED = 0.25;
  }
  public static class algae {
    public static final double ALGAE_SPEED = 0.5;
    public static final double ALGAE_WHEEL_SPEED = 0.25;
  }
}
