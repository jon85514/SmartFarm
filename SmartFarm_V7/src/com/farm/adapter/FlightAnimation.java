package com.farm.adapter;

import com.farm.farmdashboard.items.Item;

import javafx.scene.Node;

public interface FlightAnimation {
	public void startFlight();
	public void visitItem(Item item);
	public void scanFarm();
}
