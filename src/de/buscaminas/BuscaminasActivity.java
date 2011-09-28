package de.buscaminas;

import de.buscaminas.R.layout;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;


public class BuscaminasActivity extends Activity { //GUI
	static final private int INT_REQ_GAME_OPTIONS = 0;
	
	EditText ourText;
	int nrRows;
	TableLayout tl;
	TableRow tableRows[];
	QuadrantButton mineField[][];
	GameLogic game;
	OnClickListener fieldListener;
	
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
				game.explore(b.quadrant);
				updateMineFieldView();
			}
        };
        
        create_new_game( nrRows );
    }
    
    public void newGameClick(View target){
		Intent gameOptionsInt = new Intent(this, de.buscaminas.MineFieldOptionsActivity.class);
		startActivityForResult( gameOptionsInt, INT_REQ_GAME_OPTIONS );
		// ourText.setText("new game " + String.valueOf(nrRows));
		// create_new_game( nrRows );
    	
    }
    
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
    
    private void create_new_game( int nrRows ){
        game = new GameLogic(nrRows);
        tableRows = new TableRow[nrRows];
        mineField = new QuadrantButton[nrRows][nrRows];
        new Button(this);
        setupMineFileButtons();
        game.setNumbers();
        updateMineFieldView();
    }
    
    public void setupMineFileButtons(){
    	tl.removeAllViews();
    	for (int row = 0; row < nrRows; row++ ){ //It's Dynamically setup
        	this.tableRows[row] = new TableRow(this);
        	tl.addView( this.tableRows[row] );
        	for (int col = 0; col < nrRows; col++ ){
        		mineField[row][col] = new QuadrantButton(this, this.game.quads[row][col]);
        		this.tableRows[row].addView(mineField[row][col]);
        		this.mineField[row][col].setOnClickListener(fieldListener);
        	}
        }
    }    
    
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
