/*
 * Coral intake subsytem
 */

package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;


public class CoralSubsystem extends SubsystemBase {
    private final SparkMax rollerMotor = new SparkMax(5, MotorType.kBrushless);

    public CoralSubsystem() {
        SparkMaxConfig rollerConfig = new SparkMaxConfig();
        rollerConfig.smartCurrentLimit(60);
        rollerConfig.voltageCompensation(10);
        rollerConfig.inverted(true);

        rollerMotor.configure(rollerConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }

    public void dropCoral() {
        rollerMotor.set(Constants.coral.ROLLER_SPEED);
    }

    public void rollInCoral() {
        rollerMotor.set(-Constants.coral.ROLLER_SPEED);
    }

    public void stopMotor() {
        rollerMotor.set(Constants.drive.STOP_MOTOR);
    }
}