package org.usfirst.frc.team972.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends IterativeRobot {
	private DifferentialDrive m_robotDrive;
	double slider;
	private Joystick m_stick;
	private Timer m_timer;
	Trajectory traj = new Trajectory();
	public static final double stopDistance = 500;
	boolean currentButton;
	boolean pastButton;
	
	public void robotInit() {
		m_stick = new Joystick(0);
		m_robotDrive = new DifferentialDrive(new Spark(0), new Spark(1));
        m_timer = new Timer();
        
	}

	public void autonomousInit() {
		m_timer.reset();
		m_timer.start();
		
	}

	public void autonomousPeriodic() {		
		// Drive for 2 seconds
		if (m_timer.get() < 5.0) {
			m_robotDrive.arcadeDrive(0.6, -0.6); // drive forwards half speed
		} else {
			m_robotDrive.stopMotor(); // stop robot
		}
		
	}

	public void teleopInit() {
		
	}

	public void teleopPeriodic() {
		traj.readSmartDashboard();
		double left = traj.leftDriveInput();
		double right = traj.rightDriveInput();
		SmartDashboard.putNumber("Robot Left Wheel", left);
		SmartDashboard.putNumber("Robot Right Wheel", right);
		currentButton = m_stick.getRawButton(1);
		
		if(!currentButton) {
			m_robotDrive.arcadeDrive(-m_stick.getY(), -m_stick.getX());
		} else if(currentButton) {
			if(!pastButton) {System.out.println("Warning! Initiating Auto Tracking");}
			m_robotDrive.tankDrive(right, left);
		} else {
			m_robotDrive.stopMotor();
		}
		
		pastButton = currentButton;
	}

	public void testPeriodic() {

	}
}
