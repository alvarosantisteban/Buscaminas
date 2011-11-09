package de.buscaminas;

import de.buscaminas.R.layout;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;

/**
 * Contains the Android Graphical User Interface
 * 
 * @author Christopher Büttner
 * @author Alvaro Santisteban
 * @version 1
 * 
 */
public class BuscaminasActivity extends Activity { 
	/**
	 * NO SE LO QUE HACE
	 */
	static final private int INT_REQ_GAME_OPTIONS = 0;
	/**
	 * Android's EditText to visualize the results of the actions
	 */
	EditText ourText;
	/**
	 * Number of columns
	 */
	int nrRows;
	/**
	 * Android's TableLayout that contains the array of TableRow "tableRows"
	 */
	TableLayout tl;
	/**
	 * An array of Android's TableRow
	 */
	TableRow tableRows[];
	/**
	 * An array of QuadrantButton
	 */
	QuadrantButton mineField[][];
	/**
	 * The logic of the game
	 */
	GameLogic game;
	/**
	 * Listener of the user's clicks on the buttons
	 */
	OnClickListener fieldListener;
	/**
	 * Listener of the user's long clicks on the buttons which are use to mark the bombs
	 */
	OnLongClickListener longFieldListener;
	
	/**
	 * 
	 * Creates the Android elements for the Buscaminas and respond to the user clicks
	 * 
	 * @param savedInstanceState Bundle object
	 */
    public void onCreate(Bundle savedInstanceState) {
    	nrRows = 7;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
       
        ourText = (EditText)findViewById(R.id.editText1);
        tl = (TableLayout)findViewById(R.id.tableLayout1);
        
        fieldListener = new OnClickListener(){
        	
			public void onClick(View arg0) {
				QuadrantButton b = (QuadrantButton)arg0;
				if( b.quadrant.mineOnQuad ){
					ourText.setText("MINE!");
				}else{
					ourText.setText("NO MINE");
				}
				if (game.explore(b.quadrant)){
					ourText.setText("GAME OVER, LOSER");
				}
				updateMineFieldView();
				if(game.hasWon() && !game.gameOverLost){
					ourText.setText("YOU JUST WON");
				}
			}
        };
        
        longFieldListener = new OnLongClickListener(){
        	//Hay que controlar que no se puedan poner más banderitas que nrMines
        	//Hay que controlar que si, por error se marca un numero, al descubrirlo con el metodo de adyacentes, se descuente uno
			public boolean onLongClick(View arg0) {
				QuadrantButton b = (QuadrantButton)arg0;
				if(b.quadrant.mineOnQuad && b.quadrant.state == ViewState.UNTOUCHED){
					System.out.println("1");
					game.increaseMinesMarked();
					b.quadrant.state = ViewState.MARKED;
				}else if(b.quadrant.mineOnQuad && b.quadrant.state == ViewState.MARKED){//hay mina y habia algo puesto
					System.out.println("2");
					game.decreaseMinesMarked();
					b.quadrant.state = ViewState.UNTOUCHED;
				}else if(!b.quadrant.mineOnQuad && b.quadrant.state == ViewState.UNTOUCHED){//no hay mina y no habia nada puesto
					System.out.println("3");
					game.increaseMarked();
					b.quadrant.state = ViewState.MARKED;
				}else if(!b.quadrant.mineOnQuad && b.quadrant.state == ViewState.MARKED){//no hay mina y habia algo puesto
					System.out.println("4");
					game.decreaseMarked();
					b.quadrant.state = ViewState.UNTOUCHED;
				}else{ //estaba descubierto, Error
					
					System.out.println("5");
					//por ahora nada
				}
				updateMineFieldView();
				if(game.hasWon() && !game.gameOverLost){
					ourText.setText("YOU JUST WON");
				}else{
					ourText.setText("You just marked a possible mine");	
				}
				return true; //We are controlling it
			}
        	
        };
        
        
        create_new_game( nrRows );
    }
    
    /**
     * Creates a new game
     * 
     * @param target corresponding View
     */
    public void newGameClick(View target){
		Intent gameOptionsInt = new Intent(this, de.buscaminas.MineFieldOptionsActivity.class);
		startActivityForResult( gameOptionsInt, INT_REQ_GAME_OPTIONS );
    }
    
    /**
     * Creates the screen where the new game is configured
     * 
     * @param requestCode the requested code
     * @param resultCode the code for the result of the creation of the Intent
     * @param data the Intent itself
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
    	switch (requestCode) {
    		case (INT_REQ_GAME_OPTIONS):
    			if ( resultCode == RESULT_OK ){
    				this.nrRows = data.getExtras().getInt("rows"); 
    				ourText.setText("new game " + String.valueOf(nrRows));
    				create_new_game( nrRows );
    			} else { 
    				System.out.println("no valid game options replied");
    			}
    			break;
    		default:
    			System.out.println("unkonwn request code");
    	}
    }
    
    /**
	 * Creates a new game
	 * 
	 * Creates a new game by initializing all its variables or calling to the corresponding method
	 * 
	 * @param nrRows number of rows for the Buscaminas
	 */
    private void create_new_game( int nrRows ){
        game = new GameLogic(nrRows);
        tableRows = new TableRow[nrRows];
        mineField = new QuadrantButton[nrRows][nrRows];
        new Button(this); // NO LO ENTIENDO
        setupMineFieldButtons();
        game.setNumbers();
        updateMineFieldView();
    }
    
    /**
	 * Creates and setups the buttons of the Buscaminas
	 * 
	 * Removes the previous Views, creates the TableRows and fill them with the QuadrantButtons, which are located in its corresponding array.
	 * Finally, setups a ClickListener to each button.
	 */
    public void setupMineFieldButtons(){
    	tl.removeAllViews();
    	for (int row = 0; row < nrRows; row++ ){ //It's Dynamically setup
        	this.tableRows[row] = new TableRow(this);
        	tl.addView( this.tableRows[row] );
        	for (int col = 0; col < nrRows; col++ ){
        		mineField[row][col] = new QuadrantButton(this, this.game.quads[row][col]);
        		this.tableRows[row].addView(mineField[row][col]);
        		this.mineField[row][col].setOnClickListener(fieldListener);
        		this.mineField[row][col].setOnLongClickListener(longFieldListener);
        	}
        }
    }    
    
    /**
     * 
     * Updates the Quadrant
     * 
	 * Updates the color of the text of each field according to the following possible situations of the quadrant:
	 * Black = Quadrant untouched
	 * Blue = Quadrant discovered (may have a number or nothing on it)
	 * Red = Quadrant with a bomb
	 * Green = Quadrant marked
	 * 
	 *  In previous versions of the Buscaminas, due to debugging, this method printed also the position of the bombs so the user could see them
	 */
    public void updateMineFieldView(){
    	System.out.println("En viewNumbers");
    	for (int row = 0; row < nrRows; row++ ){
        	for (int col = 0; col < nrRows; col++ ){
        		QuadrantButton curr_field = mineField[row][col];
        		
        		// show debug text
        		if (curr_field.quadrant.nrAdjacentMines > 0){
        			curr_field.setText(String.valueOf((curr_field.quadrant.nrAdjacentMines)));
        		} 
        		else if (curr_field.quadrant.mineOnQuad){
        			curr_field.setText("x");
        		} 
        		else {
        			curr_field.setText("o");
        		}
        		
        		// show state (button-color)
        		if (curr_field.quadrant.state == ViewState.UNTOUCHED){
        			curr_field.setTextColor( Color.BLACK );
        		} 
        		else if (curr_field.quadrant.state == ViewState.DISCOVERED){
        			curr_field.setTextColor( Color.BLUE );
        		} 
        		else if (curr_field.quadrant.state == ViewState.BOMBED){
        			curr_field.setTextColor( Color.RED );
        		} 
        		else if (curr_field.quadrant.state == ViewState.MARKED){
        			curr_field.setTextColor( Color.GREEN );
        		}
        	}
        }
    }
}
