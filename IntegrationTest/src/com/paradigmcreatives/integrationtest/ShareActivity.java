package com.paradigmcreatives.integrationtest;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.FacebookError;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class ShareActivity extends MapActivity {
	private ProgressDialog mProgress;
	private Drawable mdrawable;
	private MapView mMap;
	private List<Overlay> mMapOverlays;
	private HelloItemizedOverlay mItemizedoverlay;
	private String str;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_share);
		System.out.println("i am pointing to pd");
		mProgress = new ProgressDialog(this);
		mProgress.setMessage(getResources().getString(R.string.loading));
		mProgress.setTitle(getResources().getString(R.string.info));
		DownloadImage mImage = new DownloadImage(ShareActivity.this);
		mImage.execute(new String[] { "http://www.paradigmit.com/wp-content/images/thumbs/paradigm-creatives-big.jpg" });

		mMap = (MapView) findViewById(R.id.map);
		//mMap.setVisibility(View.GONE);
		mMap.setBuiltInZoomControls(true);
		//mMap.setStreetView(true);
	    mMapOverlays = mMap.getOverlays();
		
		Button share = (Button) findViewById(R.id.share);
		share.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				String message = getdata();
				System.out.println("Message"+getdata());
				postMessageOnWall(message);
			}
		});
		Button logout = (Button)findViewById(R.id.logout);
		logout.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new AsyncFacebookRunner(LoginActivity.mFacebook).logout(ShareActivity.this, new RequestListener() {
					
					public void onMalformedURLException(MalformedURLException e, Object state) {
						// TODO Auto-generated method stub
						
					}
					
					public void onIOException(IOException e, Object state) {
						// TODO Auto-generated method stub
						
					}
					
					public void onFileNotFoundException(FileNotFoundException e, Object state) {
						// TODO Auto-generated method stub
						
					}
					
					public void onFacebookError(FacebookError e, Object state) {
						// TODO Auto-generated method stub
						
					}
					
					public void onComplete(String response, Object state) {
						// TODO Auto-generated method stub
						finish();
						
					}
				});
			}
		});

	}

	public String getdata() {

		Geocoder gc = new Geocoder(ShareActivity.this);
		Double lat = 17.3667, lang = 78.4667;
		try {
			List<Address> address = gc.getFromLocation(lat, lang, 3);
			str = address.get(0).getCountryName() + "  "
					+ address.get(0).getAddressLine(0);
          System.out.println("Data"+str);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return str;
	}

	public void postMessageOnWall(String msg) {
		if (LoginActivity.mFacebook.isSessionValid()) {
			Bundle parameters = new Bundle();
			String link = "http://www.paradigmit.com/wp-content/images/thumbs/paradigm-creatives-big.jpg";
			parameters.putString("message", msg);
			parameters.putString("link", link);
			try {
				 LoginActivity.mFacebook.request("me/feed",
						parameters, "POST");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private class HelloItemizedOverlay extends ItemizedOverlay<OverlayItem> {

		private Context mcontxt;
		private ArrayList<OverlayItem> mOverlays = new ArrayList<OverlayItem>();

		public HelloItemizedOverlay(Drawable defaultMarker, Context context) {
			super(boundCenterBottom(defaultMarker));
			// TODO Auto-generated constructor stub
			mcontxt = context;
		}

		public void addOverlay(OverlayItem overlay) {
			// TODO Auto-generated method stub
			mOverlays.add(overlay);
			populate();

		}

		@Override
		protected OverlayItem createItem(int i) {
			// TODO Auto-generated method stub
			return mOverlays.get(i);
		}

		@Override
		public int size() {
			// TODO Auto-generated method stub
			return mOverlays.size();
		}

	}

	private class DownloadImage extends AsyncTask<String, Void, Drawable> {

		private Context mContext;
		private URL mImageUrl;
		private Bitmap mBitmap;

		public DownloadImage(Context context) {
			mContext = context;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			mProgress.show();
		}

		@Override
		protected Drawable doInBackground(String... image) {
			// TODO Auto-generated method stub
			try {
				mImageUrl = new URL(image[0]);
				Drawable d = Drawable.createFromStream(mImageUrl.openConnection().getInputStream(), "Source");
				System.out.println("Image" + d);
				return d;
			} catch (MalformedURLException setter) {
				// TODO Auto-generated catch block
				Log.i("tag", "url exception");
			} catch (IOException setter) {
				// TODO Auto-generated catch block
				Log.i("Tag", " converting to bitmap exception");
			}

			return null;
		}

		@Override
		protected void onPostExecute(Drawable result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (mProgress != null) {
				mProgress.dismiss();
				//mMap.setVisibility(View.VISIBLE);
				mItemizedoverlay = new HelloItemizedOverlay(
						result, ShareActivity.this);
				GeoPoint mPoint = new GeoPoint(17366700, 78466700);
				OverlayItem overlayitem = new OverlayItem(mPoint,
						"Laissez les bon temps rouler!", "I'm in Louisiana!");
				mItemizedoverlay.addOverlay(overlayitem);
				mMapOverlays.add(mItemizedoverlay);
			}
		}
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

}
