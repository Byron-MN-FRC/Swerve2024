// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Acquisition;

public class autonAcquire extends Command {
  private final Acquisition m_acquisition;
  private final Timer m_time = new Timer();
  /** Creates a new autonAcquire. */
  public autonAcquire(Acquisition subsystem) {
    // Use addRequirements() here to declare subsystem dependencies.
        m_acquisition = subsystem;
        addRequirements(m_acquisition);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
      // Added after 2nd match
      // VVVVVVVVVVVVVVVVVVVVV
      m_time.reset();
      m_time.start();
      //^^^^^^^^^^^^^^^^^^^^^^
      m_acquisition.deployIntake();
      m_acquisition.runIntakeIn();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    // Added after 2nd match
    // VVVVVVVVVVVVVVVVVVVVV
     SmartDashboard.putNumber("shooter",m_time.get());
  
     if (m_time.get() > 1.3 && m_time.get() < 1.7){
       m_acquisition.retractIntake();

     } else if (m_time.get() >= 1.7) { 
       m_acquisition.deployIntake();
     }
      SmartDashboard.putNumber("shooter",m_time.get());
  
  
    // if (m_time.hasElapsed(1.3) && !m_time.hasElapsed(1.7)){
    //   m_acquisition.retractIntake();

    // } else if (m_time.hasElapsed(1.7)) { 
    //   m_acquisition.deployIntake();
    // }
    //^^^^^^^^^^^^^^^^^^^^^^
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_acquisition.retractIntake();
    m_acquisition.stopBoth();
    m_time.stop();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return
    m_acquisition.isNoteInAcquisition();
  }
}
