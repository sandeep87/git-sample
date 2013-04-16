package com.paradigmcreatives.gridviewlayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

public class FullImage extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.full_image);
		
		Intent i = getIntent();
		
		int position = i.getExtras().getInt("id");
		ImageAdapter imageAdapter = new ImageAdapter(this);
		
		ImageView imageView = (ImageView) findViewById(R.id.image_view);
		imageView.setImageResource(imageAdapter.photoIds[position]);
	}

}
