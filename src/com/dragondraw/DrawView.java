package com.dragondraw;

import java.util.ArrayList;
import java.util.List;

import com.dragondraw.shape.MovableShape;
import com.dragondraw.shape.ShapeFactory;
import com.dragondraw.shape.ShapeSpawn;
import com.dragondraw.shape.ShapeTarget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.ShapeDrawable;
import android.view.MotionEvent;
import android.view.View;

public class DrawView extends View {
	private static final String TAG = "DrawView";

	// Holds either the shape spawns or the color spawns based on the phase of
	// the
	// game.
	private List<ShapeSpawn> currentSpawns;
	private List<ShapeSpawn> shapeSpawns = new ArrayList<ShapeSpawn>();
	private List<ShapeSpawn> colorSpawns = new ArrayList<ShapeSpawn>();
	private List<ShapeTarget> unfilledTargets = new ArrayList<ShapeTarget>();
	private List<ShapeTarget> filledTargets = new ArrayList<ShapeTarget>();
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
			String shapeString = shapeArray.getString(1);

			ShapeDrawable createdShape = ShapeFactory.createShape(shapeArray);

			// Put the created shape into the corresponding list.
			if (shapeString.equals("Color")) {
				colorSpawns.add((ShapeSpawn) createdShape);
			} else if (shapeString.equals("Spawn")) {
				shapeSpawns.add((ShapeSpawn) createdShape);
			} else if (shapeString.equals("Target")) {
				unfilledTargets.add((ShapeTarget) createdShape);
			} else {
				assert false : "Shouldn't get here, invalid shape string.";
			}

			// We start with the shapes
			currentSpawns = shapeSpawns;
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// canvas.drawColor(0xFFCCCCCC); //if you want another background color

		for (ShapeTarget unfilledTarget : unfilledTargets) {
			unfilledTarget.draw(canvas);
		}

		for (ShapeTarget filledTarget : filledTargets) {
			filledTarget.draw(canvas);
		}

		for (ShapeSpawn spawn : currentSpawns) {
			spawn.draw(canvas);
		}

		if (activeShape != null) {
			activeShape.draw(canvas);
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
				for (ShapeSpawn spawn : currentSpawns) {
					if (spawn.getBounds().contains(touchedX, touchedY)) {
						activeShape = spawn.spawnShape();
						break;
					}
				}
			}

			break;
		case MotionEvent.ACTION_MOVE:
			if (activeShape != null) {
				activeShape.moveShape(touchedX, touchedY);

				// See if we have moved close to any unfilled targets
				for (ShapeTarget target : unfilledTargets) {
					// See if we hit the target and see if we match the
					// shape of the target.
					if (target.hitTarget(activeShape)
							&& target.matchesTarget(activeShape)) {

						// Snap the shape to the target and mark the target
						// as filled.
						activeShape.moveShape(target.getBounds().centerX(),
								target.getBounds().centerY());
						target.fill();
						filledTargets.add(target);
						unfilledTargets.remove(target);
						activeShape = null;
						
						//if that was the last target, switch to color spawns
						if(unfilledTargets.isEmpty()){
							currentSpawns = colorSpawns;
						}						
						break;
					}
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			// Check to see if the color is close to a target.
			// If it is, paint that target.
			if (activeShape != null) {
				for (ShapeTarget target : filledTargets) {
					if (target.getBounds().contains(touchedX, touchedY)) {
						int color = activeShape.getPaint().getColor();
						target.getPaint().setColor(color);
					}
				}
				activeShape = null;
			}
			break;
		}
		// redraw the canvas
		invalidate();
		return true;

	}
}
