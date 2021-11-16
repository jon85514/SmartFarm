//refer to: "https://gitlab.cs.uab.edu/jesusaur/jdrone/blob/master/src/main/java/surelyhuman/jdrone/control/physical/PhysicalDrone.java"

package com.farm.adapter;

import java.io.IOException;

public abstract class PhysicalDrone {

	protected DroneController controller;

	/***
	 * 
	 * @param speed
	 * @throws IOException 
	 */
	public abstract void setSpeed(int speed) throws IOException;

	/***
	 * 
	 * @return
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	public abstract double getSpeed() throws NumberFormatException, IOException;

	/***
	 * 
	 * @return
	 * @throws IOException 
	 * @throws NumberFormatException 
	 */
	public abstract int getBattery() throws NumberFormatException, IOException;

}
