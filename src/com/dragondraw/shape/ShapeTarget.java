package com.dragondraw.shape;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;

public class ShapeTarget extends ShapeDrawable {
	private static final int SNAP_DISTANCE = 40;
	private boolean filled;

	public ShapeTarget(Shape shape) {
		super(shape);
		filled = false;
	}

	@Override
	public void draw(Canvas canvas) {
		if(filled)
		{
			int paintColor = this.getPaint().getColor();
			setOutlinePaint();
			super.draw(canvas);
			setFilledPaint(paintColor);
			super.draw(canvas);
		}
		else{
			setUnfilledPaint();
			super.draw(canvas);
		}
	}

	public boolean isFilled() {
		return filled;
	}

	public void fill() {
		this.filled = true;
	}

	public boolean hitTarget(MovableShape shape) {
		Rect shapeBounds = shape.getBounds();
		Rect targetBounds = this.getBounds();

		double distance = Math.sqrt(Math.pow(shapeBounds.left
				- targetBounds.left, 2)
				+ Math.pow(shapeBounds.top - targetBounds.top, 2));

		return distance <= SNAP_DISTANCE;
	}

	public boolean matchesTarget(MovableShape shape) {
		Rect shapeBounds = shape.getBounds();
		Rect targetBounds = this.getBounds();

		return shapeBounds.height() == targetBounds.height()
				&& shapeBounds.width() == targetBounds.width()
				&& shape.getShape().getClass() == this.getShape().getClass();
	}

	private void setUnfilledPaint() {
		Paint unfilledPaint = this.getPaint();

		unfilledPaint.setStyle(Style.STROKE);
		unfilledPaint.setStrokeWidth(3);
		unfilledPaint.setAntiAlias(true);
		unfilledPaint
				.setPathEffect(new DashPathEffect(new float[] { 4, 4 }, 0));

	}

	private void setOutlinePaint() {
		Paint outlinePaint = this.getPaint();

		outlinePaint.setStyle(Style.STROKE);
		outlinePaint.setStrokeWidth(3);
		outlinePaint.setColor(Color.BLACK);
		outlinePaint.setAntiAlias(true);
		outlinePaint.setPathEffect(null);
	}

	private void setFilledPaint(int color) {
		Paint filledPaint = this.getPaint();

		filledPaint.setStyle(Style.FILL);
		filledPaint.setAntiAlias(true);
		filledPaint.setColor(color);
	}

}
