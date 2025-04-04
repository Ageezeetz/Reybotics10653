package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;

import static frc.robot.Constants.coral.ROLLER_SPEED;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class CoralSubsystem extends SubsystemBase {
    private final SparkMax rollerMotor;
    private final SparkMaxConfig rollerConfig = new SparkMaxConfig();


    public CoralSubsystem() {
        rollerMotor = new SparkMax(9, MotorType.kBrushless);
        rollerConfig.smartCurrentLimit(80);
        rollerConfig.secondaryCurrentLimit(100);
        rollerConfig.voltageCompensation(12);
        // rollerConfig.inverted(true);

        rollerMotor.configure(rollerConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }

    public void rollOut() {
        rollerMotor.set(ROLLER_SPEED);
    }

    public void rollIn() {
        rollerMotor.set(-ROLLER_SPEED);
    }

    public void stop() {
        rollerMotor.set(0);
    }
}
