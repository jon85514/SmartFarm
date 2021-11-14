package com.farm.adapter;

import com.farm.farmdashboard.items.Item;

import javafx.scene.Node;
import main.java.surelyhuman.jdrone.control.physical.tello.TelloDrone;

public class TelloDroneAdapter implements FlightAnimation {
	
	TelloDrone telloDrone;
	
	public TelloDroneAdapter(TelloDrone telloDrone) {
		this.telloDrone = telloDrone;
	}

	@Override
	public void visitItem(Item item) {
		
		
	}

	@Override
	public void scanFarm() {
		
		
	}

	@Override
	public void startFlight() {
		
	}

}
