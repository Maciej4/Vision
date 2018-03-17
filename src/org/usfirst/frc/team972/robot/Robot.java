package org.usfirst.frc.team972.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

public class Robot extends IterativeRobot {
	/**Object Declarations*/
	//Main robot drive, includes two TalonSRXs
	private DifferentialDrive m_robotDrive;
	//Main_Joystick
	private Joystick m_stick;
	//Trajectory object, see Trajectory.java for more information
	Trajectory traj = new Trajectory();
	
	/**Variable Declarations*/
	//Heading from raspberry pi
	double heading;
	double storePID;
	//Button booleans on main_joystick
	boolean button1;
	boolean button2;
	boolean button3;
	boolean button4;
	boolean button5;
	boolean button6;
	//The drive values for ultra_basic drive
	double left;
	double right;
	//Was the robot under remote operation in the last run, used for driver warning
	boolean pastHumanControl;
	
	/**Robot_Init, runs once on robot startup*/
	public void robotInit() {
		//Object initialization, joystick_on port 0, TalonSRXs ports 1 and 2 for m_robotDrive
		m_stick = new Joystick(0);
		m_robotDrive = new DifferentialDrive(new WPI_TalonSRX(1), new WPI_TalonSRX(2));
		traj.init();
	}
	
	/**Autonomous functions*/
	//Autonomous_init, runs once on start of autonomous period
	public void autonomousInit() {
		
	}

	//Autonomous periodic, runs repeatedly during autonomous period
	public void autonomousPeriodic() {
		
	}
	
	/**Teleoperated_functions*/
	//Teleoperated_Initiation, runs once at start of teleop_period
	public void teleopInit() {
		
	}
	
	//Teleoperated_Periodic, runs repeatedly during teleop_period
	public void teleopPeriodic() {
		//Reads main_joystick buttons
		button1 = m_stick.getRawButton(1);
		button2 = m_stick.getRawButton(2);
		button3 = m_stick.getRawButton(3);
		button4 = m_stick.getRawButton(4);
		button5 = m_stick.getRawButton(5);
		button6 = m_stick.getRawButton(6);
		//Reads_smartdashboard, receiving data from raspberry pi
		traj.readSmartDashboard();
		heading = traj.getHeading();
		
		//Goes through all buttons in sequence, checking if they are pressed and running routines
		if(button3) {
			
		} else if(button4) {
			//PID tracking routine, see Trajectory.java for more information
			storePID = traj.basicPIDDrive();
			if(Math.abs(traj.getHeading())<2) {
				//If the heading is less than 2 degrees, then it will drive forward at 0.5
				m_robotDrive.arcadeDrive(0.5, storePID);
			} else {
				//If the heading is greater than 2 degrees, then it will turn on the spot
				m_robotDrive.arcadeDrive(0, storePID);
			}
			System.out.println("Heading: "+heading+" | Turn Drive: "+storePID);
		} else if(button5) {
			
		} else if(button6) {
			//Very basic autonomous drive, see Trajectory.java for more information
			//Places outputs into variables, to allow for data printing to be accurate
			left = traj.ultraBasicDrive(true);
			right = traj.ultraBasicDrive(false);
			m_robotDrive.tankDrive(right, left);
			System.out.println("Heading: "+heading+" | Left: "+left+" | Right: "+right);
		} else {
			//If no buttons are pressed, then the robot is under human control
			m_robotDrive.arcadeDrive(-m_stick.getY(), -m_stick.getX());
		}
		
		//Prints warning messages for driver about the state of the robot control
		if(button3||button4||button5||button6) {
			if(pastHumanControl) {System.out.println("Warning! Disabling autonomous tracking!");}
			pastHumanControl = false;
		} else {
			if(!pastHumanControl) {System.out.println("Warning! Enabling autonomous tracking!");}
			pastHumanControl = true;
		}
	}

	public void testPeriodic() {
		
	}
}
