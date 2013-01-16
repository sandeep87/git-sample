package in.ccl.helper;

import in.ccl.ui.R;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Util {

	private static Util singleInstance;

	/**
	 * private constructor
	 */
	private Util () {
	}

	/**
	 * Creating single object of this class. not required to create a new object each time when it was invoked.
	 * 
	 * @return single object of Util class.
	 */
	public static Util getInstance () {

		if (singleInstance == null) {
			synchronized (Util.class) {
				if (singleInstance == null) {
					singleInstance = new Util();
				}
			}
		}
		return singleInstance;
	}

	/**
	 * Checks network information i.e network is available or not.
	 * 
	 * @param context Context
	 * @return true if network is connected or connecting, else false.
	 */
	public boolean isOnline (Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		if (networkInfo != null) {
			return networkInfo.isConnectedOrConnecting();
		}
		return false;
	}

	/**
	 * This method is used to get the path of the SD card
	 * 
	 * @return: Path to the SD card
	 */
	public static String getSdCardPath () {
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			return Environment.getExternalStorageDirectory().getPath() + File.separator;
		}
		return null;

	}

	public static void CopyStream (InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		}
		catch (Exception ex) {
		}
	}

	public static void setPageIndicator (int position, LinearLayout pageIndicatorLayout) {
		ImageView imgPageOne = (ImageView) pageIndicatorLayout.findViewById(R.id.page_one_indicator);
		ImageView imgPageTwo = (ImageView) pageIndicatorLayout.findViewById(R.id.page_two_indicator);
		ImageView imgPageThree = (ImageView) pageIndicatorLayout.findViewById(R.id.page_three_indicator);
		if (position == 0) {
			imgPageOne.setImageResource(R.drawable.scroll_currentpage);
			imgPageTwo.setImageResource(R.drawable.scroll);
			imgPageThree.setImageResource(R.drawable.scroll);
		}
		else if (position == 1) {
			imgPageOne.setImageResource(R.drawable.scroll);
			imgPageTwo.setImageResource(R.drawable.scroll_currentpage);
			imgPageThree.setImageResource(R.drawable.scroll);
		}
		else {
			imgPageOne.setImageResource(R.drawable.scroll);
			imgPageTwo.setImageResource(R.drawable.scroll);
			imgPageThree.setImageResource(R.drawable.scroll_currentpage);
		}
	}

	public static void setTextFont (Activity activity, TextView txtView) {
		Typeface tf = Typeface.createFromAsset(activity.getAssets(), "fonts/VonnesMediumCompressed.ttf");
		txtView.setTypeface(tf);

	}

}
