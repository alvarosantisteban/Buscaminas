package de.buscaminas;

import java.util.Random;

/**
 * Contains all the logic of the game including the internal creation of the array of Quadrants, the distribution of mines on it and the control
 * of the actions that the user can make. 
 * 
 * @author Alvaro Santisteban
 * @author Christopher B�ttner
 * @version 1.5
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
	 * Number of fields marked
	 */
	int nrMarked;
	/**
	 * Number of mines marked
	 */
	int nrMinesMarked;
	/**
	 * Array with all the Quadrants that compose the Buscaminas
	 */
	Quadrant quads[][];
	/**
	 * Controls if the game has ended because the user clicked on a mine
	 */
	boolean gameOverLost;
	/**
	 * Controls if the game has ended because the user won
	 */
	boolean gameOverWin;
	/**
	 * Creates a random number
	 */
	Random rand = new Random();
	
	/**
	 * Constructor for the class. 
	 * 
	 * Initializes the variables, creates the array of Quadrants and, randomly, distributes the mines on it.
	 * 
	 * @param rows number of rows and columns of the game
	 * @param mines number of mines of the game
	 */
	public GameLogic(int rows, int mines){
		this.gameOverLost = false;
		this.gameOverWin = false;
		this.nrRows = rows;
		this.nrMines = mines;
		this.nrMarked = 0;
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
	 * Checks if the user won.
	 * 
	 * First, check if all bombs were marked, in which case, the user won. 
	 * If not, checks if the Quadrants without a bomb were discovered, in which case, the user also won.
	 * 
	 * @return true if the user won
	 */
	public boolean hasWon(){
		if(nrMinesMarked == nrMines){
			gameOverWin = true;
		}else{
			Quadrant q;
			for (int row = 0; row < nrRows; row++){
				for (int col = 0; col < nrRows; col++){
					q = quads[row][col];
					if(!q.mineOnQuad){
						if(q.state == ViewState.UNTOUCHED){//didn't win yet
							return gameOverWin;
						}
					}
				}
			}
			gameOverWin = true;		
		}
		return gameOverWin;
	}
	
	/**
	 * Controls if the clicked given Quadrant contains a mine. 
	 * If that is so, the game ends. 
	 * If not, initializes the "explore state" for all quadrants and calls the recursive method explore_rec
	 * 
	 * @param quadrant the clicked Quadrant 
	 * @return true if the given Quadrant contains a mine
	 * @see explore_rec
	 */
	public boolean explore( Quadrant quadrant ){
		//Controls if the clicked given Quadrant contains a mine. 
		if ( quadrant.mineOnQuad ){
			gameOverLost = true;
			for (int row = 0; row < nrRows; row++){
				for (int col = 0; col < nrRows; col++){
					quads[row][col].state = ViewState.BOMBED;
				}
			}
		}else{
		
			// initializes the explore state for all quadrants
			for (int row = 0; row < nrRows; row++){
				for (int col = 0; col < nrRows; col++){
					quads[row][col].rec_explore_state = false;
				}
			}
			
			explore_rec( quadrant );
		}
		return gameOverLost;
	}
	
	/**
	 * Recursive method that for a given Quadrant explores the surrounding Quadrants to try to unveil (mark with the state DISCOVERED) 
	 * the maximum number of Quadrants that don't contain a mine
	 * 
	 * @param quadrant the given Quadrant from which its surroundings are unveiled
	 */
	private void explore_rec( Quadrant quadrant ) {
		if (quadrant.rec_explore_state)
			return;
		quadrant.rec_explore_state = true;
		if(quadrant.state == ViewState.MARKED){
			decreaseMarked();
		}
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
	
	/**
	 * Increases the number of fields marked
	 */
	public void increaseMarked(){
		nrMarked++;
	}
	
	/**
	 * Decreases the number of fields marked
	 */
	public void decreaseMarked(){
		nrMarked--;
	}
	
	public String getRemainingMarks(){
		int i = nrMines-nrMarked;
		return new String(Integer.toString(i));
	}
	
	/**
	 * Increases the number of mines marked
	 */
	public void increaseMinesMarked(){
		nrMinesMarked++;
	}
	
	/**
	 * Decreases the number of mines marked
	 */
	public void decreaseMinesMarked(){
		nrMinesMarked--;
	}
}
