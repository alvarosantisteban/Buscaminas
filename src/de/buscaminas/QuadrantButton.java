package de.buscaminas;

import android.content.Context;
import android.widget.Button;

public class QuadrantButton extends Button {
	Quadrant quadrant;
	public QuadrantButton( Context context, Quadrant quadrant ){
		super(context);
		this.quadrant = quadrant;
	}
}
