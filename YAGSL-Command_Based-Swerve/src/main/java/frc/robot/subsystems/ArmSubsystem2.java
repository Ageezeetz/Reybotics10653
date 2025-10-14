package frc.robot.subsystems;

import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.SparkLowLevel.MotorType;
import com.revrobotics.spark.SparkBase.PersistMode;
import com.revrobotics.spark.SparkBase.ResetMode;
import com.revrobotics.spark.SparkBase.*;

public class ArmSubsystem2 {
    SparkMax armMotor = new SparkMax(11, MotorType.kBrushless);

    public void setSpeed(boolean up, boolean down) {
        if (up) {
            armMotor.set(-0.2);
        }
        else if (down) {
            armMotor.set(0.2);
        }
        else {
            armMotor.stopMotor();
        }
    }


}
