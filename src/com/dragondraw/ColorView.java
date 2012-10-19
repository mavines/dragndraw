package com.dragondraw;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.ShapeDrawable;
import android.view.MotionEvent;
import android.view.View;

import com.dragondraw.shape.MovableShape;
import com.dragondraw.shape.ShapeFactory;
import com.dragondraw.shape.ShapeSpawn;
import com.dragondraw.shape.ShapeTarget;

public class ColorView extends View {
	private static final String TAG = "ColorView";
	private List<MovableShape> shapes = new ArrayList<MovableShape>();
	private List<ShapeSpawn> colorSpawns = new ArrayList<ShapeSpawn>();
	private List<ShapeTarget> targets = new ArrayList<ShapeTarget>();
	private MovableShape activeShape;

	public ColorView(Context context, int levelId) {
		super(context);
		setFocusable(true);

		loadLevel(levelId);
	}

	private void loadLevel(int levelId) {
		Resources resources = getResources();
		TypedArray levelResources = resources.obtainTypedArray(levelId);

		for (int resourceIndex = 0; resourceIndex < levelResources.length(); resourceIndex++) {
			int shapeId = levelResources.getResourceId(resourceIndex, 0);
			TypedArray shapeArray = resources.obtainTypedArray(shapeId);
			String shapeString = shapeArray.getString(1);

			// Skip all spawns, we just want targets and colors
			if (shapeString.equals("Spawn")) {
				continue;
			}

			ShapeDrawable createdShape = ShapeFactory.createShape(shapeArray);

			if (createdShape instanceof ShapeTarget) {
				((ShapeTarget) createdShape).fill();
				targets.add((ShapeTarget) createdShape);
			} else if (createdShape instanceof ShapeSpawn) {
				colorSpawns.add((ShapeSpawn) createdShape);
			}
		}

	}

	@Override
	protected void onDraw(Canvas canvas) {
		// canvas.drawColor(0xFFCCCCCC); //if you want another background color

		for (ShapeTarget target : targets) {
			target.draw(canvas);
		}

		for (MovableShape shape : shapes) {
			shape.draw(canvas);
		}

		for (ShapeSpawn spawn : colorSpawns) {
			spawn.draw(canvas);
		}
	}

	// events when touching the screen
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int eventaction = event.getAction();

		int touchedX = (int) event.getX();
		int touchedY = (int) event.getY();

		switch (eventaction) {

		case MotionEvent.ACTION_DOWN:
			// If we aren't dragging a shape, see if we clicking on a spawn.
			// If we are, create a shape.
			if (activeShape == null) {
				for (ShapeSpawn spawn : colorSpawns) {
					if (spawn.getBounds().contains(touchedX, touchedY)) {
						MovableShape newShape = spawn.spawnShape();
						shapes.add(newShape);
						break;
					}
				}
			}

			// See if we have clicked on a shape
			for (MovableShape shape : shapes) {
				if (shape.getBounds().contains(touchedX, touchedY)) {
					activeShape = shape;
					break;
				}
			}

			break;
		case MotionEvent.ACTION_MOVE:
			if (activeShape != null) {
				activeShape.moveShape(touchedX, touchedY);
			}
			break;
		case MotionEvent.ACTION_UP:
			// Check to see if the color is close to a target.
			// If it is, paint that target.
			if (activeShape != null) {
				for (ShapeTarget target : targets) {
					if (target.getBounds().contains(touchedX, touchedY)) {
						int color = activeShape.getPaint().getColor();
						target.getPaint().setColor(color);
					}
				}
				shapes.remove(activeShape);
				activeShape = null;
			}
			break;
		}
		// redraw the canvas
		invalidate();
		return true;

	}
}
