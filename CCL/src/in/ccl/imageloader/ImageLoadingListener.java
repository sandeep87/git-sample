package in.ccl.imageloader;

import android.graphics.Bitmap;

public interface ImageLoadingListener {

	/** While start to load image this will call. */
	void onLoadingStarted ();

	/** When image loading is completed this will call. */
	void onLoadingComplete (Bitmap loadedImage);

	/**
	 * If any problem occure while loading image this will call.
	 * */
	void onLoadingFailed (FailToLoad failToLoad);

}
