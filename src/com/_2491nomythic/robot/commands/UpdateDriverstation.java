package com._2491nomythic.robot.commands;


import com._2491nomythic.robot.settings.Variables;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Updates values in the code from editable values on the SmartDashboard
 */
public class UpdateDriverstation extends CommandBase {
	private Timer timer;
	private double nextRun;

	/**
	 * Updates values in the code from editable values on the SmartDashboard
	 */
    public UpdateDriverstation() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
		setRunWhenDisabled(true);
		setInterruptible(false);
		timer = new Timer();
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	timer.start();
		timer.reset();
		nextRun = timer.get();
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	if (timer.get() > nextRun) {
			nextRun = nextRun + 0.1;
		}
			
    	
			
			
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	timer.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
