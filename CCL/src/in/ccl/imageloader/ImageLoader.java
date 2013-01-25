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

	// final int stub_id = R.raw.pre_loader;
	final int stubs_id = R.drawable.banner_item_preloader;

	private String TAG = "Image Loader'";

	final int stub_id = R.drawable.grid_item_preloader;

	final int photo_preview_stub_id = R.drawable.photo_preview_preloader;
 
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
					if (displayImage.getFrom() != null) {
						if(displayImage.getFrom().equals("banner") || displayImage.getFrom().equals("fullview")){
							displayImage.getImageView().setImageBitmap(null);
							if(displayImage.getFrom().equals("banner")){
								displayImage.getImageView().setBackgroundDrawable(background);			    

							}else{
								displayImage.getImageView().setImageDrawable(background);   

							}
						}else{
							bitmap =  Bitmap.createScaledBitmap(bitmap,139,93,true);
							background = new BitmapDrawable(displayImage.getActivity().getResources(), bitmap);
							displayImage.getImageView().setImageBitmap(null);
							displayImage.getImageView().setBackgroundDrawable(background);			
						}
				}
				else {
					displayImage.getImageView().setImageBitmap(null);
					displayImage.getImageView().setBackgroundDrawable(background);
				}
				listener.onLoadingComplete(bitmap);
			}
			else {
				if (displayImage.getFrom() != null) {
					if (displayImage.getFrom().equals("banner")) {
						displayImage.getImageView().setImageResource(stubs_id);
						displayImage.getImageView().setBackgroundDrawable(null);

					}
					else if (displayImage.getFrom().equals("fullview")) {
						displayImage.getImageView().setImageResource(photo_preview_stub_id);
						displayImage.getImageView().setBackgroundDrawable(null);

					}
					else {
						displayImage.getImageView().setImageResource(stub_id);
						displayImage.getImageView().setBackgroundDrawable(null);
					}
				}
				else {
					// displayImage.getImageView().setBackgroundResource(stub_id);
					displayImage.getImageView().setImageResource(stub_id);
					displayImage.getImageView().setBackgroundDrawable(null);
				}
				listener.onLoadingFailed(FailToLoad.IO_ERROR);
			}
		}
		else {
			queuePhoto(displayImage);
			if (displayImage.getFrom() != null) {
				if (displayImage.getFrom().equals("banner")) {
					displayImage.getImageView().setImageResource(stubs_id);
					displayImage.getImageView().setBackgroundDrawable(null);
				}
				else if (displayImage.getFrom().equals("fullview")) {
					displayImage.getImageView().setImageResource(photo_preview_stub_id);
					displayImage.getImageView().setBackgroundDrawable(null);
				}
				else {
					displayImage.getImageView().setImageResource(stub_id);
					displayImage.getImageView().setBackgroundDrawable(null);
				}
			}
			else {
				// displayImage.getImageView().setBackgroundResource(stub_id);
				displayImage.getImageView().setImageResource(stub_id);
				displayImage.getImageView().setBackgroundDrawable(null);

			}
		}
	}

	/**
	 * * It calls PhotoToLoad class, for setting url, and imageView. PhotoLoader will be called, which will dispaly the image with the url, passed as parameter in PhotoToLoad
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
			input = new FlushedInputStream(input);
			// scaling image
			Bitmap bmp = BitmapFactory.decodeStream(input);
			return bmp;
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

	/*
	 * public Bitmap fitScreenBitmap(Bitmap bitmap2) { // TODO Auto-generated method stub if(bitmap2 == null) { //showInitialLayout(); return null; } float w = bitmap2.getWidth(); float h = bitmap2.getHeight();
	 * 
	 * System.out.println("width1 ------>"+w+"   Height1 ------->"+h);
	 * 
	 * Matrix m = new Matrix(); float scalefactor = 1;
	 * 
	 * if( w > 139 && h > 93 ) { if(w/h>2) { scalefactor = 139/w; } else { scalefactor = 93/h; }
	 * 
	 * 
	 * } else if( w > 139 && h < 93 ) { scalefactor = 139/w;
	 * 
	 * } else if( w < 139 && h > 93 ) { // System.out.println("h ->"+h ); scalefactor = 93/h; // System.out.println("hi"+scalefactor); } else if( w < 139 && h < 93 ) { if(bitmap2.isMutable()) { return bitmap2; } else // System.out.println("its an Immutable bitmap"); return
	 * bitmap2.copy(Bitmap.Config.ARGB_8888, true); }
	 * 
	 * 
	 * m.postScale(scalefactor, scalefactor);
	 * 
	 * Bitmap bmp = Bitmap.createBitmap(bitmap2, 0, 0, bitmap2.getWidth(), bitmap2.getHeight(), m, true);
	 * 
	 * if(!bmp.isMutable()) {
	 * 
	 * bmp= bmp.copy(Bitmap.Config.ARGB_8888, true); }
	 * 
	 * return bmp; }
	 */
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

	
	
	
	
 /* private void scaleImage(DisplayImage displayImage, int boundBoxInDp) 
  { 
      // Get the ImageView and its bitmap 
      Drawable drawing = displayImage.getImageView().getDrawable(); 
      if(drawing != null){
      Bitmap bitmap = ((BitmapDrawable)drawing).getBitmap(); 

      // Get current dimensions 
      int width = bitmap.getWidth(); 
      int height = bitmap.getHeight(); 

      // Determine how much to scale: the dimension requiring less scaling is 
      // closer to the its side. This way the image always stays inside your 
      // bounding box AND either x/y axis touches it. 
      float xScale = ((float) boundBoxInDp) / width; 
      float yScale = ((float) boundBoxInDp) / height; 
      float scale = (xScale <= yScale) ? xScale : yScale; 

      // Create a matrix for the scaling and add the scaling data 
      Matrix matrix = new Matrix(); 
      matrix.postScale(scale, scale); 

      // Create a new bitmap and convert it to a format understood by the ImageView 
      Bitmap scaledBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true); 
      BitmapDrawable result = new BitmapDrawable(scaledBitmap); 
      width = scaledBitmap.getWidth(); 
      height = scaledBitmap.getHeight(); 

      // Apply the scaled bitmap 
      displayImage.getImageView().setBackgroundDrawable(result); 
   
      // Now change ImageView's dimensions to match the scaled image 
      if(displayImage.getFrom().equals("banner")){
      FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) displayImage.getImageView().getLayoutParams(); 
      params.width = width; 
      params.height = height; 
      displayImage.getImageView().setLayoutParams(params); 
      }else{
      	 RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) displayImage.getImageView().getLayoutParams(); 
         params.width = width; 
         params.height = height; 
         displayImage.getImageView().setLayoutParams(params); 
      }
      }
  }*/
	
	
	
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
				if (photoToLoad.displayImage.getFrom() != null) {
					if(photoToLoad.displayImage.getFrom().equals("banner") || photoToLoad.displayImage.getFrom().equals("fullview")){
							photoToLoad.displayImage.getImageView().setImageBitmap(null);
							if(photoToLoad.displayImage.getFrom().equals("banner")){
								photoToLoad.displayImage.getImageView().setBackgroundDrawable(background);			    

							}else{
								photoToLoad.displayImage.getImageView().setImageDrawable(background);   

							}
						}else{
							bitmap =  Bitmap.createScaledBitmap(bitmap,139,93,true);
							background = new BitmapDrawable(photoToLoad.displayImage.getActivity().getResources(), bitmap);
							photoToLoad.displayImage.getImageView().setImageBitmap(null);
							photoToLoad.displayImage.getImageView().setBackgroundDrawable(background);			
						}
				
				}
				else {
					photoToLoad.displayImage.getImageView().setImageBitmap(null);
					photoToLoad.displayImage.getImageView().setBackgroundDrawable(background);
				}
			}
			else {
				if (photoToLoad.displayImage.getFrom() != null) {

					if (photoToLoad.displayImage.getFrom().equals("banner")) {
						photoToLoad.displayImage.getImageView().setImageResource(stubs_id);
						photoToLoad.displayImage.getImageView().setBackgroundDrawable(null);

					}
					else if (photoToLoad.displayImage.getFrom().equals("fullview")) {
						photoToLoad.displayImage.getImageView().setImageResource(photo_preview_stub_id);
						photoToLoad.displayImage.getImageView().setBackgroundDrawable(null);

					}
					else {
						photoToLoad.displayImage.getImageView().setImageResource(stub_id);
						photoToLoad.displayImage.getImageView().setBackgroundDrawable(null);

					}
				}
				else {
					// photoToLoad.displayImage.getImageView().setBackgroundResource(stub_id);
					photoToLoad.displayImage.getImageView().setImageResource(stub_id);
					photoToLoad.displayImage.getImageView().setBackgroundDrawable(null);

				}
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
