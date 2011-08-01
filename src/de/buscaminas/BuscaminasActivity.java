package de.buscaminas;

import android.app.Activity;
import android.os.Bundle;

public class BuscaminasActivity extends Activity {
	Button ourButton;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
}