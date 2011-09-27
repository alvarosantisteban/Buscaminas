package de.buscaminas;

import java.util.Random;

public class GameLogic { // LOGIC
	int nrRows;
	int nrMines;
	Quadrant quads[][];
	Random rand = new Random();
	
	public GameLogic(int rows){
		this.nrRows = rows;
		this.nrMines = (rows * rows) / 8;
		this.quads = new Quadrant[rows][rows];
		for (int row = 0; row < rows; row++){
			for (int col = 0; col < rows; col++){
				quads[row][col] = new Quadrant();
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
	
	public void setNumbers(){
    	for (int row = 0; row < nrRows; row++ ){
        	for (int col = 0; col < nrRows; col++ ){
        		if (this.quads[row][col].mineOnQuad){ 
        			// There is a Mine
        			//Check all the quadrants around this position
        			for(int x=row-1; x<=row+1; x++){
        				for(int y=col-1; y<=col+1; y++){
        					if(x<0 || x>=nrRows || y<0 || y>=nrRows || this.quads[x][y].mineOnQuad){
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
	
	public void explore( Quadrant qadrant ){
		for (int row = 0; row < nrRows; row++){
			for (int col = 0; col < nrRows; col++){
				quads[row][col].state = ViewState.DISCOVERED;
			}
		}
	}
}
