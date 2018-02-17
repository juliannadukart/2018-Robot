package com._2491nomythic.robot.commands.drivetrain;

import com._2491nomythic.robot.commands.CommandBase;
import com._2491nomythic.robot.settings.ControllerMap;

import edu.wpi.first.wpilibj.Timer;

/**
 * Controls the robot with quadratic acceleration as according to driver control input
 */
public class QuadraticDrive extends CommandBase {
	private double turnSpeed, leftSpeed, rightSpeed, rawLeftSpeed, rawRightSpeed, accelerationInterval, time, timeAddition, accelerationIncrease, quadraticCoefficient;
	int state, necessaryIterations;
	private Timer timer;

	/**
	 * Controls the robot with quadratic acceleration as according to driver control input
	 */
	public QuadraticDrive() {
		// Use requires() here to declare subsystem dependencies
		// eg. requires(chassis);
		requires(drivetrain);
		timer = new Timer();
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		state = 1;
		timer.start();
		timer.reset();
		accelerationInterval = .2;
		accelerationIncrease = .08;
			//make sure that accelerationInterval / accelerationIncrease = 2.5
		quadraticCoefficient = 1.5;
		necessaryIterations = (int) (.8 / accelerationIncrease);
			//.8 because .8, within a quadratic system of y = 1.5x^2, is the x input that creates a y of .96, the closest we need to get to 1 before then going to 1.
		
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		
		turnSpeed =  0.5 * oi.getAxisDeadzonedSquared(ControllerMap.driveController, ControllerMap.driveTurnAxis, 0.1);
		rawLeftSpeed = -oi.getAxisDeadzonedSquared(ControllerMap.driveController, ControllerMap.driveMainAxis, .05);
		rawRightSpeed = -oi.getAxisDeadzonedSquared(ControllerMap.driveController, ControllerMap.driveMainAxis, .05);
		
		if (Math.abs(oi.getAxisDeadzonedSquared(ControllerMap.driveController, ControllerMap.driveMainAxis, .1)) <= .1) {
			timer.reset();
		}
		
		time = timer.get() + timeAddition;
		timeAddition = 0;
		leftSpeed = quadraticCoefficient * Math.pow(state * accelerationIncrease, 2) * rawLeftSpeed;
		rightSpeed = quadraticCoefficient * Math.pow(state * accelerationIncrease, 2) * rawRightSpeed;
		if (state == 1) {
			if (time < accelerationInterval) {
				state++;
			}
		}
		else if (time > ( state * accelerationInterval) && timer.get() < ((state + 1) * accelerationInterval)) {
	   		state++;
	   	}
		if (Math.abs(rawLeftSpeed) < state * quadraticCoefficient * Math.pow(state * accelerationIncrease, 2) * rawLeftSpeed && Math.abs(rawRightSpeed) < state * quadraticCoefficient * Math.pow(state * accelerationIncrease, 2) * rawRightSpeed) {
			timer.reset();
			state--;
			timeAddition = (state - 1) * accelerationInterval;
		}
		if (state == necessaryIterations) {
			leftSpeed = rawLeftSpeed;
			rightSpeed = rawRightSpeed;
			if (Math.abs(rawLeftSpeed) < rawLeftSpeed && Math.abs(rawRightSpeed) < rawRightSpeed) {
				timer.reset();
				state = 3;
				timeAddition = (state - 1) * accelerationInterval;
			}
		}
		
		
		drivetrain.drivePercentOutput(leftSpeed + turnSpeed, rightSpeed - turnSpeed);
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return false;
	}

	// Called once after isFinished returns true
	protected void end() {
		drivetrain.stop();
		timer.stop();
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		end();
	}
}
