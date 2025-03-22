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
import frc.robot.Constants;


public class ClimberSubsystem extends SubsystemBase {
    private final SparkMax climberMotor = new SparkMax(6, MotorType.kBrushless);

    public ClimberSubsystem() {
        SparkMaxConfig climberConfig = new SparkMaxConfig();
        climberConfig.smartCurrentLimit(60);
        climberConfig.voltageCompensation(10);

        climberMotor.configure(climberConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }

    public void lowerClimber() {
        climberMotor.set(Constants.climber.CLIMBER_STRENGTH);
    }

    public void raiseClimber() {
        climberMotor.set(-Constants.climber.CLIMBER_STRENGTH);
    }

    public void stopMotor() {
        climberMotor.set(Constants.drive.STOP_MOTOR);
    }
}