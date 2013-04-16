package com.paradigmcreatives.drawimage;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

public class DramImage extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dram_image);

		DrawCanvas mycanvas = (DrawCanvas) findViewById(R.id.surface_view);
		mycanvas.setOnClickListener(new OnClickListener() {
			private boolean isDrawn = false;

			@Override
			public void onClick(View v) {
				if (!isDrawn) {
					DrawCanvas mycanvas = (DrawCanvas) findViewById(R.id.surface_view);
					mycanvas.startDrawImage();
					isDrawn = true;
				}

			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_dram_image, menu);
		return true;
	}

}
