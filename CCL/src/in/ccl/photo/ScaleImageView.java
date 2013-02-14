package in.ccl.photo;

import in.ccl.ui.R;

import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ScaleImageView extends ImageView implements OnTouchListener {

	// Indicates if caching should be used
	private boolean mCacheFlag;

	private TextView errorTitleTxt;

	// Status flag that indicates if onDraw has completed
	private boolean mIsDrawn;

	/*
	 * Creates a weak reference to the ImageView in this object. The weak reference prevents memory leaks and crashes, because it automatically tracks the "state" of the variable it backs. If the reference becomes invalid, the weak reference is garbage- collected. This technique is important for
	 * referring to objects that are part of a component lifecycle. Using a hard reference may cause memory leaks as the value continues to change; even worse, it can cause crashes if the underlying component is destroyed. Using a weak reference to a View ensures that the reference is more transitory
	 * in nature.
	 */
	private WeakReference <View> mThisView;

	// Contains the ID of the internal View
	private int mHideShowResId = -1;

	// The URL that points to the source of the image for this ImageView
	private URL mImageURL;

	// The Thread that will be used to download the image for this ImageView
	private PhotoTask mDownloadThread;

	// for zoom in and Zoom out

	private float MAX_SCALE = 5f;

	private int DOUBLE_TAP_SECOND = 400;

	private Matrix mMatrix;

	private final float[] mMatrixValues = new float[9];

	// display width height.
	private int mWidth;

	private int mHeight;

	private int mIntrinsicWidth;

	private int mIntrinsicHeight;

	private float mScale;

	private float mMinScale;

	// double tap for determining
	private long mLastTime = 0;

	private boolean isDoubleTap;

	private int mDoubleTapX;

	private int mDoubleTapY;

	private float mPrevDistance;

	private boolean isScaling;

	private int mPrevMoveX;

	private int mPrevMoveY;

	private Context mContext;

	/**
	 * Creates an ImageDownloadView with no settings
	 * 
	 * @param context A context for the View
	 */
	public ScaleImageView (Context context) {
		super(context);
		// getAttributes(attributeSet);
		initialize();

	}

	/**
	 * Creates an ImageDownloadView and gets attribute values
	 * 
	 * @param context A Context to use with the View
	 * @param attributeSet The entire set of attributes for the View
	 */
	public ScaleImageView (Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
		getAttributes(attributeSet);
		mContext = context;
		initialize();
		// Gets attributes associated with the attribute set
		setOnTouchListener(this);
	}

	/**
	 * Creates an ImageDownloadView, gets attribute values, and applies a default style
	 * 
	 * @param context A context for the View
	 * @param attributeSet The entire set of attributes for the View
	 * @param defaultStyle The default style to use with the View
	 */
	public ScaleImageView (Context context, AttributeSet attributeSet, int defaultStyle) {
		super(context, attributeSet, defaultStyle);
		mContext = context;

		getAttributes(attributeSet);
		initialize();
		setOnTouchListener(this);
		// Gets attributes associated with the attribute set
	}

	private void initialize () {
		this.setScaleType(ScaleType.MATRIX);
		this.mMatrix = new Matrix();
		Drawable d = getDrawable();
		System.out.println("get drawable " + d);
		if (d != null) {
			mIntrinsicWidth = d.getIntrinsicWidth();
			mIntrinsicHeight = d.getIntrinsicHeight();
			setOnTouchListener(this);
		}
	}

	/**
	 * Gets the resource ID for the hideShowSibling resource
	 * 
	 * @param attributeSet The entire set of attributes for the View
	 */
	private void getAttributes (AttributeSet attributeSet) {

		// Gets an array of attributes for the View
		TypedArray attributes = getContext().obtainStyledAttributes(attributeSet, R.styleable.ImageDownloaderView);

		// Gets the resource Id of the View to hide or show
		mHideShowResId = attributes.getResourceId(R.styleable.ImageDownloaderView_hideShowSibling, -1);

		// Returns the array for re-use
		attributes.recycle();
	}

	/**
	 * Sets the visibility of the PhotoView
	 * 
	 * @param visState The visibility state (see View.setVisibility)
	 */
	private void showView (int visState) {
		// If the View contains something
		System.out.println("Rajesh mThisView  " + mThisView);
		if (mThisView != null) {

			// Gets a local hard reference to the View
			View localView = mThisView.get();

			// If the weak reference actually contains something, set the visibility
			System.out.println("Rajesh local view " + localView);
			if (localView != null)
				localView.setVisibility(visState);
		}
	}

	/**
	 * Sets the image in this ImageView to null, and makes the View visible
	 */
	public void clearImage () {
		setImageDrawable(null);
		showView(View.VISIBLE);
	}

	/**
	 * Returns the URL of the picture associated with this ImageView
	 * 
	 * @return a URL
	 */
	final URL getLocation () {
		return mImageURL;
	}

	/*
	 * This callback is invoked when the system attaches the ImageView to a Window. The callback is invoked before onDraw(), but may be invoked after onMeasure()
	 */
	@Override
	protected void onAttachedToWindow () {
		// Always call the supermethod first
		super.onAttachedToWindow();

		// If the sibling View is set and the parent of the ImageView is itself a View
		if ((this.mHideShowResId != -1) && ((getParent() instanceof View))) {

			// Gets a handle to the sibling View
			View localView = ((View) getParent()).findViewById(this.mHideShowResId);

			// If the sibling View contains something, make it the weak reference for this View
			if (localView != null) {
				this.mThisView = new WeakReference <View>(localView);
			}
		}
	}

	/*
	 * This callback is invoked when the system tells the View to draw itself. If the View isn't already drawn, and its URL isn't null, it invokes a Thread to download the image. Otherwise, it simply passes the existing Canvas to the super method
	 */
	@Override
	protected void onDraw (Canvas canvas) {
		// If the image isn't already drawn, and the URL is set
		if ((!mIsDrawn) && (mImageURL != null)) {

			// Starts downloading this View, using the current cache setting
			mDownloadThread = PhotoManager.startDownload(this, mCacheFlag, true);

			// After successfully downloading the image, this marks that it's available.
			mIsDrawn = true;
		}
		// Always call the super method last
		super.onDraw(canvas);
	}

	/**
	 * Sets the current View weak reference to be the incoming View. See the definition of mThisView
	 * 
	 * @param view the View to use as the new WeakReference
	 */
	/*
	 * public void setHideView (View view) { this.mThisView = new WeakReference <View>(view); }
	 */

	@Override
	public void setImageBitmap (Bitmap paramBitmap) {
		System.out.println("Setting view  from scale Image view" + paramBitmap);
		super.setImageBitmap(paramBitmap);
		this.initialize(); // phani
	}

	@Override
	protected boolean setFrame (int l, int t, int r, int b) {
		mWidth = r - l;
		mHeight = b - t;

		mMatrix.reset();
		mScale = (float) r / (float) mIntrinsicWidth;
		int paddingHeight = 0;
		int paddingWidth = 0;
		// scaling vertical
		if (mScale * mIntrinsicHeight > mHeight) {
			mScale = (float) mHeight / (float) mIntrinsicHeight;
			mMatrix.postScale(mScale, mScale);
			paddingWidth = (r - mWidth) / 2;
			paddingHeight = 0;
			// scaling horizontal
		}
		else {
			mMatrix.postScale(mScale, mScale);
			paddingHeight = (b - mHeight) / 2;
			paddingWidth = 0;
		}
		mMatrix.postTranslate(paddingWidth, paddingHeight);

		setImageMatrix(mMatrix);
		mMinScale = mScale;
		zoomTo(mScale, mWidth / 2, mHeight / 2);
		cutting();

		return super.setFrame(l, t, r, b);
	}

	@Override
	public void setImageDrawable (Drawable drawable) {
		/*
		 * // The visibility of the View int viewState;
		 * 
		 * 
		 * Sets the View state to visible if the method is called with a null argument (the image is being cleared). Otherwise, sets the View state to invisible before refreshing it.
		 * 
		 * if (drawable == null) {
		 * 
		 * viewState = View.VISIBLE; } else {
		 * 
		 * viewState = View.INVISIBLE; }
		 */// Either hides or shows the View, depending on the view state
		// showView(viewState);

		// Invokes the supermethod with the provided drawable
		super.setImageDrawable(drawable);
		// super.setBackgroundDrawable(background)
	}

	/*
	 * Displays a drawable in the View
	 */
	@Override
	public void setImageResource (int resId) {
		super.setImageResource(resId);
	}

	/*
	 * Sets the URI for the Image
	 */
	@Override
	public void setImageURI (Uri uri) {
		super.setImageURI(uri);
	}

	/**
	 * Attempts to set the picture URL for this ImageView and then download the picture.
	 * <p>
	 * If the picture URL for this view is already set, and the input URL is not the same as the stored URL, then the picture has moved and any existing downloads are stopped.
	 * <p>
	 * If the input URL is the same as the stored URL, then nothing needs to be done.
	 * <p>
	 * If the stored URL is null, then this method starts a download and decode of the picture
	 * 
	 * @param pictureURL An incoming URL for a Picasa picture
	 * @param cacheFlag Whether to use caching when doing downloading and decoding
	 * @param imageDrawable The Drawable to use for this ImageView
	 * @param errorTxt
	 */
	public void setImageURL (String pictureUrl, boolean cacheFlag, Drawable imageDrawable, TextView errorTxt) {
		errorTitleTxt = errorTxt;
		URL pictureURL = null;
		try {
			if (pictureUrl != null && !TextUtils.isEmpty(pictureUrl)) {
				pictureURL = new URL(pictureUrl.replace(" ", "%20"));
			}
		}
		catch (MalformedURLException e) {
			System.out.println("Malformed exception " + pictureUrl);
			e.printStackTrace();
		}

		// If the picture URL for this ImageView is already set
		if (mImageURL != null) {

			// If the stored URL doesn't match the incoming URL, then the picture has changed.
			if (!mImageURL.equals(pictureURL)) {

				// Stops any ongoing downloads for this ImageView
				PhotoManager.removeDownload(mDownloadThread, mImageURL);
			}
			else {

				// The stored URL matches the incoming URL. Returns without doing any work.
				return;
			}
		}

		// Sets the Drawable for this ImageView
		// setImageDrawable(imageDrawable);
		setBackgroundDrawable(imageDrawable);

		// Stores the picture URL for this ImageView
		mImageURL = pictureURL;

		// If the draw operation for this ImageVIew has completed, and the picture URL isn't empty
		if ((mIsDrawn) && (pictureURL != null)) {

			// Sets the cache flag
			mCacheFlag = cacheFlag;

			/*
			 * Starts a download of the picture file. Notice that if caching is on, the picture file's contents may be taken from the cache.
			 */
			mDownloadThread = PhotoManager.startDownload(this, cacheFlag, false);
		}
	}

	/**
	 * Sets the Drawable for this ImageView
	 * 
	 * @param drawable A Drawable to use for the ImageView
	 */
	public void setStatusDrawable (Drawable drawable) {

		// If the View is empty, sets a Drawable as its content
		if (mThisView == null) {
			// setImageDrawable(drawable);
			setBackgroundDrawable(drawable);

		}
	}

	/**
	 * Sets the content of this ImageView to be a Drawable resource
	 * 
	 * @param resId
	 */
	public void setStatusResource (int resId) {
		if (errorTitleTxt != null) {
			errorTitleTxt.setTextColor(Color.LTGRAY);
		}
		// If the View is empty, provides it with a Drawable resource as its content
		if (mThisView == null) {
			if (resId == PhotoManager.DOWNLOAD_FAILED) {
				if (errorTitleTxt != null) {
					setImageResource(R.drawable.blackbackground);

					errorTitleTxt.setVisibility(View.VISIBLE);
					errorTitleTxt.setText("Loading...");
				}
			}
			else if (resId == PhotoManager.DOWNLOAD_STARTED) {
				if (errorTitleTxt != null) {
					errorTitleTxt.setVisibility(View.VISIBLE);
					errorTitleTxt.setText("Loading...");
				}

			}
			else if (resId == PhotoManager.TASK_COMPLETE) {
				System.out.println("TASK COMPLETED OOOOOOOO");
				if (errorTitleTxt != null) {
					errorTitleTxt.setVisibility(View.GONE);
				}
			}
			else {
				setImageResource(R.drawable.blackbackground);

			}
		}
	}

	// for
	protected void zoomTo (float scale, int x, int y) {
		if (getScale() * scale < mMinScale) {
			return;
		}
		if (scale >= 1 && getScale() * scale > MAX_SCALE) {
			return;
		}
		mMatrix.postScale(scale, scale);
		// move to center
		mMatrix.postTranslate(-(mWidth * scale - mWidth) / 2, -(mHeight * scale - mHeight) / 2);

		// move x and y distance
		mMatrix.postTranslate(-(x - (mWidth / 2)) * scale, 0);
		mMatrix.postTranslate(0, -(y - (mHeight / 2)) * scale);
		setImageMatrix(mMatrix);
	}

	protected float getValue (Matrix matrix, int whichValue) {
		matrix.getValues(mMatrixValues);
		return mMatrixValues[whichValue];
	}

	protected float getScale () {
		return getValue(mMatrix, Matrix.MSCALE_X);
	}

	protected float getTranslateX () {
		return getValue(mMatrix, Matrix.MTRANS_X);
	}

	protected float getTranslateY () {
		return getValue(mMatrix, Matrix.MTRANS_Y);
	}

	public void cutting () {

		int width = (int) (mIntrinsicWidth * getScale());
		int height = (int) (mIntrinsicHeight * getScale());
		if (getTranslateX() < -(width - mWidth)) {
			mMatrix.postTranslate(-(getTranslateX() + width - mWidth), 0);
		}
		if (getTranslateX() > 0) {
			mMatrix.postTranslate(-getTranslateX(), 0);
		}
		if (getTranslateY() < -(height - mHeight)) {
			mMatrix.postTranslate(0, -(getTranslateY() + height - mHeight));
		}
		if (getTranslateY() > 0) {
			mMatrix.postTranslate(0, -getTranslateY());
		}
		if (width < mWidth) {
			mMatrix.postTranslate((mWidth - width) / 2, 0);
		}
		if (height < mHeight) {
			mMatrix.postTranslate(0, (mHeight - height) / 2);
		}
		setImageMatrix(mMatrix);

	}

	private float distance (float x0, float x1, float y0, float y1) {
		float x = x0 - x1;
		float y = y0 - y1;
		return FloatMath.sqrt(x * x + y * y);
	}

	private float dispDistance () {
		return FloatMath.sqrt(mWidth * mWidth + mHeight * mHeight);
	}

	protected void maxZoomTo (int x, int y) {
		if (mMinScale != getScale() && (getScale() - mMinScale) > 0.1f) {
			// threshold 0.1f
			float scale = mMinScale / getScale();
			zoomTo(scale, x, y);
		}
		else {
			float scale = MAX_SCALE / getScale();
			zoomTo(scale, x, y);
		}
	}

	@Override
	public boolean onTouchEvent (MotionEvent event) {
		int touchCount = event.getPointerCount();

		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
			case MotionEvent.ACTION_POINTER_1_DOWN:
			case MotionEvent.ACTION_POINTER_2_DOWN:
				if (touchCount >= 2) {
					float distance = distance(event.getX(0), event.getX(1), event.getY(0), event.getY(1));
					mPrevDistance = distance;
					isScaling = true;
				}
				else {
					if (System.currentTimeMillis() <= mLastTime + DOUBLE_TAP_SECOND) {
						if (30 > Math.abs(mPrevMoveX - event.getX()) + Math.abs(mPrevMoveY - event.getY())) {
							isDoubleTap = true;
							mDoubleTapX = (int) event.getX();
							mDoubleTapY = (int) event.getY();
						}
					}
					mLastTime = System.currentTimeMillis();
					mPrevMoveX = (int) event.getX();
					mPrevMoveY = (int) event.getY();
				}
				break;
			case MotionEvent.ACTION_MOVE:
				if (touchCount >= 2 && isScaling) {
					float dist = distance(event.getX(0), event.getX(1), event.getY(0), event.getY(1));
					float scale = (dist - mPrevDistance) / dispDistance();
					mPrevDistance = dist;
					scale += 1;
					scale = scale * scale;
					zoomTo(scale, mWidth / 2, mHeight / 2);
					cutting();
				}
				else if (!isScaling) {
					int distanceX = mPrevMoveX - (int) event.getX();
					int distanceY = mPrevMoveY - (int) event.getY();
					mPrevMoveX = (int) event.getX();
					mPrevMoveY = (int) event.getY();
					mMatrix.postTranslate(-distanceX, -distanceY);
					cutting();
				}
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_POINTER_UP:
			case MotionEvent.ACTION_POINTER_2_UP:
				if (event.getPointerCount() <= 1) {
					isScaling = false;
					if (isDoubleTap) {
						if (30 > Math.abs(mDoubleTapX - event.getX()) + Math.abs(mDoubleTapY - event.getY())) {
							maxZoomTo(mDoubleTapX, mDoubleTapY);
							cutting();
						}else{
							System.out.println("ON click and long click");
							Toast.makeText(mContext,"On click event list", Toast.LENGTH_LONG).show();
						}
					
					}
					isDoubleTap = false;

					break;
				}
		}
		return true;
	}

	@Override
	public boolean onTouch (View v, MotionEvent event) {
		return false;
	}


}
