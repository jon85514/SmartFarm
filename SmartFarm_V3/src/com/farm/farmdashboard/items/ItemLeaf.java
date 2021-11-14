package com.farm.farmdashboard.items;

import java.util.ArrayList;

import com.farm.visitor.Visitor;

public class ItemLeaf implements Item {
    String name = "";
    double price = 0;
    int posX = 0;
    int posY = 0;
    int length = 0;
    int width = 0;
    int height = 0;
    double value = 0;

    // Constructors
    public ItemLeaf() {
    }

    public ItemLeaf(String name, double price, int x, int y, int l, int w, int h, double value) {
        this.name = name;
        this.price = price;
        posX = x;
        posY = y;
        length = l;
        width = w;
        height = h;
        this.value = value;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    };

    public void setPrice(double price) {
        this.price = price;
    };
    public void setValue(double value) {
        this.value = value;
    };

    public void setPosition(int x, int y){
        this.posX = x;
        this.posY = y;
    };

    public void setDimensions(int length, int width, int height) {
        this.length = length;
        this.width = width;
        this.height = height;
    };

    // Getters
    public String getName() {
        return name;
    };

    public double getPrice() {
        return price;
    };

    public double getValue() {
        return value;
    };

    public int getPosX() {
        return posX;
    };

    public int getPosY() {
        return posY;
    };

    public int getLength() {
        return length;
    };

    public int getWidth() {
        return width;
    };

    public int getHeight() {
        return height;
    };

    // Children
    public void addChild(Item item) { }

    public void removeChild(Item item) { }

    public ArrayList<Item> getChildren() {
        return null;
    }

    // Iteration
    public Item next() {
        return null;
    }

    public boolean hasNext() {
        return false;
    }

    // Utility
    public String toString() {
        return name;
    }
    // Visitor
    public double accept(Visitor visitor) {
    	return visitor.visit(this);
    };
}
