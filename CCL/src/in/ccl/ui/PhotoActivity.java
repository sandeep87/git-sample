package in.ccl.ui;

import in.ccl.adapters.FullPagerAdapter;
import in.ccl.helper.Category;
import in.ccl.helper.PageChangeListener;
import in.ccl.model.Items;
import in.ccl.util.Constants;

import java.util.ArrayList;

import com.facebook.android.DialogError;
import com.facebook.android.FacebookError;
import com.facebook.android.Facebook.DialogListener;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class PhotoActivity extends Activity {

	private ArrayList <Items> photoGalleryList;

	private int imagePosition;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo_view);

		// get intent data
		if (getIntent().hasExtra(Constants.EXTRA_PHOTO_KEY)) {
			photoGalleryList = getIntent().getParcelableArrayListExtra(Constants.EXTRA_PHOTO_KEY);
		}
		if (getIntent().hasExtra(Constants.EXTRA_PHOTO_POSITION_ID)) {
			imagePosition = getIntent().getIntExtra(Constants.EXTRA_PHOTO_POSITION_ID, 0);
		}
		ViewPager fullPhotoViewPager = (ViewPager) findViewById(R.id.photo_full_view_pager);
		fullPhotoViewPager.setAdapter(new FullPagerAdapter(this, photoGalleryList, Category.FULL_SCREEN, imagePosition));
		fullPhotoViewPager.setCurrentItem(getIntent().getIntExtra(Constants.EXTRA_PHOTO_POSITION_ID, 0));
		fullPhotoViewPager.setOnPageChangeListener(new PageChangeListener(null, fullPhotoViewPager, photoGalleryList.size()));
		fullPhotoViewPager.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick (View v) {
		     Toast.makeText(v.getContext(), "imageView.onLongClick", Toast.LENGTH_LONG).show();

				final Dialog shareDialog = new Dialog(PhotoActivity.this);
				shareDialog.setContentView(R.layout.dialog_layout);
				shareDialog.setTitle("Image displayed in FaceBook");
				ListView imageList = (ListView) shareDialog.findViewById(R.id.dialog_list);
				String[] imageDetails = { "LIKE", "COMMENT", "SHARE" };
				ArrayAdapter adapter = new ArrayAdapter <String>(PhotoActivity.this, android.R.layout.simple_list_item_1, imageDetails);
				imageList.setAdapter(adapter);
				shareDialog.show();
				imageList.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick (AdapterView <?> arg0, View arg1, int position, long arg3) {
						SharedPreferences myPrefs = getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
						String myFacebookId = myPrefs.getString("FaceBook_User_Id", null);
						String access_token = myPrefs.getString("access_token", null);
						if (position == 0) {

							Toast.makeText(PhotoActivity.this, "Need to Implement", Toast.LENGTH_LONG).show();

						}
						else if (position == 1) {
							Toast.makeText(PhotoActivity.this, "Need to Implement", Toast.LENGTH_LONG).show();

						}
						else {/*
									 * if (mFaceBook != null) { if (access_token == null && myFacebookId == null) { authorization(); } else { //shareOnFaceBook();
									 * 
									 * }
									 * 
									 * }
									 */
						}

						shareDialog.cancel();
					}

					private void authorization () {/*
																					 * mFaceBook.authorize(activity, new String[] { "publish_stream" }, new DialogListener() {
																					 * 
																					 * public void onFacebookError (FacebookError e) { }
																					 * 
																					 * public void onError (DialogError e) { }
																					 * 
																					 * public void onComplete (Bundle values) { myPrefs = activity.getSharedPreferences("myPrefs", Context.MODE_PRIVATE); prefsEditor = myPrefs.edit(); prefsEditor.putString("access_token", mFaceBook.getAccessToken()); prefsEditor.commit();
																					 * 
																					 * handler.sendEmptyMessage(Constants.LOGIN_SUCCESS); }
																					 * 
																					 * public void onCancel () { } });
																					 */
					}
				});

				return true;
			}
		});

	}

}
