// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.subsystems.Spamp;

public class autonSpampIntake extends Command {
  private final Spamp m_spamp;
  /** Creates a new autonSpampIntake. */
  public autonSpampIntake(Spamp subsystem) {
    // Use addRequirements() here to declare subsystem dependencies.
        m_spamp = subsystem;
        addRequirements(m_spamp);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    m_spamp.deployShooter();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    m_spamp.intakeFromSpamp();
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    m_spamp.retractShooter();
    m_spamp.stopall();
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return m_spamp.isNoteInSpamp();
  }
}
