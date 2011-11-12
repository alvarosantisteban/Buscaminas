package de.buscaminas;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Quadrant with the properties of a Button
 * 
 * @author Christopher Büttner
 * @author Alvaro Santisteban
 * @version 1
 * @see Button
 */
public class QuadrantButton extends Button {
	
	/**
	 * The Quadrant where the Button is placed
	 */
	Quadrant quadrant;
	/**
	 * Constructor for the class
	 * 
	 * @param context the context from the Button
	 * @param quadrant the quadrant where the button is placed
	 *
	 */
	public QuadrantButton( Context context, Quadrant quadrant ){
		super(context);
		this.quadrant = quadrant;
		
	}
	
	public QuadrantButton(Context context, Quadrant quadrant, AttributeSet as){
		super(context, as);
		this.quadrant = quadrant;
	}

}
