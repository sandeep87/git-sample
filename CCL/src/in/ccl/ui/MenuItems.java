package in.ccl.ui;

import in.ccl.database.BannerCursor;
import in.ccl.database.CCLPullService;
import in.ccl.database.CalendarItemsCursor;
import in.ccl.database.DataProviderContract;
import in.ccl.database.DownloadItemsCursor;
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
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
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
	private static TextView celebrity_calender;

	private ProgressDialog progressDialog;

	private enum RequestType {
		NO_REQUEST, BANNER_REQUEST, PHOTO_ALBUMREQUEST, TEAMS_REQUEST, TEAMROLE_REQUEST, VIDEOS_REQUEST, PHOTOS_REQUEST, OWNERS_LOUNGE_REQUEST, SCHEDULE_REQUEST, VIDEO_ALBUMREQUEST, NOTIFICATIONS_REQUEST, DOWNLOADS_REQUEST, TEAM_MEMBERS_REQUEST, REGIONAL_REQUEST;
	}

	RequestType mRequestType = RequestType.NO_REQUEST;

	/**
	 * private constructor
	 */
	public MenuItems () {
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

		RelativeLayout layoutVideo = (RelativeLayout) layout.findViewById(R.id.layout_videos);
		RelativeLayout layoutLiveScore = (RelativeLayout) layout.findViewById(R.id.layout_live_score);
		RelativeLayout layoutdownloads = (RelativeLayout) layout.findViewById(R.id.layout_downloads);

		RelativeLayout layoutcalender = (RelativeLayout) layout.findViewById(R.id.layout_calender);

		TextView photoTxt = (TextView) layout.findViewById(R.id.txt_photo);

		TextView homeTxt = (TextView) layout.findViewById(R.id.txt_home);
		TextView ownersTxt = (TextView) layout.findViewById(R.id.txt_owners);
		TextView downloadsTxt = (TextView) layout.findViewById(R.id.txt_downloads);
		blinkMenuText(downloadsTxt);
		TextView calenderTxt = (TextView) layout.findViewById(R.id.txt_celebrity_calender);
		blinkMenuText(calenderTxt);
		TextView scheduleTxt = (TextView) layout.findViewById(R.id.txt_schedule);
		TextView liveScoreTxt = (TextView) layout.findViewById(R.id.txt_livescore);
		TextView teamsTxt = (TextView) layout.findViewById(R.id.txt_tems);
		TextView videosTxt = (TextView) layout.findViewById(R.id.txt_video);
		// TextView notificationsTxt = (TextView) layout.findViewById(R.id.txt_notifications);
		Util.setTextFont(activity, photoTxt);

		Util.setTextFont(activity, homeTxt);
		Util.setTextFont(activity, ownersTxt);
		Util.setTextFont(activity, downloadsTxt);
		Util.setTextFont(activity, calenderTxt);
		Util.setTextFont(activity, scheduleTxt);
		Util.setTextFont(activity, liveScoreTxt);
		Util.setTextFont(activity, videosTxt);
		// Util.setTextFont(activity, notificationsTxt);
		Util.setTextFont(activity, teamsTxt);

		layoutPhotos.setOnClickListener(this);
		layoutSchedule.setOnClickListener(this);
		// layoutNotifications.setOnClickListener(this);
		layoutTeams.setOnClickListener(this);
		layoutOwner.setOnClickListener(this);
		layoutVideo.setOnClickListener(this);
		layoutcalender.setOnClickListener(this);
		layoutdownloads.setOnClickListener(this);
		layoutHome.setOnClickListener(this);

		layoutLiveScore.setOnClickListener(this);

		// layoutScore.setOnClickListener(this);

		/*
		 * chennaiTeamMembersList = Util.getInstance().getChnnaiTeamMembersList(); teluguTeamMembersList = Util.getInstance().getTeluguWarriorsTeamMembersList(); karnatakaTeamMembersList = Util.getInstance().getKarnatakaTeamMembersList(); keralaTeamMembersList =
		 * Util.getInstance().getKeralaTeamMembersList(); bengalTeamMembersList = Util.getInstance().getBangalTeamMembersList(); marathiTeamMembersList = Util.getInstance().getMarathiTeamMembersList(); bhojpuriTeamMembersList = Util.getInstance().getBhojpuriTeamMembersList();
		 */}

	private void blinkMenuText (TextView txt) {
		Animation anim = new AlphaAnimation(0.0f, 1.0f);
		anim.setDuration(80); // You can manage the time of the blink with this parameter
		anim.setStartOffset(20);
		anim.setRepeatMode(Animation.REVERSE);
		anim.setRepeatCount(Animation.INFINITE);
		txt.startAnimation(anim);
		Util.setTextFont(activity, txt);

	}

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
					photoGalleryIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
					// photoGalleryIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
						if (progressDialog != null) {
							try {
								progressDialog.dismiss();
							}
							catch (IllegalStateException e) {
								// TODO: handle exception
							}
						}
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
			 */case R.id.layout_teams:
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
					if (cursor != null) {
						cursor.close();
					}
					if (Util.getInstance().isOnline(activity)) {
						Intent mServiceIntent = new Intent(activity, CCLPullService.class).setData(Uri.parse(activity.getResources().getString(R.string.team_url)));
						activity.startService(mServiceIntent);
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
						if (progressDialog != null) {
							try {
								progressDialog.dismiss();
							}
							catch (IllegalStateException e) {
								// TODO: handle exception
							}
						}

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
					photoGalleryIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
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
			case R.id.layout_downloads:

				cursor = activity.getContentResolver().query(DataProviderContract.DOWNLOAD_IMAGE_TABLE_CONTENTURI, null, null, null, null);
				if (cursor != null && cursor.getCount() > 0) {
					DownloadItemsCursor downloadItemsCursor = new DownloadItemsCursor();
					list = downloadItemsCursor.getItems(cursor);
					Intent downloadImageIntent = new Intent(activity, DownloadActivity.class);
					downloadImageIntent.putParcelableArrayListExtra(Constants.EXTRA_DOWNLOAD_KEY, list);
					downloadImageIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

					activity.startActivity(downloadImageIntent);
				}
				else {
					if (Util.getInstance().isOnline(activity)) {
						Intent mServiceIntent = new Intent(activity, CCLPullService.class).setData(Uri.parse(activity.getResources().getString(R.string.downloads_url)));
						activity.startService(mServiceIntent);
					}
					else {
						Toast.makeText(activity, activity.getResources().getString(R.string.network_error_message), Toast.LENGTH_LONG).show();
					}
				}
				break;
			case R.id.layout_live_score:
				Intent liveScoreIntent = new Intent(activity, LiveScoreActivity.class);
				activity.startActivity(liveScoreIntent);
				break;

			case R.id.layout_calender:
				cursor = activity.getContentResolver().query(DataProviderContract.CALENDAR_IMAGE_TABLE_CONTENTURI, null, null, null, null);

				if (cursor != null && cursor.getCount() > 0) {
					CalendarItemsCursor mCalendarItemsCursor = new CalendarItemsCursor();
					list = mCalendarItemsCursor.getItems(cursor);
					Intent calendarImageIntent = new Intent(activity, CalendarActivity.class);
					calendarImageIntent.putParcelableArrayListExtra(Constants.EXTRA_CALENDAR_KEY, list);
					calendarImageIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

					activity.startActivity(calendarImageIntent);
				}
				else {
					if (Util.getInstance().isOnline(activity)) {

						Intent mServiceIntent = new Intent(activity, CCLPullService.class).setData(Uri.parse(activity.getResources().getString(R.string.calender_url)));
						activity.startService(mServiceIntent);
					}
					else {
						Toast.makeText(activity, activity.getResources().getString(R.string.network_error_message), Toast.LENGTH_LONG).show();
					}
				}
				break;
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
		teamActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		activity.startActivityForResult(teamActivityIntent, in.ccl.util.Constants.TEAM_RESULT);
	}
}