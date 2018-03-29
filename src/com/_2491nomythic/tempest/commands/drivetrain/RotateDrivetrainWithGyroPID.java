package com._2491nomythic.tempest.commands.drivetrain;

import com._2491nomythic.tempest.commands.CommandBase;
import com._2491nomythic.tempest.settings.Variables;

import edu.wpi.first.wpilibj.Timer;

/**
 * Rotates the robot to a specified angle using a PIDController generated by the drivetrain subsystem
 */
public class RotateDrivetrainWithGyroPID extends CommandBase {
	private double target, initialAngle;
	private Timer timer;
	private boolean type;
	private double relative;
	

	/**
	 * Rotates the robot to a specified angle using a PIDController generated by the drivetrain subsystem
	 * @param angle The angle that the robot rotates to, where negative values rotate the robot counterclockwise
	 * @param absolute Set true for an absolute turn, or, false for a relative turn
	 */
	public RotateDrivetrainWithGyroPID(double angle, boolean absolute) {
		type = absolute;
		target = angle;
		timer = new Timer();
		// Use requires() here to declare subsystem dependencies
	  	requires(drivetrain);
	  	
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		initialAngle = drivetrain.getRawGyroAngle();
		Variables.useGyroPID = true;
		drivetrain.setInputRange(0, 360);
		drivetrain.getPIDController().setContinuous(true);
		drivetrain.setAbsoluteTolerance(2);
		drivetrain.getPIDController().setPID(Variables.proportionalRotate, Variables.integralRotate, Variables.derivativeRotate);
		
		relative = ((drivetrain.getGyroAngle() + target) % 360 + 360) % 360;
		if(type) {
			drivetrain.setSetpoint(target);
		}
		else {
			drivetrain.setSetpoint(relative);
		}
		drivetrain.enable();
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {

	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return drivetrain.onTarget();
	}

	// Called once after isFinished returns true
	protected void end() {	
		drivetrain.stop();
		drivetrain.disable();
		Variables.letsGo = true;
		
		System.out.println("Change in Angle: " + (drivetrain.getRawGyroAngle() - initialAngle));
		System.out.println("Time taken: " + timer.get());
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		end();
	}
}
