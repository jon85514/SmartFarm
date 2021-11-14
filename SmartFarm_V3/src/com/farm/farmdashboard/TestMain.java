package com.farm.farmdashboard;

import java.util.ArrayList;
import com.farm.visitor.*;

import com.farm.farmdashboard.items.Item;
import com.farm.farmdashboard.items.ItemContainer;
import com.farm.farmdashboard.items.ItemLeaf;

public class TestMain {
	public static void main(String args[]) {
		Item cow1 = new ItemLeaf("Cow",30,100,100,25,45,25,50);
		Item cow2 = new ItemLeaf("Cow",40,100,150,30,55,30,80);
		Item barn1 = new ItemContainer("Barn",400,50,50,400,400,200,600, new ArrayList<Item>());
		Item area1 = new ItemContainer("Milk-Storage-Area",200,200,250,100,100,100,300, new ArrayList<Item>());
		barn1.addChild(cow1);
		barn1.addChild(cow2);
		barn1.addChild(area1);
		
		double purchasePrice;
		Visitor purchasePriceVisitor = new PurchasePriceVisitor();
		purchasePrice = barn1.accept(purchasePriceVisitor);
		
		System.out.println("Purchase Price is: " + purchasePrice);
		
		double marketValue;
		Visitor marketValueVisitor = new MarketValueVisitor();
		marketValue = barn1.accept(marketValueVisitor);
		
		System.out.println("Market value is: " + marketValue);
	}
}
