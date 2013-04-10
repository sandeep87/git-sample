package com.paradigmcreatives.toastimage;

import java.io.InputStream;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

public class ToastImageActivity extends Activity {
    /** Called when the activity is first created. */
	private ImageView image;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ImageView image = (ImageView)findViewById(R.id.image_view);
        InputStream iFile = getResources().openRawResource(R.raw.image1);
        Drawable draw = Drawable.createFromStream(iFile, null);
        image.setImageDrawable(draw);
        Toast.makeText(getApplicationContext(), getResources().getString(R.string.Toast_message), Toast.LENGTH_LONG).show();
    }
}