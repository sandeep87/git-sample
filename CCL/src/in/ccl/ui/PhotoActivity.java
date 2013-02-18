package in.ccl.ui;

import in.ccl.adapters.FullPagerAdapter;
import in.ccl.helper.CommonAsync;
import in.ccl.helper.DelegatesResponse;
import in.ccl.helper.Util;
import in.ccl.model.Items;
import in.ccl.util.Constants;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

public class PhotoActivity extends Activity implements DelegatesResponse {

	private ArrayList <Items> photoGalleryList;

	static Facebook mFaceBook = new Facebook(Constants.FACEBOOK_ID);

	private String mFaceBookUser_Id;

	private String mFinalResponse;

	private SharedPreferences myPrefs;

	private SharedPreferences.Editor prefsEditor;

	private String myFacebookId = "";

	private static PhotoActivity mInstance;

	private String url = "";

	private Context context;

	private ProgressDialog mProgressDialog;

	private int previousState;

	private int currentState;

	private static int currentPosition;

	private String AccessToken = "";

	public Handler handler = new Handler() {

		@Override
		public void handleMessage (Message msg) {
			if (msg.what == Constants.LOGIN_SUCCESS) {

				if (mFaceBook.getAccessToken() != null) {
					String url = "https://graph.facebook.com/me?access_token=" + mFaceBook.getAccessToken();
					CommonAsync mCommonAsync = new CommonAsync(context, PhotoActivity.this);
					mCommonAsync.execute(url);

				}
			}

		}
	};

	public static PhotoActivity getInstance () {
		if (mInstance == null) {
			mInstance = new PhotoActivity();
		}
		return mInstance;
	}

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo_view);
		context = this;
		mProgressDialog = new ProgressDialog(this);
		mProgressDialog.setMessage(getResources().getString(R.string.posting_message));
		mProgressDialog.setCancelable(false);
		myPrefs = PreferenceManager.getDefaultSharedPreferences(PhotoActivity.this);

		// get intent data
		if (getIntent().hasExtra(Constants.EXTRA_PHOTO_KEY)) {
			photoGalleryList = getIntent().getParcelableArrayListExtra(Constants.EXTRA_PHOTO_KEY);
		}
		final ViewPager fullPhotoViewPager = (ViewPager) findViewById(R.id.photo_full_view_pager);

		fullPhotoViewPager.setAdapter(new FullPagerAdapter(this, photoGalleryList));
		currentPosition = getIntent().getIntExtra(Constants.EXTRA_PHOTO_POSITION_ID, 0);
		fullPhotoViewPager.setCurrentItem(currentPosition);
		fullPhotoViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected (int position) {
				currentPosition = position;
			}

			@Override
			public void onPageScrolled (int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrollStateChanged (int state) {
				int currentPage = fullPhotoViewPager.getCurrentItem();

				if (currentPage == photoGalleryList.size() - 1 || currentPage == 0) {
					previousState = currentState;
					currentState = state;
					if (previousState == 1 && currentState == 0) {
						fullPhotoViewPager.setCurrentItem(currentPage == 0 ? photoGalleryList.size() : 0);
					}
				}
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu (Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.share, menu);
		return true;

	}

	@Override
	public boolean onOptionsItemSelected (MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
			case R.id.share:
				if (Util.getInstance().isOnline(this)) {
					String myFacebookId = myPrefs.getString("Fb_UserId", null);
					String access_token = myPrefs.getString("AccessToken", null);
					if (access_token == null && myFacebookId == null) {
						authorization();
					}
					else {
						shareOnFaceBook(photoGalleryList.get(currentPosition).getPhotoOrVideoUrl());
					}
				}
				else {
					Toast.makeText(PhotoActivity.this, getResources().getString(R.string.network_error_message), Toast.LENGTH_LONG).show();
				}
				return true;
			default:
				return super.onOptionsItemSelected(item);
		}
	}

	private void authorization () {
		mFaceBook.authorize(PhotoActivity.this, new String[] { "publish_stream" }, new DialogListener() {

			public void onFacebookError (FacebookError e) {
			}

			public void onError (DialogError e) {
			}

			public void onComplete (Bundle values) {
				myPrefs = PreferenceManager.getDefaultSharedPreferences(PhotoActivity.this);
				prefsEditor = myPrefs.edit();
				prefsEditor.putString("AccessToken", mFaceBook.getAccessToken());
				prefsEditor.commit();

				handler.sendEmptyMessage(Constants.LOGIN_SUCCESS);
			}

			public void onCancel () {
			}
		});
	}

	@Override
	public void setData (String jsonData, String isFrom) {
		if (isFrom.equalsIgnoreCase("CommonAsync")) {
			mFinalResponse = jsonData;
			try {
				JSONObject fbJsonObject = new JSONObject(mFinalResponse);
				if (fbJsonObject.has("id")) {
					if (!fbJsonObject.isNull("id")) {
						mFaceBookUser_Id = fbJsonObject.getString("id");
						SharedPreferences myPrefs = PreferenceManager.getDefaultSharedPreferences(PhotoActivity.this);
						SharedPreferences.Editor prefsEditor = myPrefs.edit();
						prefsEditor.putString("Fb_UserId", mFaceBookUser_Id);
						prefsEditor.commit();
					}
				}

				// network_provider = "facebook";
				shareOnFaceBook(photoGalleryList.get(currentPosition).getPhotoOrVideoUrl());

			}
			catch (JSONException e) {
				e.printStackTrace();
			}
		}
		else if (isFrom.equalsIgnoreCase("SampleUploadListener")) {
			Looper.prepare();
			if (jsonData.equals("Success")) {
				Toast.makeText(PhotoActivity.this, "Successfully posted.", Toast.LENGTH_LONG).show();
				if (mProgressDialog != null) {
					mProgressDialog.dismiss();
				}
			}
			else if (jsonData.equals("error")) {
				Toast.makeText(PhotoActivity.this, "No wall post made", Toast.LENGTH_LONG).show();

				if (mProgressDialog != null) {
					try {
						mProgressDialog.dismiss();
					}
					catch (IllegalArgumentException e) {
						// TODO: handle exception
					}
				}

			}
			Looper.loop();
		}

	}

	private void shareOnFaceBook (final String shareImageUrl) {
		publishPhoto(shareImageUrl);
		
		/*

		if (mProgressDialog != null) {
			try {
				mProgressDialog.show();
			}
			catch (IllegalArgumentException e) {
			}

		}
		new Thread() {

			public void run () {
				Looper.prepare();
				byte[] byteArray = ThumbImg(shareImageUrl);
				if (byteArray != null) {
					Bundle param = new Bundle();
					param = new Bundle();
					param.putString("caption", "Posted from CCL - Celebrity Cricket League Android App \n https://play.google.com/store/apps/details?id=in.ccl.ui");
					param.putByteArray("picture", byteArray);
					if (!mFaceBook.isSessionValid()) {
						// this is getting access token from the Shared preferences
						SharedPreferences mypref = PreferenceManager.getDefaultSharedPreferences(PhotoActivity.this);
						mFaceBook.setAccessToken(mypref.getString("AccessToken", null));
					}
					else {

						if (mProgressDialog != null) {
							try {
								mProgressDialog.dismiss();
							}
							catch (IllegalArgumentException e) {
								// TODO: handle exception
							}
						}

					}

					AsyncFacebookRunner mAsyncRunner = new AsyncFacebookRunner(mFaceBook);
					mAsyncRunner.request("me/photos", param, "POST", new in.ccl.helper.SampleUploadListener(PhotoActivity.this), null);

				}
				else {
					Toast.makeText(PhotoActivity.this, "No wall post made", Toast.LENGTH_SHORT).show();

					if (mProgressDialog != null) {
						try {
							mProgressDialog.dismiss();
						}
						catch (IllegalArgumentException e) {
						}
					}
				}
				Looper.loop();
			};
		}.start();

	*/}

	public static byte[] ThumbImg (String imgUrl) {

		// first check in the cache, if not available then store in the sd card memory
		HttpURLConnection connection = null;
		String userAgent = null;

		try {
			URL url = new URL(imgUrl);
			connection = (HttpURLConnection) url.openConnection();
			if (userAgent != null) {
				connection.setRequestProperty("User-Agent", userAgent);
			}
			connection.setConnectTimeout(5000);
			connection.setReadTimeout(5000);
			int CHUNKSIZE = 8192; // size of fixed chunks
			int BUFFERSIZE = 1024; // size of reading buffer

			int bytesRead = 0;
			byte[] buffer = new byte[BUFFERSIZE]; // initialize buffer
			byte[] fixedChunk = new byte[CHUNKSIZE]; // initialize 1st chunk
			ArrayList <byte[]> BufferChunkList = new ArrayList <byte[]>(); // List of chunk data
			int spaceLeft = CHUNKSIZE;
			int chunkIndex = 0;

			DataInputStream in = new DataInputStream(connection.getInputStream());

			while ((bytesRead = in.read(buffer)) != -1) { // loop until the DataInputStream is completed
				if (bytesRead > spaceLeft) {
					// copy to end of current chunk
					System.arraycopy(buffer, 0, fixedChunk, chunkIndex, spaceLeft);
					BufferChunkList.add(fixedChunk);

					// create a new chunk, and fill in the leftover
					fixedChunk = new byte[CHUNKSIZE];
					chunkIndex = bytesRead - spaceLeft;
					System.arraycopy(buffer, spaceLeft, fixedChunk, 0, chunkIndex);
				}
				else {
					// plenty of space, just copy it in
					System.arraycopy(buffer, 0, fixedChunk, chunkIndex, bytesRead);
					chunkIndex = chunkIndex + bytesRead;
				}
				spaceLeft = CHUNKSIZE - chunkIndex;
			}

			if (in != null) {
				in.close();
			}

			// copy it all into one big array
			int responseSize = (BufferChunkList.size() * CHUNKSIZE) + chunkIndex;
			Log.d("response size", "" + responseSize);
			byte[] responseBody = new byte[responseSize];
			int index = 0;
			for (byte[] b : BufferChunkList) {
				System.arraycopy(b, 0, responseBody, index, CHUNKSIZE);
				index = index + CHUNKSIZE;
			}
			System.arraycopy(fixedChunk, 0, responseBody, index, chunkIndex);

			return responseBody;

		}
		catch (SocketTimeoutException se) {

		}
		catch (Exception e) {

			e.printStackTrace();
		}
		finally {
			if (connection != null)
				connection.disconnect();
		}

		return null;
	}

	private void publishPhoto (String imageURL) {
		Log.d("FACEBOOK", "Post to Facebook!" + imageURL);

		try {

			JSONObject attachment = new JSONObject();
			attachment.put("message", "CCL -CELEBRITY CRICKET LEAGUE");
			attachment.put("name", "CCL -CELEBRITY CRICKET LEAGUE");
			attachment.put("href", imageURL);
			attachment.put("description", "Posted from CCL - Celebrity Cricket League Android App.");

			JSONObject media = new JSONObject();
			media.put("type", "image");
			media.put("src", imageURL);
			media.put("href", imageURL);
			attachment.put("media", new JSONArray().put(media));

			JSONObject properties = new JSONObject();

			JSONObject prop1 = new JSONObject();
			prop1.put("text", "Download");
			prop1.put("href", "https://play.google.com/store/apps/details?id=in.ccl.ui");
			properties.put("Get the App for free ", prop1);

			// u can make any number of prop object and put on "properties" for ex: //prop2,prop3

			attachment.put("properties", properties);

			Log.d("FACEBOOK", attachment.toString());

			Bundle params = new Bundle();
			params.putString("attachment", attachment.toString());

			mFaceBook.dialog(PhotoActivity.this, "stream.publish", params, new DialogListener() {

				@Override
				public void onFacebookError (FacebookError e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onError (DialogError e) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onComplete (Bundle values) {
					final String postId = values.getString("post_id");
					if (postId != null) {
						Log.d("FACEBOOK", "Dialog Success! post_id=" + postId);
						Toast.makeText(PhotoActivity.this, "Successfully shared on Facebook!", Toast.LENGTH_LONG).show();

					}
					else {
						Toast.makeText(PhotoActivity.this, "No wall post made", Toast.LENGTH_LONG).show();

						Log.d("FACEBOOK", "No wall post made");
					}
				}

				@Override
				public void onCancel () {
					// TODO Auto-generated method stub

				}
			});
			// mFaceBook.dialog(PhotoActivity.this, "stream.publish", params, new in.ccl.helper.SampleUploadListener(PhotoActivity.this));

		}
		catch (JSONException e) {
			Log.e("FACEBOOK", e.getLocalizedMessage(), e);
		}
	}
}
