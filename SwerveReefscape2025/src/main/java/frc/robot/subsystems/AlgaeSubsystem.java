package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import static frc.robot.Constants.algae.ALGAE_SPEED;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class AlgaeSubsystem extends SubsystemBase {
    private final SparkMax algaeMotor;

    public AlgaeSubsystem() {
        algaeMotor = new SparkMax(10, MotorType.kBrushless);
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