package com.dragondraw.shape;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.PathShape;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.Shape;

import com.dragondraw.R;
import com.dragondraw.utils.PixelTranslator;

public class ShapeFactory {
	private PixelTranslator translator;
	private Context context;

	public ShapeFactory(Context context, PixelTranslator translator) {
		super();
		this.context = context;
		this.translator = translator;
	}

	public ShapeDrawable createShape(TypedArray shapeArray) {
		String shapeTypeString = shapeArray.getString(1);
		String shapeId = shapeArray.getString(2);

		Shape shape = getShape(shapeArray);

		ShapeDrawable resultingShape;

		int defaultColor = context.getResources().getColor(R.color.brown);

		if (shapeTypeString.equals("Target")) {
			resultingShape = new ShapeTarget(shapeId, shape);
		} else if (shapeTypeString.equals("Spawn")) {
			resultingShape = new ShapeSpawn(shapeId, shape);
			resultingShape.getPaint().setColor(defaultColor);
		} else if (shapeTypeString.equals("Color")) {
			int color = Color.parseColor(shapeArray.getString(7));
			resultingShape = new ShapeSpawn(shapeId, shape);
			resultingShape.getPaint().setColor(color);
		} else {
			assert false : "Should never get here";
			resultingShape = null;
		}

		int left = shapeArray.getInt(3, 0);
		int top = shapeArray.getInt(4, 0);
		int right = shapeArray.getInt(5, 0);
		int bottom = shapeArray.getInt(6, 0);

		Rect bounds = translator.translateBounds(new Rect(left, top, right,
				bottom));

		resultingShape.setBounds(bounds);

		return resultingShape;
	}

	private Shape getShape(TypedArray shapeArray) {
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

	private Shape buildPathShape(TypedArray shapeArray) {
		int left = shapeArray.getInt(3, 0);
		int top = shapeArray.getInt(4, 0);
		int right = shapeArray.getInt(5, 0);
		int bottom = shapeArray.getInt(6, 0);
		
		int width = right - left;
		int height = bottom - top;
		
		Path shapePath = new Path();
		
		int pointIndex = 7;
		boolean firstPoint = true;
		while (pointIndex < shapeArray.length()) {
			int x = shapeArray.getInt(pointIndex, 0);
			int y = shapeArray.getInt(pointIndex + 1, 0);

			//Move the start of the path to the first point
			if(firstPoint)
			{
				shapePath.moveTo(x, y);
				firstPoint = false;
			}
			else{
				shapePath.lineTo(x, y);
			}

			pointIndex = pointIndex + 2;
		}
		shapePath.close();

		return new PathShape(shapePath, width, height);
	}
}
