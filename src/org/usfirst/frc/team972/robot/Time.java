package org.usfirst.frc.team972.robot;

public class Time {
	/**Variable Declarations*/
	//Start time, captured in_init()
	private static long msInit;
	//Current lap time
	private static long msLap;
	//Past lap time
	private static long msPastLap;
	
	/**Methods*/
	//Captures variables marking start time
	public void init() {
		msInit = System.currentTimeMillis();
		msPastLap = System.currentTimeMillis();
	}
	
	//Goes through one lap resetting variables for next lap and returns the time since last lap
	public double lap() {
		msLap = System.currentTimeMillis();
		long msTemp = msPastLap;
		msPastLap = msLap;
		return (msLap-msTemp)/1000;
	}
	
	//Reads the time since the previous lap without starting a new lap
	public long readOnlyLapTime() {
		return (msLap-msPastLap)/1000;
	}
	
	//Returns the time since initiation
	public double get() {
		return ((double)(System.currentTimeMillis() - msInit));
	}
}
