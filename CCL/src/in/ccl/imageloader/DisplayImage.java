package in.ccl.imageloader;

import in.ccl.helper.Util;
import in.ccl.logging.Logger;
import android.app.Activity;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class DisplayImage implements ImageLoadingListener {

	private String url;

	private ImageView imageView;

	private Activity activity;

	private String from;

	
	public String getFrom () {
		return from;
	}

	
	public void setFrom (String from) {
		this.from = from;
	}

	// if video is loading should enable paly icon once loading is completed.
	private ImageView playIcon;

	private TextView errorTitle;

	public TextView getErrorTitle () {
		return errorTitle;
	}

	public void setErrorTitle (TextView errorTitle) {
		this.errorTitle = errorTitle;
	}

	public ImageView getPlayIcon () {
		return playIcon;
	}

	public void setPlayIcon (ImageView playIcon) {
		this.playIcon = playIcon;
	}

	/*public ImageView getImgageLoader () {
		return imgageLoader;
	}*/

	public DisplayImage (String url, ImageView imageView, Activity activity, String from) {
		this.url = url;
		this.imageView = imageView;
		this.activity = activity;
	  this.from = from;
	}

	public String getUrl () {
		return url;
	}

	public ImageView getImageView () {
		return imageView;
	}

	public Activity getActivity () {
		return activity;
	}

	@Override
	public void onLoadingStarted () {
		if (errorTitle != null) {
			errorTitle.setVisibility(View.GONE);
		}
	}

	@Override
	public void onLoadingComplete (Bitmap loadedImage) {
		if (getPlayIcon() != null) {
			getPlayIcon().setVisibility(View.VISIBLE);
		}
		if (errorTitle != null) {
			errorTitle.setVisibility(View.GONE);
		}
	}

	@Override
	public void onLoadingFailed (FailToLoad failToLoad) {
		String message = null;
		switch (failToLoad) {
			case IO_ERROR:
				message = "Input/Output error";
				break;
			case OUT_OF_MEMORY:
				message = "Out Of Memory error";
				break;
			case UNKNOWN:
				message = "Unknown error";
				break;
		}
		
		Logger.info("DisplayImage", message);
		if (errorTitle != null) {
			errorTitle.setVisibility(View.VISIBLE);
			Util.setTextFont(activity, errorTitle);
			errorTitle.setText("Unable to load image");
		}
	}

	public void show () {
		ImageLoader.getInstance(activity).displayImage(this, this);

	}
}