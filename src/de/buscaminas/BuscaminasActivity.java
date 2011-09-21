package de.buscaminas;

import de.buscaminas.R.layout;
import android.app.Activity;
import android.content.Context;
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
	TableLayout tl;
	TableRow tableRows[];
	Button mineField[][];
	Game game;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	int nrRows = 7;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ourButton = (Button)findViewById(R.id.testbutton);
        ourText = (EditText)findViewById(R.id.editText1);
        tl = (TableLayout)findViewById(R.id.tableLayout1);
        
        this.game = new Game(nrRows);
        this.tableRows = new TableRow[nrRows];
        this.mineField = new Button[nrRows][nrRows];
        
        for (int row = 0; row < nrRows; row++ ){ //It's dinamically setup
        	this.tableRows[row] = new TableRow(this);
        	tl.addView(this.tableRows[row]);
        	for (int col = 0; col < nrRows; col++ ){
        		mineField[row][col] = new Button(this);
        		if (game.quads[row][col].mineOnQuad){
        			mineField[row][col].setText("x");
        		} else { 
        			mineField[row][col].setText("o");
        		}
        			
        		this.tableRows[row].addView(mineField[row][col]);
        	}
        }
        
        ourButton.setOnClickListener(new OnClickListener(){

			public void onClick(View arg0) {
				ourText.setText("you clicked me baby");
				
			}
        	
        });
    }
    
}