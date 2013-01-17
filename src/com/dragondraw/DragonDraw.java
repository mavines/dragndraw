package com.dragondraw;

import android.app.Activity;
import android.os.Bundle;

import com.dragondraw.views.DrawView;

public class DragonDraw extends Activity {
	public static final String PICTURE_KEY = "PictureId";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            long pictureKey = extras.getLong(PICTURE_KEY);
            // draw the view
//            setContentView(R.layout.activity_dragon_draw);
            setContentView(new DrawView(this, (int)pictureKey));
        }
    }
    
}
