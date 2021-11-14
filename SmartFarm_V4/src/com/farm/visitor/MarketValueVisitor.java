package com.farm.visitor;

import com.farm.farmdashboard.items.Item;

public class MarketValueVisitor implements Visitor {
	// Constructor
	public MarketValueVisitor(){
		
	}
	@Override
	public double visit(Item item) {
		double total = 0.0;
		total += item.getValue();
		return total;
	}
}
