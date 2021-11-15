package com.farm.adapter;

import com.farm.farmdashboard.items.Item;

import main.java.surelyhuman.jdrone.control.physical.tello.TelloDrone;
import main.java.surelyhuman.jdrone.Constants;

public class TelloDroneAdapter implements FlightAnimation {
	
	TelloDrone telloDrone;
	
	public TelloDroneAdapter(TelloDrone telloDrone) {
		this.telloDrone = telloDrone;
	}

	@Override
	public void visitItem(Item item) {
		int xPos = item.getPosX() * Constants.PIXELS_TO_ONE_MODEL_FOOT;
		int yPos = item.getPosY()* Constants.PIXELS_TO_ONE_MODEL_FOOT;
		int zPos = item.getHeight()* Constants.PIXELS_TO_ONE_MODEL_FOOT;
		int speed = 20;
		
		try {
			telloDrone.gotoXYZ(xPos, yPos, zPos, speed);
			telloDrone.turnCW(360);
			telloDrone.hoverInPlace(10);
			telloDrone.flyBackward(yPos* Constants.CENTIMETERS_PER_MODEL_FOOT);
			telloDrone.flyRight(xPos * Constants.CENTIMETERS_PER_MODEL_FOOT);
			telloDrone.land();
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	@Override
	public void scanFarm() {
		int gap = 5;
		int xPos = Constants.FARMHEIGHT - gap;
		int yPos = Constants.FARMWIDTH - gap;
		int zPos = Constants.FARMDEPTH - gap;
		int speed = 20;
		int xDist = xPos;
		try {
			telloDrone.gotoXYZ(xPos, yPos, zPos, speed);
			while (xDist > 0) {
			telloDrone.turnCW(90);
			telloDrone.flyForward(gap);
			telloDrone.turnCW(90);
			telloDrone.flyBackward(yPos);
			telloDrone.turnCW(90);
			telloDrone.flyBackward(gap);
			telloDrone.turnCW(90);
			telloDrone.flyForward(yPos);
			xDist -= gap;
			}
			telloDrone.gotoXYZ(gap, gap, 0, speed);
			telloDrone.land();
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	@Override
	public void startFlight() {
		try {
		telloDrone.activateSDK();
		telloDrone.streamOn();
		telloDrone.streamViewOn();
		telloDrone.hoverInPlace(10);
		telloDrone.takeoff();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
