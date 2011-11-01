package de.buscaminas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;

/**
 * Creates the screen to set the options for a game
 * 
 * @author Christopher Büttner
 * @author Alvaro Santisteban
 * @version 1
 * 
 */
public class MineFieldOptionsActivity extends Activity implements SeekBar.OnSeekBarChangeListener {
	/**
	 * Minimum number of mines in a game
	 */
	static final int minMines = 1;
	/**
	 * Maximum number of mines in a game
	 */
	static final int maxMines = 20;
	/**
	 * Minimum number of rows in a game
	 */
	static final int minRows = 5;
	/**
	 * Maximum number of rows in a game
	 */
	static final int maxRows = 11;
	/**
	 * Number of rows of the game
	 */
	int rows = 7; 
	/**
	 * Number of mines of the game
	 */
	int mines = 9;
	/**
	 * EditText for the number of rows
	 */
	EditText txtNrRows;
	/**
	 * EditText for the number of mines
	 */
	EditText txtNrMines;
	/**
	 * SeekBar for the number of rows
	 */
	SeekBar seekNrRows;
	/**
	 * SeekBar for the number of mines
	 */
	SeekBar seekNrMines;
	
	/**
	 * Creates the Android's elements for the options' screen
	 * 
	 * @param savedInstanceState Bundle object
	 */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_field_options);
        
        txtNrMines = (EditText)findViewById(R.id.txtNumberOfBombs);
        txtNrRows  = (EditText)findViewById(R.id.txtMineFieldSize);

        seekNrMines = (SeekBar)findViewById(R.id.seekNumberOfBombs);
        seekNrRows  = (SeekBar)findViewById(R.id.seekMineFieldSize);
        
        seekNrMines.setOnSeekBarChangeListener(this);
        seekNrRows.setOnSeekBarChangeListener(this);
        
        seekNrMines.setMax(maxMines - minMines);
        seekNrRows.setMax(maxRows - minRows);

        seekNrMines.setProgress( mines - minMines);
        seekNrRows.setProgress( rows - minRows );
    }
    
    /**
     * 
     * Creates an Intent with the selected number of rows and mines
     * 
     * @param target The corresponding View
     */
    public void sendReplyClick(View target){
    	System.out.println("sending reply " + rows);
    	Intent answer = new Intent();
    	answer.putExtra("rows", rows);
    	answer.putExtra("mines", mines);
    	setResult(RESULT_OK, answer);
    	finish();
    }

    /**
     * Controls the position of the pointer for the given SeekBar
     * 
     * @param seekBar the SeekBar to whom the progress is wanted (rows or mines)
     * @param progress position of the pointer in the seekBar
     * @param fromUser true if the user controls it
     * 
     */
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		System.out.println("got new value" + progress);
		if (seekBar == seekNrMines ){
			
			mines = progress + minMines;
		} 
		else if (seekBar == seekNrRows){
			
			rows = progress + minRows;
			
		}
		
		txtNrRows.setText(String.valueOf(rows));
		txtNrMines.setText(String.valueOf(mines));
		
	}


	public void onStartTrackingTouch(SeekBar seekBar) {
	}

	public void onStopTrackingTouch(SeekBar seekBar) {
	}
    
}
