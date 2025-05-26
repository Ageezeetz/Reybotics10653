/*
 * Climber subsystem
 */

 package frc.robot.subsystems;

 import com.revrobotics.spark.SparkMax;
 import com.revrobotics.spark.config.SparkMaxConfig;
 import com.revrobotics.spark.SparkLowLevel.MotorType;
 import com.revrobotics.spark.SparkBase.PersistMode;
 import com.revrobotics.spark.SparkBase.ResetMode;
 
 import edu.wpi.first.wpilibj2.command.SubsystemBase;
 import frc.robot.Constants.climber;
 
 
 public class ClimberSubsystem extends SubsystemBase {
     private final SparkMax climberMotor = new SparkMax(9, MotorType.kBrushless);
 
     public ClimberSubsystem() {
         SparkMaxConfig climberConfig = new SparkMaxConfig();
         climberConfig.smartCurrentLimit(40);
         climberConfig.voltageCompensation(10);
 
         climberMotor.configure(climberConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
     }
 
     public void lowerClimber() {
         climberMotor.set(climber.CLIMBER_STRENGTH); //climber should go out of the robot
     }
 
     public void raiseClimber() {
         climberMotor.set(-climber.CLIMBER_STRENGTH); //climber should go into the robot
     }
 
     public void stop() {
         climberMotor.set(climber.STOP_MOTOR);
     }
 }