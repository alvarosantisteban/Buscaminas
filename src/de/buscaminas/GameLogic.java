package de.buscaminas;

import java.util.Random;

public class GameLogic { // LOGIC
	int nrRows;
	int nrMines;
	Quadrant quads[][];
	boolean gameOver;
	Random rand = new Random();
	
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
	
	/*
	public void unveilQuadrant(Quadrant q, int row, int col){
		for(int x=row-1; x<=row+1; x++){
			for(int y=col-1; y<=col+1; y++){
				if (!isOut(x,y) && q.state == UNTOUCHED){
					if(hueco && !ha sido visitado){
						unveilQuadrant(q, x, y);
					}else{
						DESCUBRIR -> Pintar;
					}
				}
			}
		}
	}
	*/
	
	public boolean isOut(int x, int y){
		if(x<0 || x>=nrRows || y<0 || y>=nrRows){
			return true;
		}else{
			return false;
		}
	}
	
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

	private int get_row( Quadrant quadrant ) {
		for (int row = 0; row < nrRows; row++){
			for (int col = 0; col < nrRows; col++){
				if (quads[row][col] == quadrant)
					return row;
			}
		}
		return -1;
	}
		
	private int get_col( Quadrant quadrant ) {
		for (int row = 0; row < nrRows; row++){
			for (int col = 0; col < nrRows; col++){
				if (quads[row][col] == quadrant)
					return col;
			}
		}
		return -1;
	}
	
	public void explore( Quadrant quadrant ){
		
		if ( quadrant.mineOnQuad ){
			gameOver = true;
			for (int row = 0; row < nrRows; row++){
				for (int col = 0; col < nrRows; col++){
					quads[row][col].state = ViewState.BOMBED;
				}
			}
			return;
		}
		
		// init explore state for all quadrants
		for (int row = 0; row < nrRows; row++){
			for (int col = 0; col < nrRows; col++){
				quads[row][col].rec_explore_state = false;
			}
		}
		
		explore_rec( quadrant );
		
	}
	
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
