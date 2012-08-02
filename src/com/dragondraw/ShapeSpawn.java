package com.dragondraw;

import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;

public class ShapeSpawn extends ShapeDrawable {
	
	public ShapeSpawn(Shape shape) {
		super(shape);
	}

	
	public MovableShape spawnShape()
	{
		MovableShape movableShape = new MovableShape(this.getShape());
		movableShape.setBounds(this.getBounds());
		
		return movableShape;
	}
}
