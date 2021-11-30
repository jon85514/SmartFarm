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
		int factor = 10;
		int speed = 20;
		int xPos = item.getPosX() / Constants.PIXELS_TO_ONE_MODEL_FOOT * Constants.CENTIMETERS_PER_MODEL_FOOT / factor;
		int yPos = item.getPosY()/ Constants.PIXELS_TO_ONE_MODEL_FOOT * Constants.CENTIMETERS_PER_MODEL_FOOT/ factor;
		int zPos = item.getHeight()/ Constants.PIXELS_TO_ONE_MODEL_FOOT* Constants.CENTIMETERS_PER_MODEL_FOOT/ factor;
		
		
		
		try {
			telloDrone.activateSDK();
//			telloDrone.streamOn();
//			telloDrone.streamViewOn();
//			telloDrone.hoverInPlace(10);
			telloDrone.takeoff();
			
			telloDrone.gotoXYZ(xPos, yPos, zPos, speed);
			telloDrone.turnCW(360);
			telloDrone.hoverInPlace(10);
			telloDrone.flyBackward(yPos);
			telloDrone.flyLeft(xPos);
			telloDrone.land();
			telloDrone.end();
			} catch (Exception e) {
				e.printStackTrace();
			}
	}

	@Override
	public void scanFarm() {
		int factor = 10;
		int speed = 20;
		int gap = 100  / Constants.PIXELS_TO_ONE_MODEL_FOOT * Constants.CENTIMETERS_PER_MODEL_FOOT /factor ;
		int xPos = Constants.FARMHEIGHT/ Constants.PIXELS_TO_ONE_MODEL_FOOT * Constants.CENTIMETERS_PER_MODEL_FOOT / factor;
		int yPos = Constants.FARMWIDTH / Constants.PIXELS_TO_ONE_MODEL_FOOT * Constants.CENTIMETERS_PER_MODEL_FOOT / factor;
		int zPos = Constants.FARMDEPTH  / Constants.PIXELS_TO_ONE_MODEL_FOOT * Constants.CENTIMETERS_PER_MODEL_FOOT / factor;
		int xDist = yPos;
		
		try {
			telloDrone.activateSDK();
//			telloDrone.streamOn();
//			telloDrone.streamViewOn();
//			telloDrone.hoverInPlace(10);
			telloDrone.takeoff();
			
			telloDrone.gotoXYZ(xPos, yPos, zPos, speed);
			telloDrone.turnCW(90);
			telloDrone.turnCW(90);
			telloDrone.flyForward(xPos);
//			
			while (xDist > 0) {
				
				
				telloDrone.turnCW(90);
				telloDrone.flyForward(gap);
				telloDrone.turnCW(90);
				telloDrone.flyForward(xPos);
				telloDrone.turnCCW(90);
				telloDrone.flyForward(gap);
				telloDrone.turnCCW(90);
				telloDrone.flyForward(xPos);
				xDist -= 2* gap;
			}
//			telloDrone.gotoXYZ(gap, gap, 0, speed);
			telloDrone.land();
			telloDrone.end();
			} catch (Exception e) {
				e.printStackTrace();
			}
	}


}
