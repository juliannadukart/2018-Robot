package com._2491nomythic.robot.settings;

/**
 * Various information needed for robot functionality that can be modified by the code itself
 */
public class Variables {
	
	//Drive
	public static boolean useLinearAcceleration = true;
	public static double accelerationSpeed = 0.05;
	
	//PID Constants from Watt
	public static double proportional = 0.016;
	public static double integral = 0.0;
	public static double derivative = 0.007;
	public static double proportionalForward = .5;
	public static double integralForward = 0;
	public static double derivativeForward = .05;
	
	
	public static boolean useGyroPID;

}
