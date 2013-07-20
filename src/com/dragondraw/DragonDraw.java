package com.dragondraw;

import android.app.Activity;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DragonDraw extends Activity {
	public static final String PICTURE_KEY = "PictureId";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int levelId = (int)extras.getLong(PICTURE_KEY);
            // draw the view
            setContentView(levelId);

            //Setup the button listener           
            final Button button = (Button) findViewById(R.id.back_button);
            button.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                	onBackPressed();
                }
            });
        }
    }
    
}
