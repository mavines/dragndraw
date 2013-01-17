package com.dragondraw.shape;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;

public class ShapeSpawn extends ShapeDrawable {
	
	public ShapeSpawn(Shape shape) {
		super(shape);
	}

	
	@Override
	public void draw(Canvas canvas) {
		int paintColor = this.getPaint().getColor();
		setOutlinePaint();
		super.draw(canvas);
		setFilledPaint(paintColor);
		super.draw(canvas);
	}


	public MovableShape spawnShape()
	{
		MovableShape movableShape = new MovableShape(this.getShape());
		movableShape.setBounds(this.getBounds());
		movableShape.getPaint().setColor(this.getPaint().getColor());
		
		return movableShape;
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
