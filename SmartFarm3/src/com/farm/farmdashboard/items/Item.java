package com.farm.farmdashboard.items;

import java.util.ArrayList;
import com.farm.visitor.Visitable;

public interface Item extends Visitable{
    // Setters
    public void setName(String name);
    public void setPrice(double price);
    public void setPosition(int x, int y);
    public void setDimensions(int length, int width, int height);
    public void setValue(double value);
    
    // Getters
    public String getName();
    public double getPrice();
    public int getPosX();
    public int getPosY();
    public int getLength();
    public int getWidth();
    public int getHeight();
    public double getValue();

    // Children
    public ArrayList<Item> getChildren();
    public void addChild(Item item);
    public void removeChild(Item item);

    // Iteration
    public Item next();
    public boolean hasNext();
}
