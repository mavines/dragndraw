package com.dragondraw.shape;

import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.Shape;

public class ShapeFactory {
	public static ShapeDrawable createShape(TypedArray shapeArray) {
		String shapeString = shapeArray.getString(0);
		String shapeTypeString = shapeArray.getString(1);

		Shape shape = getShape(shapeString);

		int left = shapeArray.getInt(2, 0);
		int top = shapeArray.getInt(3, 0);
		int right = shapeArray.getInt(4, 0);
		int bottom = shapeArray.getInt(5, 0);

		Rect bounds = new Rect(left, top, right, bottom);

		ShapeDrawable resultingShape;

		if (shapeTypeString.equals("Target")) {
			resultingShape = new ShapeTarget(shape);
		} else if (shapeTypeString.equals("Spawn")) {
			resultingShape = new ShapeSpawn(shape);
		} else if (shapeTypeString.equals("Color")) {
			int color = Color.parseColor(shapeArray.getString(6));
			resultingShape = new ShapeSpawn(shape);
			resultingShape.getPaint().setColor(color);
		} else {
			assert false : "Should never get here";
			resultingShape = null;
		}

		resultingShape.setBounds(bounds);

		return resultingShape;
	}

	private static Shape getShape(String shapeString) {
		if (shapeString.equals("Oval")) {
			return new OvalShape();
		} else {
			return new RectShape();
		}
	}
}
