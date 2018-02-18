/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com._2491nomythic.tempest;

import com._2491nomythic.tempest.commands.CommandBase;
import com._2491nomythic.tempest.commands.UpdateDriverstation;
import com._2491nomythic.tempest.commands.autonomous.CrossAutoLine;
import com._2491nomythic.tempest.commands.autonomous.DoNothing;
import com._2491nomythic.tempest.commands.autonomous.LeftPrioritizeScale;
import com._2491nomythic.tempest.commands.autonomous.LeftPrioritizeSwitch;
import com._2491nomythic.tempest.commands.autonomous.PlaceOnScaleLeft;
import com._2491nomythic.tempest.commands.autonomous.PlaceOnScaleRight;
import com._2491nomythic.tempest.commands.autonomous.PlaceOnSwitch;
import com._2491nomythic.tempest.commands.autonomous.PlaceOnSwitchLeft;
import com._2491nomythic.tempest.commands.autonomous.PlaceOnSwitchRight;
import com._2491nomythic.tempest.commands.autonomous.RightPrioritizeScale;
import com._2491nomythic.tempest.commands.autonomous.RightPrioritizeSwitch;
import com._2491nomythic.tempest.commands.cubestorage.TransportCubeTime;
import com._2491nomythic.tempest.commands.drivetrain.DriveStraightToPositionPID;
import com._2491nomythic.tempest.commands.drivetrain.RotateDrivetrainWithGyroPID;
import com._2491nomythic.tempest.commands.intake.RunIntakeTime;
import com._2491nomythic.tempest.commands.intake.ToggleIntakeDeployment;
import com._2491nomythic.tempest.commands.intake.ToggleIntakeOpening;
import com._2491nomythic.tempest.commands.shooter.MonitorRPM;
import com._2491nomythic.tempest.commands.shooter.RunShooterTime;
import com._2491nomythic.tempest.commands.shooter.ToggleShooterPosition;
import com._2491nomythic.tempest.settings.Variables;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in the
 * project.
 */
public class Robot extends TimedRobot {

	Command m_autonomousCommand;
	UpdateDriverstation updateDriverstation;
	MonitorRPM monitorRPM;
	SendableChooser<Command> m_chooser = new SendableChooser<>();
	
	public static boolean isTeleop;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() { 
		CommandBase.init();
		updateDriverstation = new UpdateDriverstation();
		monitorRPM = new MonitorRPM();
		updateDriverstation.start();
		monitorRPM.start();
		m_chooser.addObject("Cross Auto Line", new CrossAutoLine());
		m_chooser.addObject("PlaceOnSwitch", new PlaceOnSwitch());
		m_chooser.addObject("PlaceOnScaleLeft", new PlaceOnScaleLeft());
		m_chooser.addObject("PlaceOnScaleRight", new PlaceOnScaleRight());
		m_chooser.addObject("LeftPrioritizeScale", new LeftPrioritizeScale());
		m_chooser.addObject("RightPrioritizeScale", new RightPrioritizeScale());
		m_chooser.addObject("LeftPrioritizeSwitch", new LeftPrioritizeSwitch());
		m_chooser.addObject("RightPrioritizeSwitch", new RightPrioritizeSwitch());
		m_chooser.addObject("LeftSwitch", new PlaceOnSwitchLeft());
		m_chooser.addObject("RightSwitch", new PlaceOnSwitchRight());
		m_chooser.addDefault("Do Nothing", new DoNothing());
		SmartDashboard.putData("Auto mode", m_chooser);
		SmartDashboard.putData("DriveStraightToPositionPID", new DriveStraightToPositionPID(20));
		SmartDashboard.putData("RotateDrivetrainRelative90", new RotateDrivetrainWithGyroPID(90, false));
		SmartDashboard.putData("Run Intake", new RunIntakeTime(0.3, 10));
		SmartDashboard.putNumber("Proportional Rotate", Variables.proportional);
		SmartDashboard.putNumber("Derivative Rotate", Variables.derivative);
		SmartDashboard.putNumber("Proportional Forward", Variables.proportionalForward);
		SmartDashboard.putNumber("Derivative Forward", Variables.derivativeForward);
		SmartDashboard.putNumber("Drive Default Command", Variables.driveDefault);
		SmartDashboard.putNumber("AutoDelay", Variables.autoDelay);
		SmartDashboard.putData("RunShooterTime", new RunShooterTime(.8, 5));
		SmartDashboard.putData("RunCubeStorageTime", new TransportCubeTime(1, .8));
		SmartDashboard.putData("DeployIntake", new ToggleIntakeDeployment());
		SmartDashboard.putData("OpenIntake", new ToggleIntakeOpening());
		SmartDashboard.putData("ToggleShooter", new ToggleShooterPosition());
	}

	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	@Override
	public void disabledInit() {
		isTeleop = false;
	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString code to get the auto name from the text box below the Gyro
	 *
	 * <p>You can add additional auto modes by adding additional commands to the
	 * chooser code above (like the commented example) or additional comparisons
	 * to the switch structure below with additional strings & commands.
	 */
	@Override
	public void autonomousInit() {
		Variables.autoDelay = SmartDashboard.getNumber("AutoDelay", 0);
		
		m_autonomousCommand = m_chooser.getSelected();

		/*
		 * String autoSelected = SmartDashboard.getString("Auto Selector",
		 * "Default"); switch(autoSelected) { case "My Auto": autonomousCommand
		 * = new MyAutoCommand(); break; case "Default Auto": default:
		 * autonomousCommand = new ExampleCommand(); break; }
		 */

		// schedule the autonomous command (example)
		if (m_autonomousCommand != null) {
			m_autonomousCommand.start();
		}
		
		isTeleop = false;
	}

	/**
	 * This function is called periodically during autonomous.
	 */
	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {
		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.
		if (m_autonomousCommand != null) {
			m_autonomousCommand.cancel();
		}
		
		isTeleop = true;
	}

	/**
	 * This function is called periodically during operator control.
	 */
	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
	}

	/**
	 * This function is called periodically during test mode.
	 */
	@Override
	public void testPeriodic() {
	}
}