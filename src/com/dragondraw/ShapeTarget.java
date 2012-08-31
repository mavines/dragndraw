package com.dragondraw;

import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;

public class ShapeTarget extends ShapeDrawable{
	private static final int SNAP_DISTANCE = 40;
	private static final Paint TARGET_PAINT = buildPaint();
	
	public ShapeTarget(Shape shape) {
		super(shape);
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
		super.onDraw(shape, canvas, TARGET_PAINT);
	}


	private static Paint buildPaint()
	{
		Paint targetPaint = new Paint();
		
		targetPaint.setStyle(Style.STROKE);
		targetPaint.setStrokeWidth(2);
		targetPaint.setAntiAlias(true);
		targetPaint.setPathEffect(new DashPathEffect(new float[] {4,4}, 0));
		
		return targetPaint;
	}
}
