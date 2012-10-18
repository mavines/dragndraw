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

public class DrawView extends View {
	private static final String TAG = "DrawView";
	private List<MovableShape> shapes = new ArrayList<MovableShape>();
	private List<ShapeSpawn> spawns = new ArrayList<ShapeSpawn>();
	private List<ShapeTarget> targets = new ArrayList<ShapeTarget>();
	private MovableShape activeShape;

	public DrawView(Context context, int levelId) {
		super(context);
		setFocusable(true); // necessary for getting the touch events

		loadLevel(levelId);

	}

	private void loadLevel(int levelId) {
		Resources resources = getResources();
		TypedArray levelResources = resources.obtainTypedArray(levelId);

		for (int resourceIndex = 0; resourceIndex < levelResources.length(); resourceIndex++) {
			int shapeId = levelResources.getResourceId(resourceIndex, 0);
			TypedArray shapeArray = resources.obtainTypedArray(shapeId);

			ShapeDrawable firstShape = ShapeFactory.createShape(shapeArray);

			if (firstShape instanceof ShapeTarget) {
				targets.add((ShapeTarget) firstShape);
			} else {
				spawns.add((ShapeSpawn) firstShape);
			}
		}

	}

	// the method that draws the balls
	@Override
	protected void onDraw(Canvas canvas) {
		// canvas.drawColor(0xFFCCCCCC); //if you want another background color

		for (ShapeTarget target : targets) {
			target.draw(canvas);
		}

		for (MovableShape shape : shapes) {
			shape.draw(canvas);
		}

		for (ShapeSpawn spawn : spawns) {
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
				for (ShapeSpawn spawn : spawns) {
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

				// See if we have moved close to any targets
				for (ShapeTarget target : targets) {
					// Make sure the target isn't filled
					if (!target.isFilled()) {
						// See if we hit the target and see if we match the
						// shape of the target.
						if (target.hitTarget(activeShape)
								&& target.matchesTarget(activeShape)) {

							// Snap the shape to the target and mark the target
							// as filled.
							activeShape.moveShape(target.getBounds().centerX(),
									target.getBounds().centerY());
							target.fill();
							shapes.remove(activeShape);
							activeShape = null;
							break;
						}
					}

				}
			}
			break;
		case MotionEvent.ACTION_UP:
			shapes.remove(activeShape);
			activeShape = null;
			break;
		}
		// redraw the canvas
		invalidate();
		return true;

	}
}
