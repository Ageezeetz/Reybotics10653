package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import static frc.robot.Constants.coral.ROLLER_SPEED;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class CoralSubsystem extends SubsystemBase {
    private final SparkMax rollerMotor;


    public CoralSubsystem() {
        rollerMotor = new SparkMax(9, MotorType.kBrushless);
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
