// RobotBuilder Version: 6.1
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.

// ROBOTBUILDER TYPE: Subsystem.

package frc.robot.subsystems;

import com.revrobotics.CANSparkBase.IdleMode;
import com.revrobotics.CANSparkLowLevel.MotorType;
import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkFlex;
// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.RobotContainer;

/**
 *
 */
public class Spamp extends SubsystemBase {
    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    private CANSparkMax bottomLeft;
    private PWMSparkFlex topRight;
    private CANSparkMax midLeft;
    private PWMSparkFlex topLeft;
    private DigitalInput noteDetectorSpampBottom;
    private DoubleSolenoid shootSolenoid;
    private DigitalInput noteDetectorSpampTop;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS

    // sets default rpm for motors
    public double kP, kI, kD, kIz, kFF, kMaxOutput, kMinOutput, maxRPM;

    // this should get the speed the shaft spins at full power
    private double midLeftGearRatio = Constants.MaxRPMConstants.maxRPMNeo / 3;

    public double bottomRightSpeed;
    public double bottomLeftSpeed;

    public boolean transferring;

    /**
    *
    */
    public Spamp() {
        // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        bottomLeft = new CANSparkMax(25, MotorType.kBrushless);

        bottomLeft.setInverted(true);
        bottomLeft.setIdleMode(IdleMode.kBrake);
        bottomLeft.burnFlash();

        topRight = new PWMSparkFlex(2);

        topRight.setInverted(false);

        midLeft = new CANSparkMax(24, MotorType.kBrushless);

        midLeft.setInverted(false);
        midLeft.setIdleMode(IdleMode.kBrake);
        midLeft.burnFlash();

        topLeft = new PWMSparkFlex(3);

        topLeft.setInverted(false);

        noteDetectorSpampBottom = new DigitalInput(2);
        addChild("noteDetectorSpampBottom", noteDetectorSpampBottom);
        
         shootSolenoid = new DoubleSolenoid(20, PneumaticsModuleType.REVPH, 15, 14);
        addChild("shootSolenoid", shootSolenoid);

        noteDetectorSpampTop = new DigitalInput(3);
        addChild("noteDetectorSpampTop", noteDetectorSpampTop);

        // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTRUCTORS
        bottomLeft.setSmartCurrentLimit(40);
        midLeft.setSmartCurrentLimit(120);

        shootSolenoid.set(Value.kReverse);

    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
        SmartDashboard.putBoolean("Note in Spamp", isNoteInSpamp());
        SmartDashboard.putBoolean("Note Leaving Spamp", isNoteLeavingSpamp());

        if (RobotContainer.getInstance().m_acquisition.readyToTransfer()) {
            transferring = true;

        }
        if (transferring) {
            runBottom();
            if (!RobotContainer.getInstance().m_acquisition.isNoteInAcquisition() &&
                    isNoteInSpamp()) {
                stopall();
                RobotContainer.getInstance().m_acquisition.stopBoth();
                transferring = false;
            }
        }
    }

    @Override
    public void simulationPeriodic() {
        // This method will be called once per scheduler run when in simulation

    }

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

    public void stopall() {
        bottomLeft.set(0);
        midLeft.set(0);
        topLeft.set(0);
        topRight.set(0);
        RobotContainer.getInstance().m_acquisition.stopBoth();
    }

    public boolean isNoteInSpamp() {
        return noteDetectorSpampBottom.get();
    }

    public boolean isNoteLeavingSpamp() {
        return noteDetectorSpampTop.get();
    }

    public boolean isDoneShooting() {
        return !noteDetectorSpampBottom.get() && !noteDetectorSpampTop.get();
    }

    public void runBottom() {

        bottomLeft.set(.2);
        midLeft.set(.2);

    }

    public void intakeFromSpamp() {
        if (!isNoteInSpamp()) {
            midLeft.set(-Constants.SpampConstants.spampIntakeRPM / midLeftGearRatio);
            topLeft.set(-Constants.SpampConstants.spampIntakeRPM / Constants.MaxRPMConstants.maxRPMVortex);
            topRight.set(Constants.SpampConstants.spampIntakeRPM / Constants.MaxRPMConstants.maxRPMVortex);
        } else {
            stopall();
        }
    }


    public void deployShooter() {
        shootSolenoid.set(Value.kForward);
    }

    public void retractShooter() {
        shootSolenoid.set(Value.kReverse);
    }

    public void runTopAmp() {
        topRight.set(Constants.SpampConstants.topRightAmpRPM / Constants.MaxRPMConstants.maxRPMVortex); // .25
        midLeft.set(Constants.SpampConstants.midLeftAmpRPM / Constants.MaxRPMConstants.maxRPMNeo); // .17
        bottomLeft.set(.2);        
        RobotContainer.getInstance().m_acquisition.runBoth();
    }

    public void shootAmp() {
        bottomLeft.set(1);
        midLeft.set(1);
        topRight.set(.25);

    }

    public void runTopShooter() {
        topLeft.set(Constants.SpampConstants.speakerRPM / Constants.MaxRPMConstants.maxRPMVortex);
        topRight.set(-Constants.SpampConstants.speakerRPM / Constants.MaxRPMConstants.maxRPMVortex);
    }

    public void shootSpeaker() {
        bottomLeft.set(1);
        midLeft.set(1);
        topLeft.set(Constants.SpampConstants.speakerRPM / Constants.MaxRPMConstants.maxRPMVortex);
        topRight.set(-Constants.SpampConstants.speakerRPM / Constants.MaxRPMConstants.maxRPMVortex);
        RobotContainer.getInstance().m_acquisition.runBoth();
    }

    public void speakerAutonShoot() {
        if (!noteDetectorSpampTop.get()) {
            bottomLeft.set(.1);
            midLeft.set(.1);
            topLeft.set(Constants.SpampConstants.speakerRPM / Constants.MaxRPMConstants.maxRPMVortex);
            topRight.set(-Constants.SpampConstants.speakerRPM / Constants.MaxRPMConstants.maxRPMVortex);
            RobotContainer.getInstance().m_acquisition.runBoth();

        }
        else {
            shootSpeaker();
        }
    }

    public void ampAutonShoot() {
        topRight.set(.3); // .25
        midLeft.set(.23); // .17
        bottomLeft.set(.2);
    }

    public void resetState(){
        transferring = false;
        retractShooter();
        RobotContainer.getInstance().m_acquisition.retractIntake();
        stopall();
        RobotContainer.getInstance().m_acquisition.stopBoth();
    }
}
