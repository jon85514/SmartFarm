package com.farm.visitor;

import com.farm.farmdashboard.items.Item;

public interface Visitor {
	
	public double visit(Item item);
}
