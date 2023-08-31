// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;

/*this robot will have six REV SparkMaxes (attached to Neo motors) on its drivebase, three operating the left side and three operating the right
 * the "operator" will control three shooting capabilities:
 * 1. the intake: has two Neos (Sparkmaxes) and will both be controlled on one button; pull the gamepiece into the robot
 * 2. transport: uses one Neo to pull the game piece up from the intake to the shooter, controlled on one button
 * 3. shooter: uses one Neo, flings the ball out of the robot at high speed
 * the buttons in this case are again arbitrary -- you should always check in Driver Station which buttons should be used where
 * 
 * implements "ramping," a crucial safety device to protect the motors; sets minimum time to achieve max speed of a motor
 * 
 * note: to use REV or CTRE company products, you have to import each respective library (search for the import link)
 * REV library link: https://software-metadata.revrobotics.com/REVLib-2023.json
 * to import, open command panel (command shift p), select "manage vendor libraries," select install new libraries online (be online for this)
*/
public class Robot extends TimedRobot {

  /*this is where you will initialize the basic components of the robot
  */
    //MOTORS
      //motor initialization (the parameter for a SparkMax is its CAN ID and the type of motor attached (we use brushless))
      CANSparkMax l1 = new CANSparkMax(1, MotorType.kBrushless);
      CANSparkMax l2 = new CANSparkMax(2, MotorType.kBrushless);
      CANSparkMax l3 = new CANSparkMax(3, MotorType.kBrushless);
      CANSparkMax r1 = new CANSparkMax(4, MotorType.kBrushless);
      CANSparkMax r2 = new CANSparkMax(5, MotorType.kBrushless);
      CANSparkMax r3 = new CANSparkMax(6, MotorType.kBrushless);
      //use REV clients to identify and set CAN IDs, be careful of duplicates -- that causes big big problems fixed through wiring isolation

      CANSparkMax intakeLeft = new CANSparkMax(10, MotorType.kBrushless);
      CANSparkMax intakeRight = new CANSparkMax(11, MotorType.kBrushless);

      CANSparkMax transport = new CANSparkMax(20, MotorType.kBrushless);

      CANSparkMax shoot = new CANSparkMax(30, MotorType.kBrushless);

      //make sure to keep track of the naming mechanisms you're using. It might be helpful to specify these are motors, if you would like
      //when working with other people, always have the same naming conventions across the board
      

    //MOTOR CONTROLLER GROUPS
      //MotorControllerGroups group together each side, allowing them to drive as sides as opposed to individual motors
      MotorControllerGroup left = new MotorControllerGroup(l1, l2, l3);
      MotorControllerGroup right = new MotorControllerGroup(r1, r2, r3);
    
    //DIFFERENTIAL DRIVE  
      //Differential Drives distinguish the left and right sides as distinct sides, allowing for driving capability
      DifferentialDrive dT = new DifferentialDrive(left, right);
    
    //CONTROLLERS
      /*controller initiallization (the position of the name of the controller in the list of controllers in Driver Station will dictate it's parameter.
      to keep things consistent, driver should always be at location 0, i.e. the top of the list, and operator should always be at loc 1, i.e. second from top)
      */
      //"Joystick" controls the janky logitech controllers; if you wish to use Xbox Controllers, use "XboxController" (keep in mind button/axis values are different)
      Joystick driver = new Joystick(0);
      Joystick operator = new Joystick(1);

  @Override
  public void robotInit() {
    //will run as soon as the robot connects, even without being enabled
    l1.setOpenLoopRampRate(0.4);
    l2.setOpenLoopRampRate(0.4);
    l3.setOpenLoopRampRate(0.4);
    r1.setOpenLoopRampRate(0.4);
    r2.setOpenLoopRampRate(0.4);
    r3.setOpenLoopRampRate(0.4);
  }

  @Override
  public void robotPeriodic() {
    //will run periodically while robot is connected
  }

  @Override
  public void autonomousInit() {
    //will run at the beginning of autonomous period
  }

  @Override
  public void autonomousPeriodic() {
    //will run periodically throughout autonomous period
  }

  @Override
  public void teleopInit() {
    //will run at the beginning of teleop period
  }

  @Override
  public void teleopPeriodic() {
    //will run periodically throughout teleop period
    //where most of the code goes in Time Based programming
    
    //to clean up this method, we're going to write our own methods to put in here for ease of testing and application
    drive();
    intakePiece();
    transportPiece();
    }

  @Override
  public void disabledInit() {
    //you get it by now
  }

  @Override
  public void disabledPeriodic() {}

  @Override
  public void testInit() {}

  @Override
  public void testPeriodic() {}

//NEW METHODS
  public void drive(){
    dT.arcadeDrive(driver.getRawAxis(0), driver.getRawAxis(1));
    //recognize this?
  }

  public void intakePiece(){
    //intake
    if(operator.getRawButton(1)){
      intakeLeft.set(0.5);
      intakeRight.set(0.5);
      //make sure to play with each polarity separately before putting them together -- some motors controllers are reversed
    }
    
    //spit out
    if(operator.getRawButton(2)){
      intakeLeft.set(-0.5);
      intakeRight.set(-0.5);
      //start with a lower magnitude of power then move up. each kind of motor is different and each task requires a different amount of power.
    }

    //if neither button is active
    if(!operator.getRawButton(1) && !operator.getRawButton(4)){
      intakeLeft.set(0);
      intakeRight.set(0);
      //again, important to set motors back to 0 when you don't need them
    }
  }

  public void transportPiece(){
    if(operator.getRawButton(3)){
      transport.set(0.3);
    }
    if(!operator.getRawButton(3)){
      transport.set(0);
    }
  }

  public void shoot(){
    if(operator.getRawButton(5)){
      shoot.set(0.8);
      //this kind of function requires a much larger magnitude of power
      //again, play with the polarity to make sure it should be positive=
    }
    if(!operator.getRawButton(5)){
      shoot.set(0);
    }
  }
}
