package com.dragondrop;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

public class Shape  {
 private Bitmap img; // the image of the shape
 private int coordX = 0; // the x coordinate at the canvas
 private int coordY = 0; // the y coordinate at the canvas
 private int targetX = 0; //The target X point of the shape
 private int targetY = 0; //The target Y point of the shape
 private int startX = 0;
 private int startY = 0;
 private int id; // gives every shape his own id, for now not necessary
 private static int count = 1;
 private boolean goRight = true;
 private boolean goDown = true;
 
	public Shape(Context context, int drawable) {

		BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        img = BitmapFactory.decodeResource(context.getResources(), drawable); 
        id=count;
		count++;

	}
	
	public Shape(Context context, int drawable, Point startingPoint, Point targetPoint) {

		BitmapFactory.Options opts = new BitmapFactory.Options();
      opts.inJustDecodeBounds = true;
      img = BitmapFactory.decodeResource(context.getResources(), drawable); 
      id=count;
		count++;
		startX = startingPoint.x;
		startY = startingPoint.y;
		coordX = startX;
		coordY = startY;
		
		targetX = targetPoint.x;
		targetY = targetPoint.y;

	}
	
	public static int getCount() {
		return count;
	}
	
	void setX(int newValue) {
        coordX = newValue;
    }
	
	public int getX() {
		return coordX;
	}

	void setY(int newValue) {
        coordY = newValue;
   }
	
	public int getY() {
		return coordY;
	}
			
	public int getTargetX() {
      return targetX;
   }

   public void setTargetX(int targetX) {
      this.targetX = targetX;
   }

   public int getTargetY() {
      return targetY;
   }

   public void setTargetY(int targetY) {
      this.targetY = targetY;
   }

   public int getID() {
		return id;
	}
	
	public Bitmap getBitmap() {
		return img;
	}
	
	public void moveShape(int goX, int goY) {
		// check the borders, and set the direction if a border has reached
		if (coordX > 270){
			goRight = false;
		}
		if (coordX < 0){
			goRight = true;
		}
		if (coordY > 400){
			goDown = false;
		}
		if (coordY < 0){
			goDown = true;
		}
		// move the x and y 
		if (goRight){
			coordX += goX;
		}else
		{
			coordX -= goX;
		}
		if (goDown){
			coordY += goY;
		}else
		{
			coordY -= goY;
		}
		
	}
	
}
