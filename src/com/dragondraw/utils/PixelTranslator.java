package com.dragondraw.utils;

import android.graphics.Rect;

public class PixelTranslator {
	private static final int DRAWING_WIDTH = 400;
	private static final int DRAWING_HEIGHT = 700;
	private double widthScaleFactor;
	private double heightScaleFactor;
	
	public PixelTranslator(int displayWidth, int displayHeight) {
		super();
		this.widthScaleFactor = displayWidth / (double)DRAWING_WIDTH;
		this.heightScaleFactor = displayHeight / (double)DRAWING_HEIGHT;
		
	}

	public Rect translateBounds(Rect originalRect)
	{
		//Due to truncation we have to calculate the delta instead of
		//simply multiplying the right and bottom.
		int newLeft = (int)(this.widthScaleFactor * originalRect.left);
		int originalwidth = originalRect.right - originalRect.left;
		int newWidth = (int)(this.widthScaleFactor * originalwidth);
		int newRight = newLeft + newWidth;

		int newTop = (int)(this.heightScaleFactor * originalRect.top); 
		int originalHeight = originalRect.bottom - originalRect.top;
		int newHeight = (int)(this.heightScaleFactor * originalHeight);
		int newBottom = newTop + newHeight;
		
		return new Rect(newLeft, newTop, newRight, newBottom);
	}

}
