package com.farm.view;

import com.farm.adapter.FlightAnimation;
import com.farm.adapter.TelloDroneAdapter;
import com.farm.farmdashboard.*;
import com.farm.farmdashboard.items.Item;
import com.farm.farmdashboard.items.ItemContainer;
import com.farm.farmdashboard.items.ItemLeaf;
import com.farm.visitor.MarketValueVisitor;
import com.farm.visitor.PurchasePriceVisitor;
import com.farm.visitor.Visitor;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PathTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.animation.PathTransition.OrientationType;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.ArcTo;
import javafx.scene.shape.HLineTo;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.VLineTo;
import javafx.scene.paint.Color;

import java.io.FileNotFoundException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import javafx.util.Duration;
import main.java.surelyhuman.jdrone.control.physical.tello.TelloDrone;
import javafx.scene.text.Text;

/*
 * Controller class to handle user interaction with 
 * SmartFarmDashboard
 */

public class SmartFarmDashboardController implements FlightAnimation {

	
    private static SmartFarmDashboardController INSTANCE;
    // Model

    public static SmartFarmDashboardController getInstance() {
            if(INSTANCE == null) {
                    INSTANCE = new SmartFarmDashboardController();
            }
            return INSTANCE;
    }
	
	// Model
	private ArrayList<Item> farmItems;
	/* TODO:
	 * @FXML annotations
	 *  initialize() method
	 */

	/*
	 * FXML annotations
	 */
	//Left side of Dashboard (Actions/Items)
	@FXML
	private SplitPane ControlsSplitPane;
	@FXML
	private AnchorPane ItemsAnchorPane;
	@FXML
	private AnchorPane ActionsAnchorPane;
	@FXML
	private Label ItemsLabel;
	@FXML
	private TreeView<Item> ItemsTreeView;
	@FXML
	private GridPane SelectedItemProperties;
	// Selected Item Properties
	@FXML
	private TextField SelectedName;
	@FXML
	private TextField SelectedPosX;
	@FXML
	private TextField SelectedPosY;
	@FXML
	private TextField SelectedLength;
	@FXML
	private TextField SelectedWidth;
	@FXML
	private TextField SelectedHeight;
	@FXML
	private TextField SelectedPrice;
    @FXML
    private Label ValueLabel;
    @FXML
    private TextField SelectedValue;
    @FXML
	private Label nameLabel;
	@FXML
	private Label priceLabel;
	@FXML
	private Label locationLabel;
	@FXML
	private Label dimensionLabel;
    
    // under gridPane
    @FXML
	private ComboBox<String> commandChoice;
	@FXML
    private Label PurchasePriceLabel;
    @FXML
    private Label MarketValueLabel;
    
	// Drone Actions
	@FXML
	private Label DroneActionsLabel;
	@FXML
	private Button VisitItemAction;
	@FXML
	private Button ScanFarmAction;
	@FXML
	private Button AddDrone;
	@FXML
    private Button DroneLaunchButton;
	
	
	//Right Side (Farm View)
	@FXML
	private AnchorPane FarmViewVBox;
	@FXML
	private ImageView drone;
	@FXML
	private ImageView crops;
	@FXML
	private ImageView barn;
	
	/*
	 * End of FXML annotations
	 */
	private ObservableList<String> commandOptions = FXCollections.observableArrayList(
			"Rename", "Change Location", "Change Price", "Change Dimension", "Change Value");
	//Reference to Main
	private Main main;

	//Drone visits the item that is selected in the treeview then returns to it's location
	//Before visiting the item
	@FXML
	private void handleVisitItemAction(ActionEvent event) {
		Item item = getSelectedItem();
		visitItem(item);
	}
	
	@FXML
	private void handleScanFarmAction(ActionEvent event) {			
		scanFarm();
	}

	//Adds the drone and the command center 
	@FXML
	private void addDrone(ActionEvent event) {
		drone.setVisible(true);
		int index = getSelectedItemIndex();
		farmItems.add(new ItemContainer("Command Center", 0, 5,5,130,130,130, 0, new ArrayList<Item>(){
			{
				add(new ItemLeaf("Drone", 4, 20, 20, 50, 50, 50, 30));
			}
		}));
		updateFarmItemsTree();
		MultipleSelectionModel msm = ItemsTreeView.getSelectionModel();
		msm.select(index);
	}
			
	

	//Add Item Leaf
	@FXML
	private void addNewFarmItemLeaf(ActionEvent event) {
		addNewFarmItem(new ItemLeaf("New_Item",0,0,0,0,0,0,0));
	}

	//Add Item Container
	@FXML
	private void addNewFarmItemContainer(ActionEvent event) {
		addNewFarmItem(new ItemContainer("New_ItemContainer",0,0,0,0,0,0,0, new ArrayList<Item>()));
	}

	//Add new item/itemcontainer to the farm
	private void addNewFarmItem(Item newItem) {
		int index = getSelectedItemIndex();
		Item selectedItem = getSelectedItem();
		if (selectedItem != null && selectedItem.getChildren() != null) {
			selectedItem.addChild(newItem);
		} else {
			farmItems.add(newItem);
		}
		updateFarmItemsTree();
		MultipleSelectionModel msm = ItemsTreeView.getSelectionModel();
		msm.select(index);
	}

	//Deletes selected item from farm
	@FXML
	private void deleteFarmItem(ActionEvent event) {
		Item itemToRemove = getSelectedItem();
		if (itemToRemove == null || String.valueOf(itemToRemove) == "Farm") return;
		TreeItem<Item> item = ItemsTreeView.getSelectionModel().getSelectedItem();
		TreeItem<Item> itemParent = item.getParent();

		if (String.valueOf(itemParent.getValue()) == "Farm") {
			farmItems.remove(itemToRemove);
		} else {
			itemParent.getValue().removeChild(itemToRemove);
		}

		updateFarmItemsTree();
	}
	
	public void invalidAlarm() {
		Alert alert = new Alert(Alert.AlertType.WARNING);
    	alert.setHeaderText("Entered Values Not Saved\n Exceed the boundary of the Farm");
    	alert.show();
	}
	//Updates the values for the current selected item
	//Checks to make sure inputs are valid for item
	@FXML
	private void saveSelectedItemChanges(ActionEvent event) {
		
		Item selectedItem = getSelectedItem();	
		int index = getSelectedItemIndex();
		selectedItem.setName(SelectedName.getText());
		selectedItem.setPosition(Integer.parseInt(SelectedPosX.getText()), Integer.parseInt(SelectedPosY.getText()));
		selectedItem.setDimensions(Integer.parseInt(SelectedLength.getText()),
				                   Integer.parseInt(SelectedWidth.getText()),
				                   Integer.parseInt(SelectedHeight.getText()));
		selectedItem.setPrice(Double.parseDouble(SelectedPrice.getText()));
		selectedItem.setValue(Double.parseDouble(SelectedValue.getText()));
		
		//Error checking to make sure item/container will not extend outside of the farm's x and y boundaries
		if (selectedItem.getPosX() + selectedItem.getLength() >= 600) 
		{
			invalidAlarm();
			return;
		}
		if (selectedItem.getPosY() + selectedItem.getWidth() >= 800) 
		{
			invalidAlarm();
			return;
		}
		ItemsTreeView.refresh();
		updateFarmItemsTree();
		MultipleSelectionModel msm = ItemsTreeView.getSelectionModel();
		msm.select(index);
		
		SelectedName.setEditable(false);
		SelectedPosX.setEditable(false);
		SelectedPosY.setEditable(false);
		SelectedLength.setEditable(false);
		SelectedWidth.setEditable(false);
		SelectedHeight.setEditable(false);
		SelectedPrice.setEditable(false);
		SelectedValue.setEditable(false);
		
		nameLabel.setTextFill(Color.BLACK);
		priceLabel.setTextFill(Color.BLACK);
		locationLabel.setTextFill(Color.BLACK);
		dimensionLabel.setTextFill(Color.BLACK);
		ValueLabel.setTextFill(Color.BLACK);
	}
	
	@FXML
	private void updateSelectedItem(ActionEvent event){
		String selectedCommand = commandChoice.getSelectionModel().getSelectedItem();
		if (selectedCommand.equals("Rename")) {
			nameLabel.setTextFill(Color.RED);
			SelectedName.setEditable(true);
	    } else if (selectedCommand.equals("Change Location")) {
	    	locationLabel.setTextFill(Color.RED);
			SelectedPosX.setEditable(true);
			SelectedPosY.setEditable(true);
	    } else if (selectedCommand.equals("Change Price")) {
	    	priceLabel.setTextFill(Color.RED);
			SelectedPrice.setEditable(true);
	    } else if (selectedCommand.equals("Change Dimension")) {
	    	dimensionLabel.setTextFill(Color.RED);
			SelectedLength.setEditable(true);
			SelectedWidth.setEditable(true);
			SelectedHeight.setEditable(true);
	    } else if (selectedCommand.equals("Change Value")) {
	    	ValueLabel.setTextFill(Color.RED);
	    	SelectedValue.setEditable(true);
	    }
	}
	
	//Draws an itemLeaf
	private void drawItem(Item item) {
		Rectangle rect = new Rectangle(item.getPosX(), item.getPosY(), item.getLength(), item.getWidth());	
		rect.setAccessibleText(item.getName());
		rect.setStroke(Color.RED);
		rect.setStrokeWidth(2);
		rect.setFill(Color.TRANSPARENT);
		FarmViewVBox.getChildren().add(rect);
	}
	
	//Draws an ItemContainer
	private void drawItemContainer(Item itemContainer) {
		Rectangle rect = new Rectangle(itemContainer.getPosX(), itemContainer.getPosY(), itemContainer.getLength(), itemContainer.getWidth());
		rect.setAccessibleText(itemContainer.getName());
		rect.setStroke(Color.GREEN);
		rect.setStrokeWidth(5);
		rect.setFill(Color.TRANSPARENT);
		FarmViewVBox.getChildren().add(rect);
	}

	// Redraws the Farm Items Tree according to the farmItems ArrayList
	// Also redraws the visual items/containers in the farm
	private void updateFarmItemsTree() {
		ItemsTreeView.getRoot().getChildren().clear();
		FarmViewVBox.getChildren().clear();	
		farmItems.forEach(item -> {
			addChildrenToItemsTree(item, ItemsTreeView.getRoot());
			drawTreeItems(item, ItemsTreeView.getRoot());
		});
	}

	// If item is Leaf, add self. If item has children, add self and then recurse on children.
	void addChildrenToItemsTree(Item item, TreeItem<Item> parent) {
		TreeItem<Item> newTreeItem = new TreeItem<Item>(item);
		parent.getChildren().add(newTreeItem);
		while (item.hasNext()) {
			addChildrenToItemsTree(item.next(), newTreeItem);
		}
		parent.setExpanded(true);
	}	
	
	//Loops through tree and redraws all containers/leafs
	void drawTreeItems(Item item, TreeItem<Item> parent) {
		TreeItem<Item> newTreeItem = new TreeItem<Item>(item);
		Text text = new Text(item.getPosX(), item.getPosY() - 5, item.getName());
		text.setAccessibleText(item.getName());
		FarmViewVBox.getChildren().add(text);			
		if (item != null && item.getChildren() != null) 
		{
			drawItemContainer(item);
		} else {
			if (item.getName().equals("Drone")) {
				drone.setX(item.getPosX());
				drone.setY(item.getPosY());
				FarmViewVBox.getChildren().add(drone);
				}
			else {drawItem(item);}				
		}
		while (item.hasNext()) {
			drawTreeItems(item.next(), newTreeItem);
		}
	}
	
	// calculate the purchase price for displaying on the label
		public double calculatePurchasePrice(Item item) {		
			double purchasePrice;
			Visitor purchasePriceVisitor = new PurchasePriceVisitor();
			purchasePrice = item.accept(purchasePriceVisitor);
			return purchasePrice;
		}
		// calculate the market value for displaying on the label
		public double calculateMarketValue(Item item) {
			double marketValue;
			Visitor marketValueVisitor = new MarketValueVisitor();
			marketValue = item.accept(marketValueVisitor);
			return marketValue;
		}
		// Show purchase price on label
		public void displayPurchasePrice(Item selectedItem) {
			double purchasePrice = calculatePurchasePrice(selectedItem);
			//System.out.println("Purchase Price is: " + purchasePrice);
			PurchasePriceLabel.textProperty().bind(new SimpleDoubleProperty(purchasePrice).asString());
		}
		// Show market value on label
		public void displayMarketValue(Item selectedItem) {
			double marketValue = calculateMarketValue(selectedItem);
			//System.out.println("Market Value is: " + marketValue);
			MarketValueLabel.textProperty().bind(new SimpleDoubleProperty(marketValue).asString());
		}
	@FXML
	public void initialize() {
		
		// Initialize farmItems
		farmItems = new ArrayList<Item>();
		// Establish a Root Item
		ItemLeaf farm = new ItemLeaf("Farm", 0, 0,0,0,0,0, 0);
		TreeItem<Item> root = new TreeItem<Item>(farm);
		root.setExpanded(true);
		ItemsTreeView.setRoot(root);
		ItemsTreeView.setShowRoot(false);
		
		// set up command box
		commandChoice.setPromptText("Update");
		commandChoice.setItems(commandOptions);

		// Add event listeners
		ItemsTreeView.getSelectionModel().selectedItemProperty().addListener((ChangeListener)
				(observable, oldValue, newValue) -> {
			Item selectedItem = getSelectedItem();
			if (selectedItem == null) {
				ItemsTreeView.getSelectionModel().select(ItemsTreeView.getRoot());
				selectedItem = getSelectedItem();
			};
			SelectedName.setText(selectedItem.getName());
			SelectedPosX.setText(String.valueOf(selectedItem.getPosX()));
			SelectedPosY.setText(String.valueOf(selectedItem.getPosY()));
			SelectedLength.setText(String.valueOf(selectedItem.getLength()));
			SelectedWidth.setText(String.valueOf(selectedItem.getWidth()));
			SelectedHeight.setText(String.valueOf(selectedItem.getHeight()));
			SelectedPrice.setText(String.valueOf(selectedItem.getPrice()));
			if (selectedItem instanceof ItemContainer) {
				ValueLabel.setVisible(false);
				SelectedValue.setVisible(false);
			} else {
				ValueLabel.setVisible(true);
				SelectedValue.setVisible(true);
				SelectedValue.setText(String.valueOf(selectedItem.getValue()));
			}
				
			SelectedValue.setText(String.valueOf(selectedItem.getValue()));
			displayPurchasePrice(selectedItem);
			displayMarketValue(selectedItem); 
		});
		
	}

	//Gets the index of the selected item in treeview
	int getSelectedItemIndex() {
		int selectedTreeItemIndex = ItemsTreeView.getSelectionModel().getSelectedIndex();
		return selectedTreeItemIndex;
	}
	
	//Gets the value of the selected item in treeview
	Item getSelectedItem() {
		TreeItem<Item> selectedTreeItem = (TreeItem<Item>) ItemsTreeView.getSelectionModel().getSelectedItem();
		if (selectedTreeItem == null) { return null;}
		return selectedTreeItem.getValue();
	}

	@Override
	public void visitItem(Item item) {
		double xPos = drone.getX(); //original drone x and y positions
		double yPos = drone.getY();
		double dLength = drone.getLayoutBounds().getHeight();
		double dWidth = drone.getLayoutBounds().getWidth();
		// Create the Timelines
		Timeline rotate0 = new Timeline();
		Timeline rotate = new Timeline();
		Timeline moveDiagonal = new Timeline();
		Timeline moveUp = new Timeline();
		Timeline rotateNext = new Timeline();
		Timeline rotateLast = new Timeline();
		Timeline moveLeft = new Timeline();
		
		SequentialTransition sequence = new SequentialTransition();
		
		// Define the Durations
		Duration startDuration = Duration.ZERO;
		Duration endDuration = Duration.seconds(4);
		Duration endDuration1 = Duration.seconds(3);
		Duration endDuration2 = Duration.seconds(2);
		
//		System.out.println("xPos="+xPos+", yPos="+yPos+", itemX="+item.getPosX()+", itemY="+item.getPosY());
//		System.out.println("dLength="+dLength+", dWidth="+dWidth);
		// Create Key Frames
		KeyValue startKeyValue = new KeyValue(drone.translateXProperty(), xPos);
		KeyFrame startKeyFrameDiagonal = new KeyFrame(startDuration, startKeyValue);
		KeyValue endKeyValueX = new KeyValue(drone.translateXProperty(), item.getPosX()-2.5 * xPos);
		KeyValue endKeyValueY = new KeyValue(drone.translateYProperty(), item.getPosY()-2.5 * yPos);
		KeyFrame endKeyFrameDiagonal = new KeyFrame(endDuration, endKeyValueX, endKeyValueY);
		
		KeyValue endKeyValueRotate0 = new KeyValue(drone.rotateProperty(), drone.getRotate());
		KeyFrame endKeyFrameRotate0 = new KeyFrame(endDuration1, endKeyValueRotate0);
		
		KeyValue endKeyValueRotate = new KeyValue(drone.rotateProperty(), drone.getRotate() + 180);
		KeyFrame endKeyFrameRotate = new KeyFrame(endDuration2, endKeyValueRotate);
		
		KeyValue endKeyValueMoveUp = new KeyValue(drone.translateYProperty(), yPos);
		KeyFrame endKeyFrameMoveUp = new KeyFrame(endDuration, endKeyValueMoveUp);
		
		KeyValue endKeyValueRotateNext = new KeyValue(drone.rotateProperty(),  drone.getRotate());
		KeyFrame endKeyFrameRotateNext = new KeyFrame(endDuration2, endKeyValueRotateNext);
		
		KeyValue endKeyValueMoveLeft = new KeyValue(drone.translateXProperty(), xPos);
		KeyFrame endKeyFrameMoveLeft = new KeyFrame(endDuration, endKeyValueMoveLeft);
		
		KeyValue endKeyValueRotateLast = new KeyValue(drone.rotateProperty(),  drone.getRotate());
		KeyFrame endKeyFrameRotateLast = new KeyFrame(endDuration2, endKeyValueRotateLast);

		// Create Timelines
		rotate0 = new Timeline(endKeyFrameRotate0);
		rotate = new Timeline(endKeyFrameRotate);
		rotateNext = new Timeline(endKeyFrameRotateNext);
		moveDiagonal = new Timeline(startKeyFrameDiagonal, endKeyFrameDiagonal);
		moveUp = new Timeline(endKeyFrameMoveUp);
		moveLeft = new Timeline(endKeyFrameMoveLeft);
		rotateLast = new Timeline(endKeyFrameRotateLast);
		
		// Create Sequence
		sequence = new SequentialTransition(moveDiagonal, rotate0, rotate, moveUp, rotateNext, moveLeft, rotateLast);
		// Let the animation run forever
		sequence.setCycleCount(1);
		sequence.play();
		sequence.setOnFinished((e)->{
        	Alert alert = new Alert(Alert.AlertType.INFORMATION);
        	alert.setHeaderText("Visit Completed");
        	alert.show();
        });
		
//		// TelloDrone Fly to visit item
//		TelloDroneAdapter telloAdapter = startDrone();
//		telloAdapter.visitItem(getSelectedItem());
	}

	@Override
	public void scanFarm() {

		/*
		 *  Farm is 800px tall and 600px wide
		 *  Drone is 50x50
		 *  For now I am just going to subtract 50 from where I actually want to go on the (x, y)
		 *  ArcTo will be good for flying directly to a specified coordinate. 
		 *  
		 */
		double xPos = drone.getX();
		double yPos = drone.getY();
		
		Path scanpath = new Path(); 
		ArcTo arcpath = new ArcTo();		
		
		//Path of the drone so that it travels over every pixel of the farm
		//Obviously needs to be changed but good enough for now
		scanpath.getElements().add(new MoveTo(50, 50)); //Initial position (0,0) taking into consideration the drone's 50x50 size
		scanpath.getElements().add(new HLineTo(550)); //Horizontal move to the top right corner
		scanpath.getElements().add(new VLineTo(750)); //Vertical move down to bottom right corner
		scanpath.getElements().add(new HLineTo(500)); //Horizontal move left 50
		scanpath.getElements().add(new VLineTo(50)); //Vertical move back to top
		scanpath.getElements().add(new HLineTo(450)); //Horizontal move left 50
		scanpath.getElements().add(new VLineTo(750)); //Vertical move back to bottom
		scanpath.getElements().add(new HLineTo(400)); //Horizontal move left 50
		scanpath.getElements().add(new VLineTo(50)); //Vertical move back to top
		scanpath.getElements().add(new HLineTo(350)); //Horizontal move left 50
		scanpath.getElements().add(new VLineTo(750)); //Vertical move back to bottom
		scanpath.getElements().add(new HLineTo(300)); //Horizontal move left 50
		scanpath.getElements().add(new VLineTo(50)); //Vertical move back to top
		scanpath.getElements().add(new HLineTo(250)); //Horizontal move left 50
		scanpath.getElements().add(new VLineTo(750)); //Vertical move back to bottom
		scanpath.getElements().add(new HLineTo(200)); //Horizontal move left 50
		scanpath.getElements().add(new VLineTo(50)); //Vertical move back to top
		scanpath.getElements().add(new HLineTo(150)); //Horizontal move left 50
		scanpath.getElements().add(new VLineTo(750)); //Vertical move back to bottom
		scanpath.getElements().add(new HLineTo(100)); //Horizontal move left 50
		scanpath.getElements().add(new VLineTo(50)); //Vertical move back to top
		scanpath.getElements().add(new HLineTo(50)); //Horizontal move left 50
		scanpath.getElements().add(new VLineTo(750)); //Vertical move back to bottom
		scanpath.getElements().add(new VLineTo(50)); //Vertical move back to top
		scanpath.getElements().add(new VLineTo(yPos + 50));
		scanpath.getElements().add(new HLineTo(xPos + 50));
		/*
		arcpath.setX(50); //Destination Horizontal (back to dock)
		arcpath.setY(50);// Destination Vertical (back to dock)
		arcpath.setRadiusX(550); //radius of the arc x,y nice and wide to pass over the middle of the farm
		arcpath.setRadiusY(550);
		scanpath.getElements().add(arcpath); //add arcpath to the scanpath
		*/
		PathTransition scantransition = new PathTransition();
		scantransition.setNode(drone);
		scantransition.setDuration(Duration.millis(10000));
		scantransition.setPath(scanpath); 
		scantransition.setCycleCount(1);
		scantransition.play();
		scantransition.setOnFinished((e)->{
        	Alert alert = new Alert(Alert.AlertType.INFORMATION);
        	alert.setHeaderText("Scan Completed");
        	alert.show();
        });
		
		//for testing, would be nice to disable button and change it's text to "scanning" while it is running
		System.out.println("Current state: "+ scantransition.getStatus());
		//ScanFarmAction.setText("Scanning...");
		//ScanFarmAction.setText("Scan Farm");
		
//		// TelloDrone fly to scan farm
//		TelloDroneAdapter telloAdapter = startDrone();
//		telloAdapter.scanFarm();
		
	}
	
    @FXML
    void launchDrone(ActionEvent event) {
    	
    }
    
    public TelloDroneAdapter startDrone(){
		TelloDroneAdapter telloAdapter = null;
		try {
		TelloDrone telloDrone = new TelloDrone();
    	telloAdapter = new TelloDroneAdapter(telloDrone);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return telloAdapter;
	}
}
