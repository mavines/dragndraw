package com.dragondraw.shape;

import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;

public class DefaultComparableShape extends ShapeDrawable {

	private String id;

	public DefaultComparableShape(String id) {
		super();
		this.id = id;
	}

	public DefaultComparableShape(String id, Shape shape) {
		super(shape);
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public boolean equals(DefaultComparableShape otherShape)
	{
		return this.getId().equals(otherShape.getId());
	}
}
