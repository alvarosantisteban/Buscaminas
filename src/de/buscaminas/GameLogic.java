package de.buscaminas;

import java.util.Random;

/**
 * Contains all the logic of the game including the internal creation of the array of Quadrants, the distribution of mines on it and the control
 * of the actions that the user can make. 
 * 
 * @author Christopher Büttner
 * @author Alvaro Santisteban
 * @version 1
 * 
 */
public class GameLogic { 
	
	/**
	 * Number of rows for the Buscaminas
	 */
	int nrRows;
	/**
	 * Numbers of mines for the Buscaminas
	 */
	int nrMines;
	/**
	 * Array with all the Quadrants that compose the Buscaminas
	 */
	Quadrant quads[][];
	/**
	 * Controls if the game has ended: true if it has ended
	 */
	boolean gameOver;
	/**
	 * Creates a random number
	 */
	Random rand = new Random();
	
	/**
	 * Constructor for the class. 
	 * 
	 * Initializes the variables, creates the array of Quadrants and, randomly, distributes the mines on it.
	 */
	public GameLogic(int rows){
		this.gameOver = false;
		this.nrRows = rows;
		this.nrMines = (rows * rows) / 8;
		this.quads = new Quadrant[rows][rows];
		for (int row = 0; row < rows; row++){
			for (int col = 0; col < rows; col++){
				quads[row][col] = new Quadrant( this );
			}
		}
		int distributed_mines = 0;
		while (distributed_mines < nrMines){
			int mine_col = rand.nextInt(rows); 
			int mine_row = rand.nextInt(rows);
			if (quads[mine_row][mine_col].mineOnQuad == false){
				distributed_mines += 1;
				quads[mine_row][mine_col].mineOnQuad = true;
			}
		}
	}
	
	
	/**
	 * Controls if a certain position is out of the array
	 * 
	 * @param x position in the columns
	 * @param y position in the rows
	 * @return true if the position is out of the array
	 */
	public boolean isOut(int x, int y){
		if(x<0 || x>=nrRows || y<0 || y>=nrRows){
			return true;
		}else{
			return false;
		}
	}
	
	/**
	 * Counts the number of bombs that surround each Quadrant from the Buscaminas
	 */
	public void setNumbers(){
    	for (int row = 0; row < nrRows; row++ ){
        	for (int col = 0; col < nrRows; col++ ){
        		if (this.quads[row][col].mineOnQuad){ 
        			// There is a Mine
        			//Check all the quadrants around this position
        			for(int x=row-1; x<=row+1; x++){
        				for(int y=col-1; y<=col+1; y++){
        					if(isOut(x,y) || this.quads[x][y].mineOnQuad){
        						//Out of at least one border or there is a bomb
        						//Don't do anything
        					}else{
        						//Set +1
        						this.quads[x][y].addOne();
        					}
        				}
        			}
        		}
        	}
        }
    }

	/**
	 * Returns the row where the given Quadrant is located
	 * @param quadrant the Quadrant from whom the row is wanted
	 * @return the number of the row. Returns "-1" if the Quadrant doesn't exist in the Buscaminas
	 */
	private int get_row( Quadrant quadrant ) {
		for (int row = 0; row < nrRows; row++){
			for (int col = 0; col < nrRows; col++){
				if (quads[row][col] == quadrant)
					return row;
			}
		}
		return -1;
	}
		
	/**
	 * Returns the column where the given Quadrant is located
	 * @param quadrant the Quadrant from whom the column is wanted
	 * @return the number of the column. Returns "-1" if the Quadrant doesn't exist in the Buscaminas
	 */
	private int get_col( Quadrant quadrant ) {
		for (int row = 0; row < nrRows; row++){
			for (int col = 0; col < nrRows; col++){
				if (quads[row][col] == quadrant)
					return col;
			}
		}
		return -1;
	}
	
	/**
	 * Controls if the clicked given Quadrant contains a bomb. 
	 * If that is so, the game ends. 
	 * If not, initializes the "explore state" for all quadrants and calls the recursive method explore_rec
	 * 
	 * @param quadrant the clicked Quadrant 
	 * @see explore_rec
	 */
	public void explore( Quadrant quadrant ){
		//Controls if the clicked given Quadrant contains a bomb. 
		if ( quadrant.mineOnQuad ){
			gameOver = true;
			for (int row = 0; row < nrRows; row++){
				for (int col = 0; col < nrRows; col++){
					quads[row][col].state = ViewState.BOMBED;
				}
			}
			return;
		}
		
		// initializes the explore state for all quadrants
		for (int row = 0; row < nrRows; row++){
			for (int col = 0; col < nrRows; col++){
				quads[row][col].rec_explore_state = false;
			}
		}
		
		explore_rec( quadrant );
		
	}
	
	/**
	 * Recursive method that for a given Quadrant explores the surrounding Quadrants to try to unveil (mark with the state DISCOVERED) 
	 * the maximum number of Quadrants that don't contain a bomb
	 * 
	 * @param quadrant the given Quadrant from which its surroundings are unveiled
	 */
	private void explore_rec( Quadrant quadrant ) {
		if (quadrant.rec_explore_state)
			return;
		quadrant.rec_explore_state = true;
		quadrant.state = ViewState.DISCOVERED;

		if ( quadrant.nrAdjacentMines > 0 )
			return;
			
		int row = get_row( quadrant );
		int col = get_col( quadrant );
		
		if ( ( row > 0) && ( col > 0 ) )
			explore_rec( quads[row - 1][col - 1]);

		if ( row > 0 )
			explore_rec( quads[row - 1][col]);

		if ( ( row > 0 ) && ( col < nrRows - 1 ) )
			explore_rec( quads[row - 1][col + 1]);
	
		if ( col > 0 )
			explore_rec( quads[row][col - 1]);

		if ( col < nrRows - 1 )
			explore_rec( quads[row][col + 1]);
		
		if ( row < nrRows - 1 ) 
			explore_rec( quads[row + 1][col] );
			
		if ( ( row < nrRows - 1 ) && ( col < nrRows - 1 ) )
			explore_rec( quads[row + 1][col + 1]);

		if ( ( row < nrRows - 1 ) && ( col > 0 ) )
			explore_rec( quads[row + 1][col - 1]);
		
	}
}
