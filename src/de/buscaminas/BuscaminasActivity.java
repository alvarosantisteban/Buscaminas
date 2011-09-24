package de.buscaminas;

import de.buscaminas.R.layout;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;

public class BuscaminasActivity extends Activity { //GUI
	Button ourButton;
	EditText ourText;
	int nrRows;
	TableLayout tl;
	TableRow tableRows[];
	Button mineField[][];
	GameLogic game;
	OnClickListener buttonListener;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	nrRows = 7;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ourButton = (Button)findViewById(R.id.testbutton);
        ourText = (EditText)findViewById(R.id.editText1);
        tl = (TableLayout)findViewById(R.id.tableLayout1);
        buttonListener = new OnClickListener(){
        	
			public void onClick(View arg0) {
				Button b = (Button)arg0;
				if(b.getText() == "x"){
					ourText.setText("MINE!");
				}else{
					ourText.setText("NO MINE");
				}
			}
        	
        };
        
        this.game = new GameLogic(nrRows);
        this.tableRows = new TableRow[nrRows];
        this.mineField = new Button[nrRows][nrRows];
        setMines();
        this.game.setNumbers();
        viewNumbers();
        ourButton.setOnClickListener(new OnClickListener(){

			public void onClick(View arg0) {
				ourText.setText("you clicked me baby");
				
			}
        	
        });
    }
    
    public void setMines(){
    	for (int row = 0; row < nrRows; row++ ){ //It's Dynamically setup
        	this.tableRows[row] = new TableRow(this);
        	tl.addView( this.tableRows[row] );
        	for (int col = 0; col < nrRows; col++ ){
        		mineField[row][col] = new Button(this);
        		if (game.quads[row][col].mineOnQuad){
        			mineField[row][col].setText("x");
        		} else { 
        			//mineField[row][col].setText("o");
        		}
        			
        		this.tableRows[row].addView(mineField[row][col]);
        		this.mineField[row][col].setOnClickListener(buttonListener);
        	}
        }
    }    
    
    public void viewNumbers(){
    	System.out.println("En viewNumbers");
    	for (int row = 0; row < nrRows; row++ ){
        	for (int col = 0; col < nrRows; col++ ){
        		if (game.quads[row][col].number == 1){
        			mineField[row][col].setText("1");
        		}else if (game.quads[row][col].number == 2){
        			mineField[row][col].setText("2");
        		}else if (game.quads[row][col].number == 3){
        			mineField[row][col].setText("3");
        		}else if (game.quads[row][col].number == 4){
        			mineField[row][col].setText("4");
        		}else if (game.quads[row][col].number == 5){
        			mineField[row][col].setText("5");
        		}else if (game.quads[row][col].number == 6){
        			mineField[row][col].setText("6");
        		}else if (game.quads[row][col].number == 7){
        			mineField[row][col].setText("7");
        		}else if (game.quads[row][col].number == 8){
        			mineField[row][col].setText("8");
        		}
        	}
        }
    }
}
