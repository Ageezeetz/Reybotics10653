package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.config.SparkMaxConfig;

import static frc.robot.Constants.algae.ALGAE_SPEED;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class AlgaeSubsystem extends SubsystemBase {
    private final SparkMax algaeMotor;
    private final SparkMaxConfig algaeConfig = new SparkMaxConfig();

    public AlgaeSubsystem() {
        algaeMotor = new SparkMax(10, MotorType.kBrushless);
        algaeConfig.smartCurrentLimit(80);
        algaeConfig.secondaryCurrentLimit(100);
        algaeConfig.voltageCompensation(12);
        // algaeConfig.inverted(true);

        algaeMotor.configure(algaeConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
    }

    public void algaeDown() { //lowers algae superstructure
        algaeMotor.set(ALGAE_SPEED);
    }

    public void algaeUp() { //rises algae superstructure
        algaeMotor.set(-ALGAE_SPEED);
    }

    public void stop() {
        algaeMotor.set(0);
    }
}