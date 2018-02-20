package org.usfirst.frc.team972.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Time {
	private static long msInit = 0;
	
	public static void init() {
		msInit = System.currentTimeMillis();
	}
	
	public static double get() {
		return ((double)(System.currentTimeMillis() - msInit));
	}
	
	public static void updateSmartDashboard() {
		SmartDashboard.putNumber("Init Time", msInit);
		SmartDashboard.putNumber("Current Time", get());
	}
}
