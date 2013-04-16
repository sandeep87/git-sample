package com.example.cameraactivity;

import java.io.ByteArrayOutputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;

public class CameraActivity extends Activity implements OnClickListener,
		PictureCallback {

	private static final int CAMERA_PIC_REQUEST = 1;
	CameraSurfaceView cameraSurfaceView;
	Button shutterButton;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_camera);

		// set up our preview surface
		FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
		cameraSurfaceView = new CameraSurfaceView(this);
		preview.addView(cameraSurfaceView);

		// grab out shutter button so we can reference it later
		shutterButton = (Button) findViewById(R.id.shutter_button);
		shutterButton.setOnClickListener(this);

		Intent intent = new Intent(
				android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT,
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI.getPath());
		startActivityForResult(intent, CAMERA_PIC_REQUEST);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		Bitmap bm = (Bitmap) data.getExtras().get("data");
		MediaStore.Images.Media.insertImage(getContentResolver(), bm, null,
				null);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); // bm is the bitmap
															// object
		byte[] b = baos.toByteArray();
	}

	@Override
	public void onClick(View v) {
		takePicture();
	}

	private void takePicture() {
		shutterButton.setEnabled(false);
		cameraSurfaceView.takePicture(this);
	}

	@Override
	public void onPictureTaken(byte[] data, Camera camera) {
		// TODO something with the image data

		// Restart the preview and re-enable the shutter button so that we can
		// take another picture
		camera.startPreview();
		shutterButton.setEnabled(true);
	}
}
