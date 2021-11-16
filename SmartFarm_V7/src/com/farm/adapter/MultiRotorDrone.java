//refer to: " https://gitlab.cs.uab.edu/jesusaur/jdrone/blob/master/src/main/java/surelyhuman/jdrone/control/physical/MultiRotorDrone.java"

package com.farm.adapter;

import java.io.IOException;

public abstract class MultiRotorDrone extends PhysicalDrone implements FlightControllable {

	/***
	 * 
	 * @param back
	 * @throws IOException 
	 */
	public abstract void flyBackward(int back) throws IOException;

	/***
	 * 
	 * @param direction
	 * @throws IOException 
	 */
	public abstract void flip(String direction) throws IOException;

	/***
	 * 
	 * @param seconds
	 * @throws InterruptedException
	 * @throws IOException 
	 */
	public abstract void hoverInPlace(int seconds) throws InterruptedException, IOException;	

}

