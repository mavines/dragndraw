package com.dragondraw;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.Shape;
import android.view.MotionEvent;
import android.view.View;

public class DrawView extends View {
	private List<MovableShape> shapes = new ArrayList<MovableShape>();
	private List<ShapeSpawn> spawns = new ArrayList<ShapeSpawn>();
	private MovableShape activeShape;

	public DrawView(Context context) {
		super(context);
		setFocusable(true); // necessary for getting the touch events

		Shape circle = new OvalShape();

		ShapeSpawn ball = new ShapeSpawn(circle);
		Rect bounds = new Rect(20, 20, 40, 40);
		ball.setBounds(bounds);
		spawns.add(ball);
	}

	// the method that draws the balls
	@Override
	protected void onDraw(Canvas canvas) {
		// canvas.drawColor(0xFFCCCCCC); //if you want another background color

		// draw the balls on the canvas
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
			if (activeShape == null) {
				for (ShapeSpawn spawn : spawns) {
					if (spawn.getBounds().contains(touchedX, touchedY)) {
						MovableShape newShape = spawn.spawnShape();
						shapes.add(newShape);
						break;
					}
				}
			}
			
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
			shapes.remove(activeShape);
			activeShape = null;
			break;
		}
		// redraw the canvas
		invalidate();
		return true;

	}
}
