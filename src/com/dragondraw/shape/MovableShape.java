package com.dragondraw.shape;

import com.dragondraw.R;

import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;

public class MovableShape extends ShapeDrawable{
	
	public MovableShape() {
		super();
	}

	public MovableShape(Shape shape) {
		super(shape);
	}

	public void moveShape(int newX, int newY) {
		Rect bounds = getBounds();
		int centerOffsetX = bounds.width() / 2;
		int centerOffsetY = bounds.height() / 2;
		
		bounds.offsetTo(newX - centerOffsetX, newY - centerOffsetY);
		
		super.setBounds(bounds);
	}
	
	
}
