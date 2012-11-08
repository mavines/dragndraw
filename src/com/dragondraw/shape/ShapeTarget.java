package com.dragondraw.shape;


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
		setUnfilledPaint();
	}

	public boolean isFilled() {
		return filled;
	}

	public void fill() {
		this.filled = true;
		setFilledPaint();
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
		unfilledPaint.setStrokeWidth(2);
		unfilledPaint.setAntiAlias(true);
		unfilledPaint.setPathEffect(new DashPathEffect(new float[] { 4, 4 }, 0));

	}

	private void setFilledPaint() {
		Paint filledPaint = this.getPaint();

		filledPaint.setStyle(Style.FILL);
	}
}
