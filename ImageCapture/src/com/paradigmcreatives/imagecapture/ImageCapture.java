package com.paradigmcreatives.imagecapture;


import android.app.Activity;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class ImageCapture extends Activity {
	
	private Camera camera;
	private int cameraId = 0;
	final static String DEBUG_TAG = "make photo activity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
			
			Toast.makeText(ImageCapture.this, "No camera on this device", Toast.LENGTH_LONG).show();
			
		} else {
			cameraId = findFrontFacingCamera();
			
			if (cameraId < 0) {
				Toast.makeText(ImageCapture.this, "No front faccing camera found on this device", Toast.LENGTH_LONG).show();
			} else {
				camera = Camera.open(cameraId);
			}
			
		}
	}
	
	
	

	public void onClick(View view) {
		camera.takePicture(null, null, new PhotoHandler(getApplicationContext()));
	}
	private int findFrontFacingCamera() {
		int cameraId = -1;
		int numberOfCameras = Camera.getNumberOfCameras();
		for (int i = 0; i < numberOfCameras; i ++) {
			CameraInfo info = new CameraInfo();
			Camera.getCameraInfo(i, info);
			if (info.facing == CameraInfo.CAMERA_FACING_FRONT) {
				Log.d(DEBUG_TAG, "Camera Found");
				cameraId = i;
				break;
			}
		}
		return cameraId;
	}
	@Override
	protected void onPause() {
		if (camera != null) {
			camera.release();
			camera = null;
		}
		super.onPause();
	}
}
