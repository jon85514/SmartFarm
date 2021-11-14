package com.farm.visitor;

import com.farm.farmdashboard.items.Item;

public class PurchasePriceVisitor implements Visitor {

	// Constructor
		public PurchasePriceVisitor(){
			
		}
	@Override
	public double visit(Item item) {
		double total = 0.0;
		total += item.getPrice();
		return total;
	}
}
