package com.dragondraw.shape;


import android.graphics.Rect;
import android.graphics.drawable.shapes.Shape;

public class MovableShape extends DefaultComparableShape{
	public MovableShape(String id) {
		super(id);
	}

	public MovableShape(String id, Shape shape) {
		super(id, shape);
	}
	
	public void moveShape(int newX, int newY) {
		Rect bounds = getBounds();
		int centerOffsetX = bounds.width() / 2;
		int centerOffsetY = bounds.height() / 2;
		
		bounds.offsetTo(newX - centerOffsetX, newY - centerOffsetY);
		
		super.setBounds(bounds);
	}	
}
