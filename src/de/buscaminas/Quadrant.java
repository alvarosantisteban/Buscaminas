package de.buscaminas;

public class Quadrant {
	ViewState state;
	boolean mineOnQuad;
	int number;
	
	public Quadrant(){
		this.state = ViewState.UNTOUCHED;
		this.mineOnQuad = false;
		this.number=0;
	}
	
	void addOne(){
		this.number++;
	}
}
