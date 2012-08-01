package com.dragondrop;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;

public class DrawView extends View {
   private Shape[] shapes = new Shape[2]; // array that holds the balls
   private int shapeId = 0; // variable to know what ball is being dragged

   public DrawView(Context context) {
      super(context);
      setFocusable(true); // necessary for getting the touch events

      // setting the start point for the balls
      Point startingPoint = new Point(50, 20);

      Point targetPoint = new Point(200, 300);

      // declare each ball with the ColorBall class
      shapes[0] = new Shape(context, R.drawable.bol_groen, startingPoint, targetPoint);
      shapes[1] = new Shape(context, R.drawable.bol_blauw, targetPoint, targetPoint);
   }

   // the method that draws the balls
   @Override
   protected void onDraw(Canvas canvas) {
      // canvas.drawColor(0xFFCCCCCC); //if you want another background color

      // draw the balls on the canvas
      for (Shape shape : shapes) {
         canvas.drawBitmap(shape.getBitmap(), shape.getX(), shape.getY(), null);
      }

   }

   // events when touching the screen
   public boolean onTouchEvent(MotionEvent event) {
      int eventaction = event.getAction();

      int X = (int) event.getX();
      int Y = (int) event.getY();

      switch (eventaction) {

      case MotionEvent.ACTION_DOWN: // touch down so check if the finger is on a
                                    // ball
         shapeId = 0;
         for (Shape shape : shapes) {
            // check if inside the bounds of the ball (circle)
            // get the center for the ball
            int centerX = shape.getX() + 25;
            int centerY = shape.getY() + 25;

            // calculate the radius from the touch to the center of the ball
            double radCircle = Math.sqrt((double) (((centerX - X) * (centerX - X)) + (centerY - Y)
                  * (centerY - Y)));

            // if the radius is smaller then 23 (radius of a ball is 22), then
            // it must be on the ball
            if (radCircle < 23) {
               shapeId = shape.getID();
               break;
            }

            // check all the bounds of the ball (square)
            // if (X > ball.getX() && X < ball.getX()+50 && Y > ball.getY() && Y
            // < ball.getY()+50){
            // balID = ball.getID();
            // break;
            // }
         }

         break;

      case MotionEvent.ACTION_MOVE: // touch drag with the ball
         // move the balls the same as the finger
         if (shapeId > 0) {
            shapes[shapeId - 1].setX(X - 25);
            shapes[shapeId - 1].setY(Y - 25);

            Shape shape = shapes[shapeId - 1];

            int deltaX = shape.getTargetX() - shape.getX();
            int deltaY = shape.getTargetY() - shape.getY();

            double distance = Math.sqrt((deltaX * deltaX) + (deltaY * deltaY));

            if (distance < 30) {
               shape.setX(shape.getTargetX());
               shape.setY(shape.getTargetY());
            }
         }

         break;

      case MotionEvent.ACTION_UP:
         // touch drop - just do things here after dropping

         break;
      }
      // redraw the canvas
      invalidate();
      return true;

   }
}
