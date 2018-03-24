/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package com._2491nomythic.tempest;

import com._2491nomythic.tempest.commands.CommandBase;
import com._2491nomythic.tempest.commands.ResetSolenoids;
import com._2491nomythic.tempest.commands.UpdateDriverstation;
import com._2491nomythic.tempest.commands.autonomous.CrossAutoLine;
import com._2491nomythic.tempest.commands.autonomous.DoNothing;
import com._2491nomythic.tempest.commands.lights.SendAllianceColor;
import com._2491nomythic.tempest.commands.autonomous.PathAutoScale;
import com._2491nomythic.tempest.commands.autonomous.PathAutoSwitch;
import com._2491nomythic.tempest.commands.autonomous.VelocityTestAuto;
/*import com._2491nomythic.tempest.commands.autonomous.PlaceOnSwitchLeft;
import com._2491nomythic.tempest.commands.autonomous.PlaceOnSwitchRight;
import com._2491nomythic.tempest.commands.autonomous.RightPrioritizeScale;
import com._2491nomythic.tempest.commands.autonomous.RightPrioritizeSwitch;*/
import com._2491nomythic.tempest.commands.lights.SerialConnectivityTest;
import com._2491nomythic.tempest.commands.lights.UpdateLightPattern;
import com._2491nomythic.tempest.commands.shooter.MonitorRPS;
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
	ResetSolenoids resetSolenoids;
	UpdateDriverstation updateDriverstation;
	UpdateLightPattern updateLights;
	SendAllianceColor sendColor;
	MonitorRPS monitorRPS;
	
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
		resetSolenoids = new ResetSolenoids();
		monitorRPS = new MonitorRPS();
		sendColor = new SendAllianceColor();
		updateLights = new UpdateLightPattern();
		
		updateDriverstation.start();
		updateLights.start();
		monitorRPS.start();
		
		m_chooser.addObject("DoNothing", new DoNothing());
		m_chooser.addObject("CrossLine", new CrossAutoLine());
		m_chooser.addObject("Pathing/SwitchCenter", new PathAutoSwitch());
		m_chooser.addObject("Pathing/Scale", new PathAutoScale());
		
		/*
		m_chooser.addObject("SwitchLeft", new DriveForwardSwitch(true));
		m_chooser.addObject("SwitchRight", new DriveForwardSwitch(false));
		m_chooser.addObject("PlaceOnScaleLeft", new PlaceOnScaleLeft());
		m_chooser.addObject("PlaceOnScaleRight", new PlaceOnScaleRight());
		m_chooser.addObject("LeftPrioritizeScale", new LeftPrioritizeScale());
		m_chooser.addObject("RightPrioritizeScale", new RightPrioritizeScale());
		m_chooser.addObject("LeftPrioritizeSwitch", new LeftPrioritizeSwitch());
		m_chooser.addObject("RightPrioritizeSwitch", new RightPrioritizeSwitch());
		*/
		m_chooser.addObject("VelocityTest", new VelocityTestAuto());		
		
		SmartDashboard.putData("Auto mode", m_chooser);

		SmartDashboard.putData("send 1", new SerialConnectivityTest(1));
		SmartDashboard.putData("send 2", new SerialConnectivityTest(2));
		SmartDashboard.putData("send 3", new SerialConnectivityTest(3));
		SmartDashboard.putData("DriveStraightToPositionPID", new DriveStraightToPositionPID(-20));
		SmartDashboard.putData("RotateDrivetrainRelative90", new RotateDrivetrainWithGyroPID(90, false));
		SmartDashboard.putData("RotateDrivetrainRelative-90", new RotateDrivetrainWithGyroPID(-90, false));
		SmartDashboard.putNumber("ProportionalRotate", Variables.proportionalRotate);
		SmartDashboard.putNumber("DerivativeRotate", Variables.derivativeRotate);
		SmartDashboard.putNumber("ProportionalForward", Variables.proportionalForward);
		SmartDashboard.putNumber("DerivativeForward", Variables.derivativeForward);
		SmartDashboard.putNumber("DriveDefault", Variables.driveDefault);
		SmartDashboard.putNumber("AutoDelay", Variables.autoDelay);
		SmartDashboard.putNumber("SwitchRPS", Constants.shooterSwitchRPS);
		SmartDashboard.putNumber("LowScaleRPS", Constants.shooterLowScaleRPS);
		SmartDashboard.putNumber("MedScaleRPS", Constants.shooterMediumScaleRPS);
		SmartDashboard.putNumber("HighScaleRPS", Constants.shooterHighScaleRPS);
		SmartDashboard.putBoolean("UseMonitorRPS", Variables.useMonitorRPS);
		SmartDashboard.putNumber("LeftShootPower", Variables.leftShootSpeed);
		SmartDashboard.putNumber("RightShootPower", Variables.rightShootSpeed);
		SmartDashboard.putData("RunShooter", new RunShooterManual());
        
		System.out.println("Boot Successful");
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
		sendColor.start();
		
		m_autonomousCommand = m_chooser.getSelected();

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
