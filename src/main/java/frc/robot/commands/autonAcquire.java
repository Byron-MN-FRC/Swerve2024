// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj.Timer;
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
      m_time.start();
      m_acquisition.deployIntake();
      m_acquisition.runIntakeIn();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (m_time.get() > 1.3 && m_time.get() < 1.7){
      m_acquisition.retractIntake();

    } else if (m_time.get() >= 1.7) { 
      m_acquisition.deployIntake();
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_acquisition.retractIntake();
    m_acquisition.stopBoth();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return
    m_acquisition.isNoteInAcquisition();
  }
}
