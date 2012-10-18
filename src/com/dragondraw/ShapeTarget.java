package com.dragondraw;

import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;

public class ShapeTarget extends ShapeDrawable {
	private static final int SNAP_DISTANCE = 40;
	private static final Paint TARGET_PAINT = buildTargetPaint();
	private static final Paint FILLED_PAINT = buildFillPaint();
	private boolean filled;

	public ShapeTarget(Shape shape) {
		super(shape);
		filled = false;
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

	@Override
	protected void onDraw(Shape shape, Canvas canvas, Paint paint) {
		if (isFilled()) {
			super.onDraw(shape, canvas, FILLED_PAINT);

		} else {
			super.onDraw(shape, canvas, TARGET_PAINT);
		}
	}

	private static Paint buildTargetPaint() {
		Paint targetPaint = new Paint();

		targetPaint.setStyle(Style.STROKE);
		targetPaint.setStrokeWidth(2);
		targetPaint.setAntiAlias(true);
		targetPaint.setPathEffect(new DashPathEffect(new float[] { 4, 4 }, 0));

		return targetPaint;
	}

	private static Paint buildFillPaint() {
		Paint fillPaint = new Paint();

		fillPaint.setStyle(Style.FILL);
		fillPaint.setAntiAlias(true);

		return fillPaint;
	}
}
