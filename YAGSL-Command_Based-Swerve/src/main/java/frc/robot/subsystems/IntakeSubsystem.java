package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.intake;


public class IntakeSubsystem extends SubsystemBase {
    private final SparkMax rollerMotor = new SparkMax(10, MotorType.kBrushless);

    public IntakeSubsystem() {
        SparkMaxConfig rollerConfig = new SparkMaxConfig();
        rollerConfig.smartCurrentLimit(40);
        rollerConfig.voltageCompensation(10);
        rollerConfig.inverted(true);

        rollerMotor.configure(rollerConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
        
    }

    public void rollOut() {
        rollerMotor.set(intake.ROLLER_SPEED); //intake wheels roll outward
    }

    public void rollIn() {
        rollerMotor.set(-intake.ROLLER_SPEED); //intake wheels roll inward
    }

    public void stop() {
        rollerMotor.set(intake.STOP_MOTOR);
    }
}