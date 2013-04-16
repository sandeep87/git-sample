package com.paradigmcreatives.gridcustom;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

public class FullImage extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fullview);
		
		Intent i = getIntent();
		
		int position = i.getExtras().getInt("key");
		ImageAdapter imageAdapter = new ImageAdapter(this, null);
		ImageView imageView = (ImageView) findViewById(R.id.image_view);
		imageView.setImageResource(ImageAdapter.this[position]);
	}

}
