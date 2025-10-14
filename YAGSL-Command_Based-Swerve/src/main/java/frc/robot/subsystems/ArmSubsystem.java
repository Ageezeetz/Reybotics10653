/*
 * arm subsystem
 */
/*
 package frc.robot.subsystems;

 import com.revrobotics.spark.SparkMax;
 import com.revrobotics.spark.config.SparkMaxConfig;
 import com.revrobotics.spark.SparkLowLevel.MotorType;
 import com.revrobotics.spark.SparkBase.PersistMode;
 import com.revrobotics.spark.SparkBase.ResetMode;
 
 import edu.wpi.first.wpilibj2.command.SubsystemBase;

 import frc.robot.Constants.arm;
 
 
 public class ArmSubsystem extends SubsystemBase {
     private final SparkMax armMotor = new SparkMax(11, MotorType.kBrushless);
 
     public ArmSubsystem() {
         SparkMaxConfig armConfig = new SparkMaxConfig();
         armConfig.smartCurrentLimit(40);
         armConfig.voltageCompensation(10);
 
         armMotor.configure(armConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
     }
 
     public void lowerArm() {
         armMotor.set(arm.ARM_STRENGTH); //arm should go out of the robot
     }
 
     public void raiseArm() {
         armMotor.set(-arm.ARM_STRENGTH); //arm should go into the robot
     }
 
     public void stop() {
         armMotor.set(arm.STOP_MOTOR);
     }
 }
     */