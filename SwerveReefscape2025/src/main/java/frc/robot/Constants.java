package frc.robot;

public final class Constants {

  public static class drivetrain {
    public static final double TRACK_WIDTH = 23.5 * 0.0254; //distance from center of left and right modules from inches to meters
    public static final double WHEEL_BASE = 28 * 0.0254; //distance from center of front and back modules from inches to meters
    public static final double MAX_SPEED = 1.0; //meters per second
    public static final double MAX_ROTATION_SPEED = 0.1;
    public static final double WHEEL_DIAMETER_INCHES = 1 / 3;
    public static final double VORTEX_TICKS_PER_REV = 3;
    public static final double GEAR_RATIO = 1 / 5.08; //may be wrong, only used for drive encoders

    public static final int SPARKMAX_ID_TOPRIGHT_DRIVE = 1;
    public static final int SPARKMAX_ID_TOPLEFT_DRIVE = 2;
    public static final int SPARKMAX_ID_BOTTOMRIGHT_DRIVE = 3;
    public static final int SPARKMAX_ID_BOTTOMLEFT_DRIVE = 4;
    public static final int SPARKMAX_ID_TOPRIGHT_TURN = 5;
    public static final int SPARKMAX_ID_TOPLEFT_TURN = 6;
    public static final int SPARKMAX_ID_BOTTOMRIGHT_TURN = 7;
    public static final int SPARKMAX_ID_BOTTOMLEFT_TURN = 8;
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
  }
  public static class climber {
    public static final double CLIMBER_SPEED = 0.75;
  }
}
