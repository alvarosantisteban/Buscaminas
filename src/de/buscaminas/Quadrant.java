package de.buscaminas;

public class Quadrant {
	ViewState state;
	boolean mineOnQuad;
	int nrAdjacentMines;
	GameLogic game;
	boolean rec_explore_state;
	
	public Quadrant(GameLogic game){
		this.game = game;
		this.state = ViewState.UNTOUCHED;
		this.mineOnQuad = false;
		this.nrAdjacentMines = 0;
	}
	
	void addOne(){
		this.nrAdjacentMines++;
	}
}
