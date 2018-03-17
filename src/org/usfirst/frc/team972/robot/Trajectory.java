package org.usfirst.frc.team972.robot;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import com.analog.adis16448.frc.ADIS16448_IMU;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Trajectory {
	/**Object Declarations*/
	//Inertial_Measurment_Unit: measures acceleration and angle
	public static final ADIS16448_IMU imu = new ADIS16448_IMU();
	//PID for trajectory calculations
	PID PID = new PID(0.05,0,0);
	//Array list for storing past values of IMU
	ArrayList<Double> imuStorage;
	//Time object for accurate time counting
	Time time = new Time();
	
	/**Variable Declarations*/
	//The time that the image that the heading was calculated from was taken
	long captureTimestamp = 0;
	//Heading and distance based on camera image processing 
	double heading = 0;
	double pastHeading = 0;
	double distance = 0;
	//Values for ultra_basic drive, including motor speeds and the goal range for the turning
	public static final double accMin = 0.4;
	public static final double accMax = 0.5;
	public static final int goalAngle = 1;
	//The heading based on IMU gyroscope
	double imuAngleZ;
	//The time data for accurately capturing IMU data points
	double timeNow;
	double timeGoal;
	//The amount of time passed since the last lap for PID drive
	double lapTimeStorage;
	
	/**Methods*/
	//Returns distance from raspberry pi
	public double getDistance() { 
		return distance;
	}
	
	//Returns camera calculated heading from raspberry pi
	public double getHeading() { 
		return heading;
	}
	
	//Limits the value inputed to between the max and min values
	//@param double input (input value), double max (top limit), double min (bottom limit)
	public static double limitAcc(double input, double max, double min) {
		if(input > max) {
			input = max;
		} else if(input < min) {
			input = min;
		}
		return input;
	}
	
	//Stores the IMU heading for IMU assisted course correction
	public void storeHeadingPoints() {
		imuAngleZ = imu.getAngleZ();
		if(imuStorage.size()>20) {
			imuStorage.remove(0);
		}
		imuStorage.add(imuAngleZ);
	}
	
	public void init() {
		//Thread initiated at start to have very accurate storage times
		Thread thread = new Thread(new Runnable() {
		    public void run() {
		    	//While true, always is running
		        while(true) {
		        	//Stores heading point
		        	storeHeadingPoints();
		        	//Then waits 100_ms, with error catching
		            try {
						TimeUnit.MILLISECONDS.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		        }   
		    }
		    
		});
		//Runs the thread
		thread.start();
	}
	
	//Adjusts the heading value by using IMU on robot
	public double adjustHeading() {
		double adjustedHeading = heading;
		int timeLoopCount = Math.round((captureTimestamp-System.currentTimeMillis())/100);
		if(heading != pastHeading) {
			adjustedHeading += imuStorage.get(timeLoopCount)-imuAngleZ;
		} else {
			adjustedHeading = heading + imuAngleZ;
			
		}
		return adjustedHeading;
	}
	
	//Reads the raspberry pi for camera heading, distance and the time-stamp of the image capture
	public void readSmartDashboard() {
		if(!(SmartDashboard.getNumber("heading", 0) == pastHeading)) {
			heading = SmartDashboard.getNumber("heading", 0);
		}
		heading = adjustHeading();
		distance = SmartDashboard.getNumber("distance", 0);
		distance=distance*1000;
		captureTimestamp = (long)SmartDashboard.getNumber("captureTimestamp", 0);
		pastHeading = heading;
	}
	
	//Simple auto tracking drive code using max and min drive values
	//@param: boolean sideLeft-is the motor on the left side (true) or right (false)
	public double ultraBasicDrive(boolean sideLeft) {
		double driveVel = 0;
		if(heading > goalAngle) {
			if(sideLeft) {
				driveVel = accMax;
			} else {
				driveVel = accMin;
			}
		} else if(heading < -goalAngle) {
			if(sideLeft) {
				driveVel = accMin;
			} else {
				driveVel = accMax;
			}
		}
		return driveVel;
	}
	
	//Very simple PID based drive calculations
	public double basicPIDDrive() {
		//Make sure that the time scale is not too long, so as to not have a very large integral
		if(time.readOnlyLapTime()>0.3) {
			lapTimeStorage = 0.3;
		} else {
			lapTimeStorage = time.lap();
		}
		return(PID.Update(0, heading, lapTimeStorage));
	}
}
