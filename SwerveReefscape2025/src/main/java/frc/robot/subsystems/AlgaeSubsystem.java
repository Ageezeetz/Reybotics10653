package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import static frc.robot.Constants.algae.ALGAE_SPEED;
import static frc.robot.Constants.algae.ALGAE_WHEEL_SPEED;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class AlgaeSubsystem extends SubsystemBase {
    private final SparkMax algaeMotor;
    private final SparkMax algaeWheelMotor;

    public AlgaeSubsystem() {
        algaeMotor = new SparkMax(10, MotorType.kBrushless);
        algaeWheelMotor = new SparkMax(11, MotorType.kBrushless);
    }

    public void algaeDown() { //lowers algae superstructure
        algaeMotor.set(ALGAE_SPEED);
    }

    public void algaeUp() { //rises algae superstructure
        algaeMotor.set(-ALGAE_SPEED);
    }

    public void algaeOut() { //rolls algae out
        algaeWheelMotor.set(ALGAE_WHEEL_SPEED);
    }

    public void algaeIn() { //rolls algae in
        algaeWheelMotor.set(-ALGAE_WHEEL_SPEED);
    }
}