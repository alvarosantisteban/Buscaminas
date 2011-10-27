package de.buscaminas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;

public class MineFieldOptionsActivity extends Activity implements SeekBar.OnSeekBarChangeListener {
	static final int minMines = 1;
	static final int maxMines = 20;

	static final int minRows = 5;
	static final int maxRows = 11;
	
	int rows = 7; 
	int mines = 9;
	
	EditText txtNrRows;
	EditText txtNrMines;
	
	SeekBar seekNrRows;
	SeekBar seekNrMines;
	
	
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
    
    public void sendReplyClick(View target){
    	System.out.println("sending reply " + rows);
    	Intent answer = new Intent();
    	answer.putExtra("rows", rows);
    	answer.putExtra("mines", mines);
    	setResult(RESULT_OK, answer);
    	finish();
    }

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
