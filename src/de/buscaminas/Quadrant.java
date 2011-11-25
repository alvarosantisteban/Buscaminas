package de.buscaminas;

/**
 * 
 * A Quadrant is a field situated in a concrete position inside a TableRow.
 * 
 * A Quadrant has an external and internal state and the numbers of mines that surround it.  
 * 
 * @author Alvaro Santisteban
 * @author Christopher Büttner
 * @version 1.5
 * 
 */
public class Quadrant {
	/**
	 * The external state of the Quadrant: UNTOUCHED, MARKED, DISCOVERED or BOMBED
	 */
	ViewState state;
	/**
	 * The internal state of the Quadrant: true if it contains a mine
	 */
	boolean mineOnQuad;
	/**
	 * The number of mines that surround the Quadrant
	 */
	int nrAdjacentMines;
	/**
	 * The game logic
	 */
	GameLogic game;
	/**
	 * A boolean that controls if the Quadrant has been explored during the method explore_rec: true if it has been already explored
	 */
	boolean rec_explore_state; 
	
	/**
	 * Constructor for the class that initializes all the variables of the class
	 * 
	 * @param game Game logic
	 */
	public Quadrant(GameLogic game){
		this.game = game;
		this.state = ViewState.UNTOUCHED;
		this.mineOnQuad = false;
		this.nrAdjacentMines = 0;
	}
	
	/**
	 * Increases the counter nrAdjacentMines, which controls the numbers of mines that surround the Quadrant
	 */
	void addOne(){
		this.nrAdjacentMines++;
	}
}
