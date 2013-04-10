package com.example.flip3d;

import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

public class Flip3dAnimation extends Animation {
	
	private final float mfromDegrees;
	private final float mtoDegrees;
	private final float mcenterX;
	private final float mcenterY;
	private Camera mCamera;

	public Flip3dAnimation(float fromDegrees, float toDegrees, float centerX, float centerY) {
		mfromDegrees = fromDegrees;
		mtoDegrees = toDegrees;
		mcenterX = centerX;
		mcenterY = centerY;
	}

	@Override
	public void initialize(int width, int height, int parentWidth,
			int parentHeight) {
		super.initialize(width, height, parentWidth, parentHeight);
		mCamera = new Camera();
	}
	
	@Override
	public void applyTransformation(float interpolatedTime, Transformation t) {
		super.applyTransformation(interpolatedTime, t);
		final float fromDegrees = mfromDegrees;
		float degrees = fromDegrees + ((mtoDegrees - fromDegrees) * interpolatedTime);
		final float centerX = mcenterX;
		final float centerY = mcenterY;
		final Camera camera = mCamera;
		final Matrix matrix = t.getMatrix();
		camera.save();
		camera.rotateY(degrees);
		camera.getMatrix(matrix);
		camera.restore();
		matrix.preTranslate(-centerX, -centerY);
		matrix.postTranslate(centerX, centerY);
	}

}
