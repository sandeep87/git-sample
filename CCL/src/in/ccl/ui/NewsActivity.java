package in.ccl.ui;

import in.ccl.adapters.NewsAdapter;
import in.ccl.database.CCLPullService;
import in.ccl.database.NewsItemsCursor;
import in.ccl.helper.Util;
import in.ccl.model.Items;
import in.ccl.util.Constants;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * In News Screen contains national,regional,list controls.when tap on the regional button get data from calling activity shows in a listview,when tap in the national shows data in a list view.when tap on the list item send url to NewsDataActivity.
 * 
 * @author
 * 
 */
public class NewsActivity extends TopActivity implements OnClickListener, OnItemClickListener {

	private Button btnRegional;

	private Button btnNational;

	private ListView newsListview;

	private NewsAdapter newsAdapter;

	private ArrayList <Items> newsArrayListItems;

	private ArrayList <Items> newsItems;

	private DownloadStateReceiver mDownloadStateReceiver;

	private IntentFilter statusIntentFilter;

	@Override
	public void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addContent(R.layout.news_layout);

		// The filter's action is BROADCAST_ACTION
		statusIntentFilter = new IntentFilter(Constants.BROADCAST_ACTION);

		// Sets the filter's category to DEFAULT
		statusIntentFilter.addCategory(Intent.CATEGORY_DEFAULT);

		// Instantiates a new DownloadStateReceiver
		mDownloadStateReceiver = new DownloadStateReceiver();

		// assigning ids to the controls
		btnNational = (Button) findViewById(R.id.btn_national);
		btnRegional = (Button) findViewById(R.id.btn_regional);
		newsListview = (ListView) findViewById(R.id.news_listview);

		// on click listener for required controls
		btnNational.setOnClickListener(this);
		btnRegional.setOnClickListener(this);
		// get data from calling activity

		TextView txtNews = (TextView)findViewById(R.id.txt_news_title);
		//on click listener for required controls
	
		    Util.setTextFont(NewsActivity.this,txtNews);
        Util.setTextFont(NewsActivity.this,btnRegional);
        Util.setTextFont(NewsActivity.this,btnNational);
		//get data from calling activity
		if (getIntent().hasExtra(Constants.EXTRA_NEWS_KEY)) {
			newsArrayListItems = getIntent().getParcelableArrayListExtra(Constants.EXTRA_NEWS_KEY);
		}
		// get news items from the data base using cursor
		newsItems = NewsItemsCursor.getNews(this, 1);
		if (newsItems != null) {
			showingNewsInListView(newsItems);
		}
		newsListview.setOnItemClickListener(this);

	}

	@Override
	public void onClick (View v) {
		switch (v.getId()) {
			case R.id.btn_regional:
				btnRegional.setBackgroundResource(R.drawable.inningstab_tapped);
				btnNational.setBackgroundResource(R.drawable.innings_tab);
				newsItems = NewsItemsCursor.getNews(this, 1);
				showingNewsInListView(newsItems);

				break;

			case R.id.btn_national:

				btnNational.setBackgroundResource(R.drawable.inningstab_tapped);
				btnRegional.setBackgroundResource(R.drawable.innings_tab);
				newsItems = NewsItemsCursor.getNews(this, 2);
				showingNewsInListView(newsItems);

				break;

			default:
				break;
		}

	}

	/**
	 * calling adapter shows in a listview
	 * 
	 * @param newsItems ArrayList <Items>
	 */
	private void showingNewsInListView (ArrayList <Items> newsItems) {
		newsAdapter = new NewsAdapter(this, newsItems);
		newsListview.setAdapter(newsAdapter);
		newsListview.setFadingEdgeLength(1);
		newsListview.setCacheColorHint(Color.TRANSPARENT);
		newsListview.setVerticalScrollBarEnabled(false);
	}

	@Override
	public void onItemClick (AdapterView <?> arg0, View arg1, int position, long arg3) {
		// checking network connection when navigating activity and send url to NewsDataActivity,if neetwork is not available shows toast message.
		if (Util.getInstance().isOnline(NewsActivity.this)) {
			Intent newsPhotoIntent = new Intent(this, NewsDataActivity.class);
			newsPhotoIntent.putExtra(Constants.EXTRA_NEWS_DOWNLOAD_IMAGE_KEY, newsItems.get(position).getPhotoOrVideoUrl());
			startActivity(newsPhotoIntent);
		}
		else {
			Toast.makeText(NewsActivity.this, getResources().getString(R.string.network_error_message), Toast.LENGTH_LONG).show();

		}

	}

	@Override
	protected void onResume () {
		// TODO Auto-generated method stub
		super.onResume();
		LocalBroadcastManager.getInstance(this).registerReceiver(mDownloadStateReceiver, statusIntentFilter);
		if (Util.getInstance().isOnline(NewsActivity.this)) {
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run () {
					Intent mServiceIntent = new Intent(NewsActivity.this, CCLPullService.class).setData(Uri.parse(getResources().getString(R.string.news_url)));
					mServiceIntent.putExtra("KEY", "news_updates");
					startService(mServiceIntent);

					mServiceIntent = new Intent(NewsActivity.this, CCLPullService.class).setData(Uri.parse(getResources().getString(R.string.nation_news_url)));
					mServiceIntent.putExtra("KEY", "national_news_updates");
					startService(mServiceIntent);
				}
			}, 20000);
		}

	}

	@Override
	protected void onPause () {
		// TODO Auto-generated method stub
		super.onPause();
		LocalBroadcastManager.getInstance(this).unregisterReceiver(mDownloadStateReceiver);

	}

	private class DownloadStateReceiver extends BroadcastReceiver {

		private DownloadStateReceiver () {
		}

		/**
		 * 
		 * This method is called by the system when a broadcast Intent is matched by this class' intent filters
		 * 
		 * @param context An Android context
		 * @param intent The incoming broadcast Intent
		 */
		@Override
		public void onReceive (Context context, Intent intent) {

			// Gets the status from the Intent's extended data, and chooses the appropriate action

			switch (intent.getIntExtra(Constants.EXTENDED_DATA_STATUS, Constants.STATE_ACTION_COMPLETE)) {

				case in.ccl.database.Constants.STATE_ACTION_NEWS_UPDATE_COMPLETE:

					ArrayList <Items> regionalList = NewsItemsCursor.getNews(NewsActivity.this, 1);

					showingNewsInListView(regionalList);

					break;

				case in.ccl.database.Constants.STATE_ACTION_NATIONAL_NEWS_UPDATE_COMPLETE:

					ArrayList <Items> nationalList = NewsItemsCursor.getNews(NewsActivity.this, 2);

					showingNewsInListView(nationalList);

					break;
				default:
					break;
			}
		}
	}

}
