package com.dragondraw.shape;

import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.PathShape;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.Shape;

public class ShapeFactory {
	public static ShapeDrawable createShape(TypedArray shapeArray) {
		String shapeTypeString = shapeArray.getString(1);

		Shape shape = getShape(shapeArray);

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

	private static Shape getShape(TypedArray shapeArray) {
		String shapeString = shapeArray.getString(0);

		if (shapeString.equals("Oval")) {
			return new OvalShape();
		} else if (shapeString.equals("Rect")) {
			return new RectShape();
		} else if (shapeString.equals("Path")) {
			return buildPathShape(shapeArray);
		} else {
			assert false : "Invalid shape type: " + shapeString;
			return null;
		}

	}

	private static Shape buildPathShape(TypedArray shapeArray) {
		int left = shapeArray.getInt(2, 0);
		int top = shapeArray.getInt(3, 0);
		int right = shapeArray.getInt(4, 0);
		int bottom = shapeArray.getInt(5, 0);

		int width = right - left;
		int height = bottom - top;

		Path shapePath = new Path();

		int pointIndex = 6;
		while (pointIndex < shapeArray.length()) {
			int x = shapeArray.getInt(pointIndex, 0);
			int y = shapeArray.getInt(pointIndex + 1, 0);
			
			shapePath.lineTo(x, y);
			
			pointIndex = pointIndex + 2;
		}
		shapePath.close();

		return new PathShape(shapePath, width, height);
	}
}
