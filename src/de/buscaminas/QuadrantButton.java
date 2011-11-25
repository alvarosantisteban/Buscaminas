package de.buscaminas;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Quadrant with the properties of a Button
 
 * @author Alvaro Santisteban
 * @author Christopher Büttner
 * @version 1.5
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
		setLook(context);
	}
	
	/**
	 * Constructor for the class that also receives a set of attributes. Currently not used.
	 * 
	 * @param context the context from the Button
	 * @param quadrant the quadrant where the button is placed
	 * @param as set of attributes
	 *
	 */
	public QuadrantButton(Context context, Quadrant quadrant, AttributeSet as){
		super(context, as);
		this.quadrant = quadrant;
	}

	/**
	 * Sets the initial look of the QuadrantButtons of the Buscaminas when they are not discovered
	 * 
	 * @param context context from the game
	 */
	public void setLook(Context context){
		Resources res = context.getResources();
		Drawable myDrawableUndiscovered = res.getDrawable(R.drawable.undiscovered);
		this.setBackgroundDrawable(myDrawableUndiscovered);
	}
	
	/**
	 * Sets the look of the QuadrantButtons of the Buscaminas after they have been discovered
	 * 
	 * @param context context from the game
	 */
	public void setDiscoveredLook(Context context){
		Resources res = context.getResources();
		Drawable myDrawableDiscovered = res.getDrawable(R.drawable.discovered);
		this.setBackgroundDrawable(myDrawableDiscovered);
	}
}
