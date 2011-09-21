package de.buscaminas;

import java.util.Random;

public class GameLogic {
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

}
