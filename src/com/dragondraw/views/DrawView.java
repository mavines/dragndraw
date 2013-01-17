package com.dragondraw.views;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.ShapeDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.view.MotionEvent;
import android.view.View;

import com.dragondraw.R;
import com.dragondraw.shape.MovableShape;
import com.dragondraw.shape.ShapeFactory;
import com.dragondraw.shape.ShapeSpawn;
import com.dragondraw.shape.ShapeTarget;
import com.dragondraw.utils.PixelTranslator;

public class DrawView extends View {
	private static final String TAG = "DrawView";

	// Holds either the shape spawns or the color spawns based on the phase of
	// the game.
	private List<ShapeSpawn> currentSpawns;
	private List<ShapeSpawn> shapeSpawns = new ArrayList<ShapeSpawn>();
	private List<ShapeSpawn> colorSpawns = new ArrayList<ShapeSpawn>();
	private List<ShapeTarget> unfilledTargets = new ArrayList<ShapeTarget>();
	private List<ShapeTarget> filledTargets = new ArrayList<ShapeTarget>();
	private MovableShape activeShape;
	private SoundPool sounds;
	private int pieceFitsSound;
	private int pickupPieceSound;
	private int paintPieceSound;
	private ShapeFactory shapeFactory;
	private int height;
	private int width;
	private int levelId;

	public DrawView(Context context, int levelId) {
		super(context);
		setFocusable(true); // necessary for getting the touch events
		sounds = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		pieceFitsSound = sounds.load(context, R.raw.piece_fits, 1);
		pickupPieceSound = sounds.load(context, R.raw.pickup_piece, 1);
		paintPieceSound = sounds.load(context, R.raw.paint_piece, 1);
		this.levelId = levelId;
		setBackgroundResource(R.drawable.train_background);
	}

	private void loadLevel(int levelId) {
		Resources resources = getResources();
		TypedArray levelResources = resources.obtainTypedArray(levelId);

		for (int resourceIndex = 0; resourceIndex < levelResources.length(); resourceIndex++) {
			int shapeId = levelResources.getResourceId(resourceIndex, 0);
			TypedArray shapeArray = resources.obtainTypedArray(shapeId);
			String shapeString = shapeArray.getString(1);

			ShapeDrawable createdShape = shapeFactory.createShape(shapeArray);

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

		if (!unfilledTargets.isEmpty()) {
			for (ShapeSpawn spawn : currentSpawns) {
				spawn.draw(canvas);
			}
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

			if (activeShape == null) {
				activeShape = spawnShapeIfTouched(touchedX, touchedY);
			}

			break;
		case MotionEvent.ACTION_MOVE:
			if (activeShape != null) {
				activeShape.moveShape(touchedX, touchedY);

				snapIfNearTarget();
			}
			break;
		case MotionEvent.ACTION_UP:

			if (activeShape != null) {
				paintTargetIfClose(touchedX, touchedY);
				//clear the active shape
				activeShape = null;
			}
			break;
		}
		// redraw the canvas
		invalidate();
		return true;

	}

	private void paintTargetIfClose(int touchedX, int touchedY) {
		// Check to see if the color is close to a target.
		// If it is, paint that target.
		if (unfilledTargets.isEmpty()) {
			for (ShapeTarget target : filledTargets) {
				if (target.getBounds().contains(touchedX, touchedY)) {
					int color = activeShape.getPaint().getColor();
					target.getPaint().setColor(color);
					sounds.play(paintPieceSound, 2.0f, 2.0f, 0, 0, 1.5f);
					break;
				}
			}
		}
	}

	private void snapIfNearTarget() {
		// See if we have moved close to any unfilled targets
		for (ShapeTarget target : unfilledTargets) {
			// See if we hit the target and see if we match the
			// shape of the target.
			if (target.hitTarget(activeShape)
					&& target.matchesTarget(activeShape)) {

				// Snap the shape to the target and mark the target
				// as filled and play a sound.
				activeShape.moveShape(target.getBounds().centerX(), target
						.getBounds().centerY());
				target.fill();
				int color = activeShape.getPaint().getColor();
				target.getPaint().setColor(color);
				sounds.play(pieceFitsSound, 1.0f, 10.f, 0, 0, 1.5f);
				filledTargets.add(target);
				unfilledTargets.remove(target);
				activeShape = null;

				// if that was the last target, switch to color spawns
				if (unfilledTargets.isEmpty()) {
					currentSpawns = colorSpawns;
					setBackgroundResource(R.drawable.train_background_paint);
				}
				break;
			}
		}
	}

	private MovableShape spawnShapeIfTouched(int touchedX, int touchedY) {
		// If we aren't dragging a shape, see if we clicking on a spawn.
		// If we are, create a shape.
		for (ShapeSpawn spawn : currentSpawns) {
			if (spawn.getBounds().contains(touchedX, touchedY)) {
				sounds.play(pickupPieceSound, 1.0f, 1.0f, 0, 0, 1.5f);
				return spawn.spawnShape();
			}
		}

		return null;
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		if (this.width == 0 && this.height == 0) {
			this.width = w;
			this.height = h;
			PixelTranslator translator = new PixelTranslator(width, height);

			shapeFactory = new ShapeFactory(super.getContext(), translator);
			loadLevel(levelId);
		}
	}

}
