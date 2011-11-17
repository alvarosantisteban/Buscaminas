package de.buscaminas;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

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
	 * Number of columns
	 */
	int nrRows;
	/**
	 * Number of mines
	 */
	int nrMines;
	/**
	 * Android's TableLayout that contains the array of TableRow "tableRows"
	 */
	TableLayout tl;
	/**
	 * Anroid's TextView to show the number of remaining marks
	 */
	TextView tv;
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
	 * Builder from the alert dialog
	 */
	AlertDialog.Builder builder;
	/**
	 * Alert dialog to control the end of a game and give the opportunity to play again
	 */
	AlertDialog alert;
	
	/**
	 * 
	 * Creates the Android elements for the Buscaminas, respond to the user clicks and shoots the dialog in case the game ends
	 * 
	 * @param savedInstanceState Bundle object
	 */
    public void onCreate(Bundle savedInstanceState) {
    	nrRows = 7;
    	nrMines = (nrRows * nrRows) / 8;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        tl = (TableLayout)findViewById(R.id.tableLayout1);
        tv =(TextView)findViewById(R.id.nrMarked);
        
        // Control of the end dialog
        builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to play again?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				create_new_game(nrRows, nrMines);
				Toast.makeText(getApplicationContext(), "Good luck!", Toast.LENGTH_SHORT).show();
				
			}
		});
        
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				Toast.makeText(getApplicationContext(), "Hope you had fun", Toast.LENGTH_SHORT).show();
				dialog.cancel();
				System.exit(RESULT_OK);
			}
		});
        
        alert = builder.create();
        
        // Control of the user's click
        fieldListener = new OnClickListener(){
        	
			public void onClick(View arg0) {
				QuadrantButton b = (QuadrantButton)arg0;
				if (game.explore(b.quadrant)){
					Toast.makeText(getApplicationContext(), "GAME OVER, LOSER", Toast.LENGTH_SHORT).show();
					alert.show();
				}
				updateMineFieldView();
				if(game.hasWon() && !game.gameOverLost){
					Toast.makeText(getApplicationContext(), "WINNING!", Toast.LENGTH_SHORT).show();
					alert.show();
				}
				tv.setText("Number of remaining marks: " +game.getRemainingMarks());
			}
        };
        
        //Control of the user's long click
        longFieldListener = new OnLongClickListener(){
			public boolean onLongClick(View arg0) {
				
				QuadrantButton b = (QuadrantButton)arg0;
				
				if(b.quadrant.mineOnQuad && b.quadrant.state == ViewState.UNTOUCHED && game.nrMarked < game.nrMines){//mine and nothing on it
					System.out.println("Mine marked");
					game.increaseMinesMarked();
					game.increaseMarked();
					b.quadrant.state = ViewState.MARKED;
				}else if(!b.quadrant.mineOnQuad && b.quadrant.state == ViewState.UNTOUCHED && game.nrMarked < game.nrMines){//no mine and nothing on it
					System.out.println("Number marked");
					game.increaseMarked();
					b.quadrant.state = ViewState.MARKED;
				}else if(b.quadrant.mineOnQuad && b.quadrant.state == ViewState.MARKED){//mine and something on it
					System.out.println("Mine unmarked");
					game.decreaseMinesMarked();
					game.decreaseMarked();
					b.quadrant.state = ViewState.UNTOUCHED;
				}else if(!b.quadrant.mineOnQuad && b.quadrant.state == ViewState.MARKED){//no mine and somthing on it
					System.out.println("Number unmarked");
					game.decreaseMarked();
					b.quadrant.state = ViewState.UNTOUCHED;
				}
				updateMineFieldView();
				if(game.hasWon() && !game.gameOverLost){
					Toast.makeText(getApplicationContext(), "WINNING!", Toast.LENGTH_SHORT).show();
					alert.show();
				}
				tv.setText("Number of remaining marks: " +game.getRemainingMarks());
				return true; //We are controlling the long click
			}
        	
        };
        create_new_game( nrRows, nrMines );
    }
    
    /**
     * Creates the intent to create a new game in another screen
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
    				this.nrMines = data.getExtras().getInt("mines"); 
    				create_new_game( nrRows, nrMines );
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
    private void create_new_game( int nrRows, int nrMines ){
        game = new GameLogic(nrRows, nrMines);
        tableRows = new TableRow[nrRows];
        mineField = new QuadrantButton[nrRows][nrRows];
        setupMineFieldButtons();
        game.setNumbers();
        updateMineFieldView();
        tv.setText("Number of remaining marks: " +game.getRemainingMarks());
    }
    
    /**
	 * Creates and setups the buttons of the Buscaminas
	 * 
	 * Removes the previous Views, creates the TableRows and fill them with the QuadrantButtons, which are located in its corresponding array.
	 * Finally, setups a ClickListener to each button.
	 */
    public void setupMineFieldButtons(){
    	tl.removeAllViews();
    	for (int row = 0; row < nrRows; row++ ){ 
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
     * Updates the all the Quadrants
     * 
	 * Updates the situation and color of each field according to the following possible situations of the quadrant:
	 * Black = Quadrant untouched
	 * Blue = Quadrant discovered (may have a number or nothing on it)
	 * Red = Quadrant with a bomb
	 * Green = Quadrant marked
	 * 
	 *  In previous versions of the Buscaminas, due to debugging, this method printed also the position of the bombs so the user could see them
	 */
    public void updateMineFieldView(){
    	for (int row = 0; row < nrRows; row++ ){
        	for (int col = 0; col < nrRows; col++ ){
        		QuadrantButton curr_field = mineField[row][col];
        		
        		if(curr_field.quadrant.state == ViewState.DISCOVERED || curr_field.quadrant.state == ViewState.BOMBED){
	        		if (curr_field.quadrant.nrAdjacentMines > 0){
	        			curr_field.setText(String.valueOf((curr_field.quadrant.nrAdjacentMines)));
	        		} 
	        		else if (curr_field.quadrant.mineOnQuad){
	        			curr_field.setText("x");
	        		} 
	        		else {
	        			curr_field.setText("o");
	        		}
        		}else if (curr_field.quadrant.state == ViewState.MARKED){
        			curr_field.setText("M");
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
