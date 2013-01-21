package in.ccl.imageloader;

import in.ccl.ui.R;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.assist.FlushedInputStream;

public class ImageLoader {

	FileCache fileCache;

	private Map <ImageView, String> imageViews = Collections.synchronizedMap(new WeakHashMap <ImageView, String>());

	private static HashMap <String, Bitmap> cache = new HashMap <String, Bitmap>();

	ExecutorService executorService;

	/**
	 * Constructor for Image Loader
	 * 
	 * @param context application context
	 */
	public ImageLoader (Context context) {
		fileCache = new FileCache(context);
		executorService = Executors.newFixedThreadPool(5);
	}

	private volatile static ImageLoader instance;

	/** Returns singleton class instance */
	public static ImageLoader getInstance (Context ctx) {
		if (instance == null) {
			synchronized (ImageLoader.class) {
				if (instance == null) {
					instance = new ImageLoader(ctx);
				}
			}
		}
		return instance;
	}

	final int stub_id = R.raw.pre_loader;

	/**
	 * It displays image.
	 * 
	 * @param url as {@link String}
	 * @param imageView {@link ImageView}
	 */
	public void displayImage (final DisplayImage displayImage, final ImageLoadingListener listener) {
		imageViews.put(displayImage.getImageView(), displayImage.getUrl());
		if (cache.containsKey(displayImage.getUrl())) {
			Bitmap bitmap = cache.get(displayImage.getUrl());
			if (bitmap != null) {
				Drawable background = new BitmapDrawable(displayImage.getActivity().getResources(),bitmap);				 
				displayImage.getImageView().setBackgroundDrawable(background);
				listener.onLoadingComplete(bitmap);
			}
			else {
				displayImage.getImageView().setBackgroundResource(stub_id);

				listener.onLoadingFailed(FailToLoad.IO_ERROR);
			}
		}
		else {
			queuePhoto(displayImage);
			displayImage.getImageView().setBackgroundResource(stub_id);

		}
	}

	/**
	 * It calls PhotoToLoad class, for setting url, and imageView. PhotoLoader will be called, which will dispaly the image with the url, passed as parameter in PhotoToLoad
	 * 
	 * @param url
	 * @param imageView
	 */
	private void queuePhoto (DisplayImage displayImage) {
		PhotoToLoad p = new PhotoToLoad(displayImage);
		executorService.submit(new PhotosLoader(p));
	}

	/**
	 * 
	 * @param url {@link String}
	 * @return {@link Bitmap}
	 */
	private Bitmap getBitmap (String url) {
		if (cache.containsKey(url)) {
			Bitmap bmp = cache.get(url);
			return bmp;
		}// from web
		try {
			URL urls = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) urls.openConnection();
			connection.setDoInput(true);
			// connection.setInstanceFollowRedirects(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			return BitmapFactory.decodeStream(new FlushedInputStream(input));
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * decodes image and scales it to reduce memory consumption
	 * 
	 * @param f {@link File}
	 * @return image, {@link Bitmap}
	 */
	/**
	 * Task for the queue
	 */
	private class PhotoToLoad {

		DisplayImage displayImage;

		public PhotoToLoad (DisplayImage displayImage) {
			this.displayImage = displayImage;
		}
	}

	/**
	 * This class displays images by accessing url form PhotoToLoad class
	 */
	class PhotosLoader implements Runnable {

		PhotoToLoad photoToLoad;

		PhotosLoader (PhotoToLoad photoToLoad) {
			this.photoToLoad = photoToLoad;
		}

		@Override
		public void run () {
			if (imageViewReused(photoToLoad))
				return;
			Bitmap bmp = getBitmap(photoToLoad.displayImage.getUrl().replace(" ", "%20"));
			cache.put(photoToLoad.displayImage.getUrl(), bmp);
			if (imageViewReused(photoToLoad))
				return;
			BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);
			Activity a = (Activity) photoToLoad.displayImage.getImageView().getContext();
			a.runOnUiThread(bd);
		}
	}

	/**
	 * 
	 * @param photoToLoad {@link PhotoToLoad}
	 * @return, It returns true if {@link PhotoToLoad} contains image url({@link String})
	 */
	boolean imageViewReused (PhotoToLoad photoToLoad) {
		String tag = imageViews.get(photoToLoad.displayImage.getImageView());
		if (tag == null || !tag.equals(photoToLoad.displayImage.getUrl()))
			return true;
		return false;
	}

	/**
	 * Used to display bitmap in the UI thread
	 */
	class BitmapDisplayer implements Runnable {

		Bitmap bitmap;

		PhotoToLoad photoToLoad;

		public BitmapDisplayer (Bitmap b, PhotoToLoad p) {
			bitmap = b;
			photoToLoad = p;
		}

		public void run () {
			if (imageViewReused(photoToLoad))
				return;
			if (bitmap != null) {
				Drawable background = new BitmapDrawable(photoToLoad.displayImage.getActivity().getResources(),bitmap);				 

				photoToLoad.displayImage.getImageView().setBackgroundDrawable(background);
			}
			else {
				photoToLoad.displayImage.getImageView().setBackgroundResource(stub_id);

			}
		}
	}

	/**
	 * delete the files, for memory reuse.
	 */
	public void clearCache () {
		fileCache.clear();
	}

	public static Object getInstance () {
		// TODO Auto-generated method stub
		return null;
	}

}
