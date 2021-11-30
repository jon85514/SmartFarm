# assignment5

## URL: https://gitlab.cs.uab.edu/cs520_group1/assignment5.git

## Term project – Design and Implementation - Part 2 - Vistor and Adapter Pattern


### OverView

![](/pic1.PNG)

### Visitor Design Pattern

Two interfaces were created, Visitable and Visitor. Visitable interface included method accept() to interact with Visitor interface.  
Visitor interface provided visit(Item item) method to interact with Item interface.  
Two concrete class, MarketValueVistor and PurchasePriceVisitor, implemented Visitor interface.

##### Instructions

1. Select <mark>add item</mark> or <mark>add item container</mark>  
2. Change price or market price by selecting <mark>change price</mark> or <mark>change value</mark>
3. The result will show in the <mark>Purchase Price</mark> and <mark>Current Market Value</mark> label 

### Adapter Design Pattern

An interface FlightAnimation was created with two methods: visitItem(Item item) and scanFarm(), which was the targeted interface.  
DashboardOverviewController class implemented the FlightAnimation interface.  
By integrating a drone ImageView; TelloDroneAdapter class implemented the FlightAnimation interface by creating a TelloDrone object and call its methods.

##### Instructions

1. Select <mark>Launch Drone</mark> button  
2. Select <mark>Scan Farm</mark> or <mark>Visit Item/Item Container</mark> button  
3. Physic drone will react to the corresponding command