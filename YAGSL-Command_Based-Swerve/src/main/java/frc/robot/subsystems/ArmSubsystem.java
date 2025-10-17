package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.RelativeEncoder;

import frc.robot.Constants.arm;

public class ArmSubsystem {
    public final SparkMax armMotor = new SparkMax(11, MotorType.kBrushless);
    private final RelativeEncoder armEncoder;

    private final double armUpperLimit = 3.0; //change for tomorrow
    private final double armLowerLimit = 11.5; //change for tomorrow

    public ArmSubsystem() {
        SparkMaxConfig armConfig = new SparkMaxConfig();
        armConfig.smartCurrentLimit(40);
        armConfig.voltageCompensation(10);

        armMotor.configure(armConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);

        armEncoder = armMotor.getEncoder();

    }

    public void setSpeed(boolean up, boolean down) {
        double currentPosition = armEncoder.getPosition();

        if (up && (currentPosition >= armUpperLimit)) { //if operator A is pressed and arm has not gone above limit
            armMotor.set(-arm.ARM_STRENGTH - 0.1); //pull in climber
        }
        else if (down && (currentPosition <= armLowerLimit)) { //if operator Y is pressed and arm has not gone below limit
            armMotor.set(arm.ARM_STRENGTH); //lower climber
        }
        else {
            armMotor.stopMotor();
        }
    }


}
