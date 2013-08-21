package com.dragondraw;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

public class MainMenu extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_menu);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

		GridView gridView = (GridView) findViewById(R.id.gridview);
		gridView.setAdapter(new HomeScreenShortcutAdapter(this));
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				Intent intent = new Intent(MainMenu.this, DragonDraw.class);
				intent.putExtra(DragonDraw.PICTURE_KEY, id);
				startActivity(intent);
			}
		});
		
		 //Setup the button listener           
        final Button button = (Button) findViewById(R.id.donebutton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            	finish();
            }
        });

	}

	public class HomeScreenShortcutAdapter extends BaseAdapter {

		private Context mContext;

		HomeScreenShortcutAdapter(Context c) {
			mContext = c;
		}

		@Override
		public int getCount() {
			Resources resources = getResources();
			return resources.obtainTypedArray(R.array.pictures).length();
		}

		@Override
		public Object getItem(int position) {
			return mThumbIds[position];
		}

		@Override
		public long getItemId(int position) {
			Resources resources = getResources();
			return resources.obtainTypedArray(R.array.pictures).getResourceId(
					position, 0);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView imageView;
			if (convertView == null) { // if it's not recycled, initialize some
										// attributes
				imageView = new ImageView(mContext);
				int height = getResources().getDimensionPixelSize(
						R.dimen.icon_height);
				int width = getResources().getDimensionPixelSize(
						R.dimen.icon_width);
				imageView.setLayoutParams(new GridView.LayoutParams(width,
						height));
				imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
				imageView.setAdjustViewBounds(true);
			} else {
				imageView = (ImageView) convertView;
			}

			imageView.setImageResource(mThumbIds[position]);
			return imageView;
		}

		// references to our images
		private Integer[] mThumbIds = { R.drawable.train_icon,
				R.drawable.truck_icon, R.drawable.ufo_icon,
				R.drawable.castle_icon };
	}
}
