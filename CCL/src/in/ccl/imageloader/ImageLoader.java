package in.ccl.imageloader;

import in.ccl.logging.Logger;
import in.ccl.ui.R;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.util.ByteArrayBuffer;

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

	private String TAG = "Image Loader'";

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
				Drawable background = new BitmapDrawable(displayImage.getActivity().getResources(), bitmap);
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
			connection.connect();
			InputStream input = connection.getInputStream();
			// return BitmapFactory.decodeStream(input);
			return decodeFile(new FlushedInputStream(input));
		}
		catch (Exception ex) {
			Logger.info(TAG, ex.toString());
			return null;
		}
	}

	private Bitmap decodeFile (InputStream is) {
		final BitmapFactory.Options options = new BitmapFactory.Options();

		BufferedInputStream bis = new BufferedInputStream(is, 4 * 1024);
		ByteArrayBuffer baf = new ByteArrayBuffer(50);
		int current = 0;
		try {
			while ((current = bis.read()) != -1) {
				baf.append((byte) current);
			}
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] imageData = baf.toByteArray();
		BitmapFactory.decodeByteArray(imageData, 0, imageData.length, options);
		options.inJustDecodeBounds = true;
		options.inSampleSize =  calculateInSampleSize(options, 139, 93);
		options.inJustDecodeBounds = false;
		Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length, options);
		return bitmap;
	}

	public static int calculateInSampleSize (BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and width
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will guarantee a final image
			// with both dimensions larger than or equal to the requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;

			// This offers some additional logic in case the image has a strange
			// aspect ratio. For example, a panorama may have a much larger
			// width than height. In these cases the total pixels might still
			// end up being too large to fit comfortably in memory, so we should
			// be more aggressive with sample down the image (=larger inSampleSize).

			final float totalPixels = width * height;

			// Anything more than 2x the requested pixels we'll sample down further
			final float totalReqPixelsCap = reqWidth * reqHeight * 2;

			while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
				inSampleSize++;
			}
		}
		return inSampleSize;
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
				Drawable background = new BitmapDrawable(photoToLoad.displayImage.getActivity().getResources(), bitmap);

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
