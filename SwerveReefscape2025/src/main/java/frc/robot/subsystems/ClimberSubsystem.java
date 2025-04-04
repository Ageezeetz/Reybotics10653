package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;

import static frc.robot.Constants.climber.CLIMBER_SPEED;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class ClimberSubsystem extends SubsystemBase {
    private final SparkMax climberMotor;
    private final SparkMaxConfig climberConfig = new SparkMaxConfig();


    public ClimberSubsystem() {
        climberMotor = new SparkMax(11, MotorType.kBrushless);
        climberConfig.smartCurrentLimit(80);
        climberConfig.secondaryCurrentLimit(100);
        climberConfig.voltageCompensation(12);
        // climberConfig.inverted(true);

        climberMotor.configure(climberConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }

    public void raiseClimber() {
        climberMotor.set(CLIMBER_SPEED);
    }

    public void lowerClimber() {
        climberMotor.set(-CLIMBER_SPEED);
    }

    public void stop() {
        climberMotor.set(0);
    }
}
