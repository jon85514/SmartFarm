package com.farm.visitor;

public interface Visitable {
	
	public double accept(Visitor visitor);
}
