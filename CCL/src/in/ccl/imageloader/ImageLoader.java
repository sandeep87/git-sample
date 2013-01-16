package in.ccl.imageloader;

import java.io.File;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Stack;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

public class ImageLoader {

	public static Bitmap bmp;

	// the simplest in-memory cache implementation. This should be replaced with something like SoftReference or BitmapOptions.inPurgeable(since 1.6)
	private static HashMap <String, Bitmap> cache = new HashMap <String, Bitmap>();

	private File cacheDir;

	private Context mContext;

	private volatile static ImageLoader instance;

	/** Returns singleton class instance */
	public static ImageLoader getInstance () {
		if (instance == null) {
			synchronized (ImageLoader.class) {
				if (instance == null) {
					instance = new ImageLoader();
				}
			}
		}
		return instance;
	}

	protected ImageLoader () {
	}

	public ImageLoader (Context context) {
		mContext = context;
		// Make the background thead low priority. This way it will not affect the UI performance
		photoLoaderThread.setPriority(Thread.NORM_PRIORITY - 1);

		// Find the dir to save cached images
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
			cacheDir = new File(android.os.Environment.getExternalStorageDirectory(), "LazyList");
		else
			cacheDir = mContext.getCacheDir();
		if (!cacheDir.exists())
			cacheDir.mkdirs();
	}

	// final int stub_id = R.drawable.ccl_no_avatar;

	public void displayImage (DisplayImage displayImage, ImageLoadingListener listener) {
		listener.onLoadingStarted();
		if (displayImage != null) {
			if (cache.containsKey(displayImage.getUrl())) {
				Bitmap bmp = cache.get(displayImage.getUrl());
				if (bmp != null) {
					Drawable drawable = new BitmapDrawable(bmp);
					displayImage.getImageView().setBackgroundDrawable(drawable);
					listener.onLoadingComplete(bmp);
				}
				else {
					listener.onLoadingFailed(FailToLoad.IO_ERROR);
				}
			}
			else {
				queuePhoto(displayImage.getUrl(), displayImage.getActivity(), displayImage.getImageView(), listener);
				// imageView.Resource(stub_id);
			}
		}
	}

	private void queuePhoto (String url, Activity activity, ImageView imageView, ImageLoadingListener listener) {
		// This ImageView may be used for other images before. So there may be some old tasks in the queue. We need to discard them.
		photosQueue.Clean(imageView);
		PhotoToLoad p = new PhotoToLoad(url, imageView, listener);
		synchronized (photosQueue.photosToLoad) {
			photosQueue.photosToLoad.push(p);
			photosQueue.photosToLoad.notifyAll();
		}

		// start thread if it's not started yet
		if (photoLoaderThread.getState() == Thread.State.NEW)
			photoLoaderThread.start();
	}

	private Bitmap getBitmap (String url) {

		if (cache.containsKey(url)) {
			Bitmap bmp = cache.get(url);
			return bmp;
		}

		try {
			// Bitmap bitmap = null;
			URL urls = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) urls.openConnection();
			connection.setDoInput(true);
			connection.connect();
			InputStream input = connection.getInputStream();
			return BitmapFactory.decodeStream(new FlushedInputStream(input));
		}
		catch (Exception ex) {

		}
		return null;

	}

	// Task for the queue
	private class PhotoToLoad {

		public String url;

		public ImageView imageView;

		public ImageLoadingListener listener;

		public PhotoToLoad (String u, ImageView i, ImageLoadingListener lis) {
			url = u;
			imageView = i;
			listener = lis;
		}
	}

	PhotosQueue photosQueue = new PhotosQueue();

	public void stopThread () {
		photoLoaderThread.interrupt();
	}

	// stores list of photos to download
	class PhotosQueue {

		private Stack <PhotoToLoad> photosToLoad = new Stack <PhotoToLoad>();

		// removes all instances of this ImageView
		public void Clean (ImageView image) {
			try {
				for (int j = 0; j < photosToLoad.size();) {
					if (photosToLoad.get(j).imageView == image)
						photosToLoad.remove(j);
					else
						++j;

				}
			}
			catch (ArrayIndexOutOfBoundsException e) {
			}
			catch (IndexOutOfBoundsException e) {

			}
		}
	}

	class PhotosLoader extends Thread {

		public void run () {
			try {
				while (true) {
					// thread waits until there are any images to load in the queue
					if (photosQueue.photosToLoad.size() == 0)
						synchronized (photosQueue.photosToLoad) {
							photosQueue.photosToLoad.wait();
						}
					if (photosQueue.photosToLoad.size() != 0) {
						PhotoToLoad photoToLoad;
						synchronized (photosQueue.photosToLoad) {
							photoToLoad = photosQueue.photosToLoad.pop();
						}
						bmp = getBitmap(photoToLoad.url.replace(" ", "%20"));
						cache.put(photoToLoad.url, bmp);
						Object tag = photoToLoad.imageView.getTag();
						if (tag != null && ((String) tag).equals(photoToLoad.url)) {

							BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad.imageView, photoToLoad.listener);
							Activity a = (Activity) photoToLoad.imageView.getContext();
							/*
							 * if (bmp != null) { bmp.recycle(); bmp = null; }
							 */
							a.runOnUiThread(bd);
						}
					}
					if (Thread.interrupted())
						break;
				}
			}
			catch (InterruptedException e) {
				// allow thread to exit
			}
			catch (OutOfMemoryError e) {

			}
		}
	}

	public class PatchInputStream extends FilterInputStream {

		public PatchInputStream (InputStream in) {
			super(in);
		}

		public long skip (long n) throws IOException {
			long m = 0L;
			while (m < n) {
				long _m = in.skip(n - m);
				if (_m == 0L)
					break;
				m += _m;
			}
			return m;
		}
	}

	PhotosLoader photoLoaderThread = new PhotosLoader();

	// Used to display bitmap in the UI thread
	class BitmapDisplayer implements Runnable {

		Bitmap bitmap = null;

		ImageView imageView;

		ImageLoadingListener listener;

		public BitmapDisplayer (Bitmap b, ImageView i, ImageLoadingListener lis) {
			bitmap = b;
			imageView = i;
			listener = lis;
		}

		public void run () {
			if (bitmap != null) {
				Drawable drawable = new BitmapDrawable(bitmap);
				imageView.setBackgroundDrawable(drawable);
				if (listener != null) {
					listener.onLoadingComplete(bitmap);
				}
			}
			else {
				listener.onLoadingFailed(FailToLoad.IO_ERROR);
			}
		}
	}

	public void clearCache () {
		// clear memory cache
		cache.clear();

		// clear SD cache
		File[] files = cacheDir.listFiles();
		for (File f : files)
			f.delete();
	}

	public void disposeBitmap (Bitmap bitmap) {
		bitmap.recycle();
		bitmap = null;
	}

	static class FlushedInputStream extends FilterInputStream {

		public FlushedInputStream (InputStream inputStream) {
			super(inputStream);
		}

		@Override
		public long skip (long n) throws IOException {
			long totalBytesSkipped = 0L;
			while (totalBytesSkipped < n) {
				long bytesSkipped = in.skip(n - totalBytesSkipped);
				if (bytesSkipped == 0L) {
					int byteValue = read();
					if (byteValue < 0) {
						break; // we reached EOF
					}
					else {
						bytesSkipped = 1; // we read one byte
					}
				}
				totalBytesSkipped += bytesSkipped;
			}
			return totalBytesSkipped;
		}
	}

}
