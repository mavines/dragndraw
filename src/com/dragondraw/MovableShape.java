package com.dragondraw;

import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;

public class MovableShape extends ShapeDrawable{
	private boolean goRight = true;
	private boolean goDown = true;

	
	public MovableShape() {
		super();
	}

	public MovableShape(Shape s) {
		super(s);
	}

	public void moveShape(int newX, int newY) {
		Rect bounds = getBounds();
		int centerOffsetX = bounds.width() / 2;
		int centerOffsetY = bounds.height() / 2;
		
		bounds.offsetTo(newX - centerOffsetX, newY - centerOffsetY);
		
		super.setBounds(bounds);
	}
}
