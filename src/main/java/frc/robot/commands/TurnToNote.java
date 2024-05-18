// RobotBuilder Version: 6.1
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.

// ROBOTBUILDER TYPE: Command.

package frc.robot.commands;

import java.util.function.BooleanSupplier;
import java.util.function.DoubleSupplier;

import edu.wpi.first.math.MathUtil;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.LimelightHelpers;
// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS
// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS
import frc.robot.subsystems.Swerve;
import frc.robot.subsystems.Vision;

/**
 *
 */
public class TurnToNote extends Command {
    private DoubleSupplier translationSuppier;
    private DoubleSupplier strafeSupplier;
    private DoubleSupplier rotationSupplier;
    private BooleanSupplier robotCentricSupplier;
    private String _limelightName = "limelight-front";
    private double turnPower = 0.0;

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS
    private final Swerve m_swerve;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_DECLARATIONS

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS

    public TurnToNote(Swerve subsystem,
            DoubleSupplier translationsupplier,
            DoubleSupplier strafeSupplier,
            DoubleSupplier rotationSupplier,
            BooleanSupplier robotCentricSupplier) {

        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING

        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=VARIABLE_SETTING
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES

        m_swerve = subsystem;
        addRequirements(m_swerve);

        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=REQUIRES
        this.translationSuppier = translationsupplier;
        this.strafeSupplier = strafeSupplier;
        this.rotationSupplier = rotationSupplier;
        this.robotCentricSupplier = robotCentricSupplier;
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
    }

    private boolean targetAcquired() {
        return LimelightHelpers.getTV(_limelightName);
    }

    private double getTargetDegrees() {
        return LimelightHelpers.getTX(_limelightName);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        // // Set values as if we are driving. Once set, we will check to see if we hae a
        // // target before taking over steering
        // /* get values, deadband */
        double translationValue = MathUtil.applyDeadband(translationSuppier.getAsDouble(), Constants.stickDeadband);
        double strafeValue = MathUtil.applyDeadband(strafeSupplier.getAsDouble(), Constants.stickDeadband);
        double rotationValue = MathUtil.applyDeadband(rotationSupplier.getAsDouble(), Constants.stickDeadband);

        // double joystickAngle = (strafeValue != 0) && (translationValue != 0)
        //         ? Math.toDegrees(Math.atan2(strafeValue, translationValue))
        //         : 0;
        double magnitude = Math.sqrt(Math.pow(translationValue, 2) +
                Math.pow(strafeValue, 2));

        double xTargetVector;
        double yTargetVector;
        double targetAngle;
        double targetRadians;

        

        // targetAngle = targetAcquired() ? getTargetDegrees() -
        //         m_swerve.getHeading().getDegrees() : 0;
        targetAngle = targetAcquired() ? getTargetDegrees() : 0;
        targetRadians = Math.toRadians(targetAngle);
        xTargetVector = targetAcquired() ? magnitude * Math.cos(targetRadians) : 0;
        yTargetVector = targetAcquired() ? magnitude * Math.sin(targetRadians) : 0;

        // /* Drive */
        SmartDashboard.putNumber("degeees", getTargetDegrees());
        SmartDashboard.putBoolean("target", targetAcquired());
        SmartDashboard.putNumber("degrees", targetAngle);
        SmartDashboard.putNumber("radians", targetRadians);

        // // See if we have a target
        if (!targetAcquired()) {
            m_swerve.drive(
                    new Translation2d(translationValue, strafeValue).times(Constants.Swerve.maxSpeed),
                    rotationValue * Constants.Swerve.maxAngularVelocity,
                    !robotCentricSupplier.getAsBoolean(),
                    true);
        } else {
            SmartDashboard.putNumber("rotatepower", Vision.getInstance().GetTargetRotPower());
            m_swerve.drive(
                    new Translation2d(-xTargetVector, yTargetVector).times(Constants.Swerve.maxSpeed),
                    -targetAngle * Constants.Swerve.maxAngularVelocity,
                    !robotCentricSupplier.getAsBoolean(),
                    true);
        }

    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public boolean runsWhenDisabled() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DISABLED
        return false;

        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DISABLED
    }
}