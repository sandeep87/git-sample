package in.ccl.ui;

import in.ccl.database.BannerCursor;
import in.ccl.database.CCLPullService;
import in.ccl.database.DataProviderContract;
import in.ccl.database.DownloadItemsCursor;
import in.ccl.database.NewsItemsCursor;
import in.ccl.database.PhotoAlbumCurosr;
import in.ccl.database.VideoAlbumCursor;
import in.ccl.helper.AnimationLayout;
import in.ccl.helper.Util;
import in.ccl.model.Items;
import in.ccl.model.TeamMember;
import in.ccl.model.Teams;
import in.ccl.util.Constants;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.BadTokenException;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MenuItems implements OnClickListener {

	private static MenuItems singleInstance;

	private Activity activity;
	
	private AnimationLayout mLayout;

	public static ArrayList <Items> teamLogosList;

	public static ArrayList <Items> teamRolesList;

	public static ArrayList <Items> teamMembersList;

	public static ArrayList <Items> chennaiTeamMembersList;

	public static ArrayList <Items> teluguTeamMembersList;

	public static ArrayList <Items> karnatakaTeamMembersList;

	public static ArrayList <Items> keralaTeamMembersList;

	public static ArrayList <Items> bengalTeamMembersList;

	public static ArrayList <Items> marathiTeamMembersList;

	public static ArrayList <Items> bhojpuriTeamMembersList;

	// private Items membersItem;

	private ProgressDialog progressDialog;

	private enum RequestType {
		NO_REQUEST, BANNER_REQUEST, PHOTO_ALBUMREQUEST, TEAMS_REQUEST, TEAMROLE_REQUEST, VIDEOS_REQUEST, PHOTOS_REQUEST, OWNERS_LOUNGE_REQUEST, SCHEDULE_REQUEST, VIDEO_ALBUMREQUEST, NOTIFICATIONS_REQUEST, DOWNLOADS_REQUEST, TEAM_MEMBERS_REQUEST, REGIONAL_REQUEST;
	}

	RequestType mRequestType = RequestType.NO_REQUEST;

	/**
	 * private constructor
	 */
	private MenuItems () {
	}

	/**
	 * Creating single object of this class. not required to create a new object each time when it was invoked.
	 * 
	 * @return single object of MenuItems class.
	 */
	public static MenuItems getInstance () {

		if (singleInstance == null) {
			synchronized (Util.class) {
				if (singleInstance == null) {
					singleInstance = new MenuItems();
				}
			}
		}
		return singleInstance;
	}

	public void loadMenu (Activity activity, LinearLayout layout, AnimationLayout mLayout) {
		this.activity = activity;
		this.mLayout = mLayout;
		// getting menu image button id for invisible button when user in menu
		// screen.
		RelativeLayout layoutPhotos = (RelativeLayout) layout.findViewById(R.id.layout_photos);
		RelativeLayout layoutSchedule = (RelativeLayout) layout.findViewById(R.id.layout_schedule);
		// RelativeLayout layoutNotifications = (RelativeLayout) layout.findViewById(R.id.layout_notifications);
		RelativeLayout layoutTeams = (RelativeLayout) layout.findViewById(R.id.layout_teams);
		RelativeLayout layoutOwner = (RelativeLayout) layout.findViewById(R.id.layout_ownerslounge);
		RelativeLayout layoutHome = (RelativeLayout) layout.findViewById(R.id.layout_home);
		RelativeLayout layoutNews = (RelativeLayout) layout.findViewById(R.id.layout_news);
		RelativeLayout layoutVideo = (RelativeLayout) layout.findViewById(R.id.layout_videos);
		// RelativeLayout layoutScore = (RelativeLayout) layout.findViewById(R.id.layout_scores);
		 RelativeLayout layoutDownloads = (RelativeLayout) layout.findViewById(R.id.layout_downloads);

		TextView photoTxt = (TextView) layout.findViewById(R.id.txt_photo);
		TextView newsTxt = (TextView) layout.findViewById(R.id.txt_news);
		TextView homeTxt = (TextView) layout.findViewById(R.id.txt_home);
		TextView ownersTxt = (TextView) layout.findViewById(R.id.txt_owners);
		 TextView downloadsTxt = (TextView) layout.findViewById(R.id.txt_downloads);
		TextView scheduleTxt = (TextView) layout.findViewById(R.id.txt_schedule);
		// TextView scoreTxt = (TextView) layout.findViewById(R.id.txt_score);
		TextView teamsTxt = (TextView) layout.findViewById(R.id.txt_tems);
		TextView videosTxt = (TextView) layout.findViewById(R.id.txt_video);
		// TextView notificationsTxt = (TextView) layout.findViewById(R.id.txt_notifications);

		Util.setTextFont(activity, photoTxt);
		Util.setTextFont(activity, newsTxt);
		Util.setTextFont(activity, homeTxt);
		Util.setTextFont(activity, ownersTxt);
		 Util.setTextFont(activity, downloadsTxt);
		Util.setTextFont(activity, scheduleTxt);
		// Util.setTextFont(activity, scoreTxt);
		Util.setTextFont(activity, videosTxt);
		// Util.setTextFont(activity, notificationsTxt);
		Util.setTextFont(activity, teamsTxt);

		layoutPhotos.setOnClickListener(this);
		layoutSchedule.setOnClickListener(this);
		// layoutNotifications.setOnClickListener(this);
		layoutTeams.setOnClickListener(this);
		layoutOwner.setOnClickListener(this);
		layoutVideo.setOnClickListener(this);
		 layoutDownloads.setOnClickListener(this);
		layoutHome.setOnClickListener(this);
		// layoutScore.setOnClickListener(this);
		layoutNews.setOnClickListener(this);

		/*
		 * chennaiTeamMembersList = Util.getInstance().getChnnaiTeamMembersList(); teluguTeamMembersList = Util.getInstance().getTeluguWarriorsTeamMembersList(); karnatakaTeamMembersList = Util.getInstance().getKarnatakaTeamMembersList(); keralaTeamMembersList =
		 * Util.getInstance().getKeralaTeamMembersList(); bengalTeamMembersList = Util.getInstance().getBangalTeamMembersList(); marathiTeamMembersList = Util.getInstance().getMarathiTeamMembersList(); bhojpuriTeamMembersList = Util.getInstance().getBhojpuriTeamMembersList();
		 */}

	@Override
	public void onClick (View v) {

		int key = v.getId();
		switch (key) {
			case R.id.layout_photos:
				mRequestType = RequestType.PHOTOS_REQUEST;
				Cursor cursor = activity.getContentResolver().query(DataProviderContract.PHOTO_ALBUM_TABLE_CONTENTURI, null, null, null, null);
				ArrayList <Items> list = PhotoAlbumCurosr.getItems(cursor);
				if (list == null || list.size() <= 0) {
					if (Util.getInstance().isOnline(activity)) {
						Intent mServiceIntent = new Intent(activity, CCLPullService.class).setData(Uri.parse(activity.getResources().getString(R.string.photo_album_url)));
						activity.startService(mServiceIntent);
					}
					else {
						Toast.makeText(activity, activity.getResources().getString(R.string.network_error_message), Toast.LENGTH_LONG).show();
					}
				}
				else {
					Intent photoGalleryIntent = new Intent(activity, PhotoGalleryActivity.class);
					photoGalleryIntent.putParcelableArrayListExtra(Constants.EXTRA_PHOTO_KEY, list);
					// photoGalleryIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
					activity.startActivity(photoGalleryIntent);

				}
				break;
			case R.id.layout_schedule:
				progressDialog = new ProgressDialog(activity);
				progressDialog.setMessage(activity.getResources().getString(R.string.loading));
				try {
					progressDialog.show();
				}
				catch (BadTokenException e) {
				}
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run () {
						progressDialog.dismiss();
						Intent scheduleIntent = new Intent(activity, ScheduleActivity.class);
						scheduleIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
						activity.startActivity(scheduleIntent);
					}
				}, 200);

				break;
			/*
			 * case R.id.layout_notifications: Toast.makeText(activity, "Need to implement", Toast.LENGTH_LONG).show();
			 * 
			 * Intent notificationIntent = new Intent(activity, NotificationActivity.class); activity.startActivity(notificationIntent);
			 * 
			 * break;
			 */	case R.id.layout_teams:
					cursor = activity.getContentResolver().query(DataProviderContract.TEAMS_LOGO_TABLE_CONTENTURI, null, null, null, null);
					if (cursor != null && cursor.getCount() > 0) {

						ArrayList <Teams> teamLogoItems = null;
						ArrayList <TeamMember> teamMemberItems = null;

						teamLogoItems = BannerCursor.getTeamLogoItems(cursor);
						
						if (cursor != null) {
							cursor.close();
						}
						cursor = activity.getContentResolver().query(DataProviderContract.TEAM_MEMBERS_TABLE_CONTENTURI, null, null, null, null);
						if (cursor.getCount() > 0) {
							teamMemberItems = BannerCursor.getTeamMemberItems(cursor);

						}
						if (cursor != null) {
							cursor.close();
						}
						if ((teamLogoItems != null && teamLogoItems.size() > 0) && (teamMemberItems != null && teamMemberItems.size() > 0)) {

							callTeamIntent(teamLogoItems, teamMemberItems);

						}
						else {
							Log.e("MenuItems", "Problem Occured While Retriving Team Data From DB ");
						}
					}
					else {
						if(cursor != null){
							cursor.close();
						}

						if (Util.getInstance().isOnline(activity)) {

						Intent	mServiceIntent = new Intent(activity, CCLPullService.class).setData(Uri.parse(activity.getResources().getString(R.string.team_url)));
							activity.startService(mServiceIntent);

							for (int i = 1; i <= 8; i++) {
								mServiceIntent = new Intent(activity, CCLPullService.class).setData(Uri.parse(activity.getResources().getString(R.string.team_members_url) + i));
								mServiceIntent.putExtra("KEY", "team_members");
								activity.startService(mServiceIntent);
							}

						}
						else {
							Toast.makeText(activity, activity.getResources().getString(R.string.network_error_message), Toast.LENGTH_LONG).show();
						}
					}
				break;

			case R.id.layout_ownerslounge:
				progressDialog = new ProgressDialog(activity);
				progressDialog.setMessage(activity.getResources().getString(R.string.loading));
				try {
					progressDialog.show();
				}
				catch (BadTokenException e) {
				}
				new Handler().postDelayed(new Runnable() {

					@Override
					public void run () {
						progressDialog.dismiss();

						Intent ownerIntent = new Intent(activity, OwnersLoungueActivity.class);
						ownerIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
						activity.startActivity(ownerIntent);
					}
				}, 200);

				break;
			case R.id.layout_videos:
				mRequestType = RequestType.VIDEOS_REQUEST;
				cursor = activity.getContentResolver().query(DataProviderContract.VIDEO_ALBUM_TABLE_CONTENTURI, null, null, null, null);
				list = VideoAlbumCursor.getItems(cursor);
				if (list == null || list.size() <= 0) {
					if (Util.getInstance().isOnline(activity)) {
						Intent mServiceIntent = new Intent(activity, CCLPullService.class).setData(Uri.parse(activity.getResources().getString(R.string.photo_album_url)));
						activity.startService(mServiceIntent);
					}
					else {
						Toast.makeText(activity, activity.getResources().getString(R.string.network_error_message), Toast.LENGTH_LONG).show();
					}
				}
				else {
					Intent photoGalleryIntent = new Intent(activity, VideoGalleryActivity.class);
					photoGalleryIntent.putParcelableArrayListExtra(Constants.EXTRA_VIDEO_KEY, list);
					// photoGalleryIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
					activity.startActivity(photoGalleryIntent);
				}

				break;
			case R.id.layout_home:
				if (mLayout.isShown()) {
					cursor = activity.getContentResolver().query(DataProviderContract.BANNERURL_TABLE_CONTENTURI, null, null, null, null);
					if (cursor.getCount() > 0) {
						ArrayList <Items> bannerItems = BannerCursor.getItems(cursor);
						cursor = activity.getContentResolver().query(DataProviderContract.PHOTO_ALBUM_TABLE_CONTENTURI, null, null, null, null);
						ArrayList <Items> photoAlbumItems = PhotoAlbumCurosr.getItems(cursor);
						cursor = activity.getContentResolver().query(DataProviderContract.VIDEO_ALBUM_TABLE_CONTENTURI, null, null, null, null);
						ArrayList <Items> videoAlbumItems = VideoAlbumCursor.getItems(cursor);
						cursor.close();
						callHomeIntent(bannerItems, photoAlbumItems, videoAlbumItems);
					}
					else {
						if (cursor != null) {
							cursor.close();
						}
						if (Util.getInstance().isOnline(activity)) {
							Intent mServiceIntent = new Intent(activity, CCLPullService.class).setData(Uri.parse(activity.getResources().getString(R.string.banner_url)));
							activity.startService(mServiceIntent);

							mServiceIntent = new Intent(activity, CCLPullService.class).setData(Uri.parse(activity.getResources().getString(R.string.photo_album_url)));
							activity.startService(mServiceIntent);

							mServiceIntent = new Intent(activity, CCLPullService.class).setData(Uri.parse(activity.getResources().getString(R.string.video_album_url)));
							activity.startService(mServiceIntent);
						}
						else {
							Toast.makeText(activity, activity.getResources().getString(R.string.network_error_message), Toast.LENGTH_LONG).show();
						}
					}
				}

				break;

			case R.id.layout_news:
				cursor = activity.getContentResolver().query(DataProviderContract.NEWS_TABLE_CONTENTURI, null, DataProviderContract.NEWS_CATEGORY +" = 1", null, null);
				if(cursor != null && cursor.getCount()>0){
					list = NewsItemsCursor.getItems(cursor);
					Intent newsIntent = new Intent(activity, NewsActivity.class);
					newsIntent.putParcelableArrayListExtra(Constants.EXTRA_NEWS_KEY, list);
					//newsIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
					activity.startActivity(newsIntent);
				}else{
					if (Util.getInstance().isOnline(activity)) {
						Intent mServiceIntent = new Intent(activity, CCLPullService.class).setData(Uri.parse(activity.getResources().getString(R.string.news_url)));
						activity.startService(mServiceIntent);			
						
					  mServiceIntent = new Intent(activity, CCLPullService.class).setData(Uri.parse(activity.getResources().getString(R.string.nation_news_url)));
					  activity.startService(mServiceIntent);
					}
					else {
						Toast.makeText(activity, activity.getResources().getString(R.string.network_error_message), Toast.LENGTH_LONG).show();
					}
				}
				break;
				
				
			case R.id.layout_downloads:
				cursor = activity.getContentResolver().query(DataProviderContract.DOWNLOAD_IMAGE_TABLE_CONTENTURI, null, null, null, null);
				System.out.println("cursor lenght"+cursor.getCount());
				if(cursor != null && cursor.getCount()>0){
					list = DownloadItemsCursor.getItems(cursor);
					Intent downloadImageIntent = new Intent(activity, DownloadActivity.class);
					downloadImageIntent.putParcelableArrayListExtra(Constants.EXTRA_DOWNLOAD_KEY, list);
					activity.startActivity(downloadImageIntent);
				}else{
					if (Util.getInstance().isOnline(activity)) {
						Intent mServiceIntent = new Intent(activity, CCLPullService.class).setData(Uri.parse(activity.getResources().getString(R.string.downloads_url)));
						activity.startService(mServiceIntent);
					}
					else {
						Toast.makeText(activity, activity.getResources().getString(R.string.network_error_message), Toast.LENGTH_LONG).show();
					}
				}
			
				
			default:
				break;
		}

	}

	private void callHomeIntent (ArrayList <Items> bannerItems, ArrayList <Items> photoAlbumItems, ArrayList <Items> videoAlbumItems) {
		Intent homeActivityIntent = new Intent(activity, HomeActivity.class);
		homeActivityIntent.putParcelableArrayListExtra(in.ccl.util.Constants.EXTRA_BANNER_KEY, bannerItems);
		homeActivityIntent.putParcelableArrayListExtra(in.ccl.util.Constants.EXTRA_PHOTO_KEY, photoAlbumItems);
		homeActivityIntent.putParcelableArrayListExtra(in.ccl.util.Constants.EXTRA_VIDEO_KEY, videoAlbumItems);
		activity.startActivityForResult(homeActivityIntent, in.ccl.util.Constants.SPLASH_SCREEN_RESULT);
	}

	private void callTeamIntent (ArrayList <Teams> teamLogoItems, ArrayList <TeamMember> teamMemberItems) {
		Intent teamActivityIntent = new Intent(activity, TeamActivity.class);
		teamActivityIntent.putParcelableArrayListExtra(in.ccl.util.Constants.EXTRA_TEAM_LOGO_KEY, teamLogoItems);
		teamActivityIntent.putParcelableArrayListExtra(in.ccl.util.Constants.EXTRA_TEAM_MEMBER_KEY, teamMemberItems);
		activity.startActivityForResult(teamActivityIntent, in.ccl.util.Constants.TEAM_RESULT);
	}
}