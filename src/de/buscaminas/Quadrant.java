package de.buscaminas;

public class Quadrant {
	ViewState state;
	boolean mineOnQuad;
	public Quadrant(){
		this.state = ViewState.UNTOUCHED;
		this.mineOnQuad = false; 
	}
}
