
package com._2491nomythic.tempest.commands.drivetrain;

import com._2491nomythic.tempest.commands.CommandBase;
import com._2491nomythic.tempest.settings.Variables;

import edu.wpi.first.wpilibj.Timer;

/**
 * Drive a given distance in a straight line using a PIDController generated by the drivetrain susbsystem.
 */
public class DriveStraightToPositionPID extends CommandBase {
	private double target, initialPosition;
	private Timer timer;

	/**
	 * Drive a given distance in a straight line using a PIDController generated by the drivetrain susbsystem.
	 * @param distance The relative distance for the robot to drive
	 */
	public DriveStraightToPositionPID(double distance) {
		target = distance;
		timer = new Timer();
		requires(drivetrain);
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		timer.start();
		drivetrain.resetEncoders();
		initialPosition = drivetrain.getLeftEncoderDistance();
		Variables.useGyroPID = false;
		drivetrain.setInputRange(-100000000, 100000000);
		drivetrain.getPIDController().setContinuous(false);
		drivetrain.setAbsoluteTolerance(1.5);
		drivetrain.getPIDController().setPID(Variables.proportionalForward, Variables.integralForward, Variables.derivativeForward);
		
		drivetrain.setSetpoint(drivetrain.getDistance() + target);
		drivetrain.enable();
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return drivetrain.onTarget();
	}

	// Called once after isFinished returns true
	protected void end() {
		drivetrain.disable();
		drivetrain.stop();
		
		System.out.println("Change in Distance: " + (drivetrain.getLeftEncoderDistance() - initialPosition));
		System.out.println("Time taken: " + timer.get());
	}
	
	protected void interrupted() {
		end();
	}
}