package org.usfirst.frc.team972.robot;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Trajectory {
	public double heading = 0;
	public double distance = 0;
	public long lastTime = 0;
	public static final double wheelWidth = 100; //aoe in mm change
	public static final long dTime = 2; //aoe check every 2s
	public static final double scale = 0.1; //aoe change
	public static final double accMax = 0.55; //aoe change
	public static final double dVel = 0.00005; //aoe change
	public static final double accMin = 0.45;
	public static final int stopDist = 500;
	public static final int goalAngle = 1;
	
	public void readSmartDashboard() {
		heading = SmartDashboard.getNumber("heading", 0);
		distance = SmartDashboard.getNumber("distance", 0);
		distance=distance*1000;
		lastTime = (long)SmartDashboard.getNumber("lastTime", 0);
	}
	
	public double getLength() {
		double length = 0;
		double alfa = Math.PI/180*heading/2; //in radians
		double sinAlfa = Math.sin(alfa);
		length = 2*wheelWidth*sinAlfa;
		System.out.println("Length: "+length+"; Alfa: "+alfa+"; Sinus: "+sinAlfa);
		return Math.abs(length);
	}
	
	public double getDistance() {
		return distance;
	}
		
	public double leftDriveInput() {
		double leftVel = 0;
		if(Math.abs(heading)>goalAngle && distance > 0.5) {
			if(heading > goalAngle) {
				leftVel = accMax;
			} else {
				leftVel = accMin;
			}
		} else if(distance > 0.5) {leftVel = 0.5;} else {leftVel = 0;}
		return leftVel;
	}
	
	public double rightDriveInput() {
		double rightVel = 0;
		if(Math.abs(heading)>goalAngle && distance > 0.5) {
			if(heading < goalAngle) {
				rightVel = accMax;
			} else {
				rightVel = accMin;
			}
		} else if(distance > 0.5) {rightVel = 0.5;} else {rightVel = 0;}
		return rightVel;
	}
	
//	public double leftDriveInput() {
//		double leftVel = 0;
//		double dDist = 0;
//		if(distance > stopDist) dDist = 0.1;
//		else dDist = distance*dVel;
//		if(heading > 0) leftVel = scale*getLength()/dTime + dDist;
//		else if(heading < 0 && distance >= stopDist) leftVel = accMin;
//		else if(heading < 0 && distance < stopDist) leftVel = dDist;
//		else if (heading == 0) leftVel = dDist;
//		
//		if(leftVel > accMax) leftVel = accMax;
//		return leftVel;
//	}
//	
//	public double rightDriveInput() {
//		double rightVel = 0;
//		double dDist = 0;
//		if(distance > stopDist) dDist = 0.1;
//		else dDist = distance*dVel;
//		if(heading < 0) rightVel = scale*getLength()/dTime + dDist;
//		else if(heading > 0 && distance >= stopDist) rightVel = accMin;
//		else if(heading > 0 && distance < stopDist) rightVel = dDist;
//		else if (heading == 0) rightVel = dDist;
//		
//		if(rightVel > accMax) rightVel = accMax;
//		return rightVel;
//	}
}
