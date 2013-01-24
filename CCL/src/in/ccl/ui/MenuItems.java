package in.ccl.ui;

import in.ccl.database.CCLDAO;
import in.ccl.helper.AnimationLayout;
import in.ccl.helper.ServerResponse;
import in.ccl.helper.Util;
import in.ccl.model.Items;
import in.ccl.net.CCLService;
import in.ccl.net.DownLoadAsynTask;
import in.ccl.util.Constants;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager.BadTokenException;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MenuItems implements OnClickListener, ServerResponse {

	private static MenuItems singleInstance;

	private Activity activity;

	private AnimationLayout mLayout;

	private ArrayList <Items> bannerList;

	private ArrayList <Items> photoGalleryList;

	private ArrayList <Items> videoGalleryList;

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
		NO_REQUEST, BANNER_REQUEST, PHOTO_ALBUMREQUEST, TEAMS_REQUEST, TEAMROLE_REQUEST, VIDEOS_REQUEST, PHOTOS_REQUEST, OWNERS_LOUNGE_REQUEST, SCHEDULE_REQUEST, VIDEO_ALBUMREQUEST, NOTIFICATIONS_REQUEST, DOWNLOADS_REQUEST, TEAM_MEMBERS_REQUEST;
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
		// RelativeLayout layoutNews = (RelativeLayout) layout.findViewById(R.id.layout_news);
		RelativeLayout layoutVideo = (RelativeLayout) layout.findViewById(R.id.layout_videos);
		// RelativeLayout layoutScore = (RelativeLayout) layout.findViewById(R.id.layout_scores);
		// RelativeLayout layoutDownloads = (RelativeLayout) layout.findViewById(R.id.layout_downloads);

		TextView photoTxt = (TextView) layout.findViewById(R.id.txt_photo);
		// TextView newsTxt = (TextView) layout.findViewById(R.id.txt_news);
		TextView homeTxt = (TextView) layout.findViewById(R.id.txt_home);
		TextView ownersTxt = (TextView) layout.findViewById(R.id.txt_owners);
		// TextView downloadsTxt = (TextView) layout.findViewById(R.id.txt_downloads);
		TextView scheduleTxt = (TextView) layout.findViewById(R.id.txt_schedule);
		// TextView scoreTxt = (TextView) layout.findViewById(R.id.txt_score);
		TextView teamsTxt = (TextView) layout.findViewById(R.id.txt_tems);
		TextView videosTxt = (TextView) layout.findViewById(R.id.txt_video);
		// TextView notificationsTxt = (TextView) layout.findViewById(R.id.txt_notifications);

		Util.setTextFont(activity, photoTxt);
		// Util.setTextFont(activity, newsTxt);
		Util.setTextFont(activity, homeTxt);
		Util.setTextFont(activity, ownersTxt);
		// Util.setTextFont(activity, downloadsTxt);
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
		// layoutDownloads.setOnClickListener(this);
		layoutHome.setOnClickListener(this);
		// layoutScore.setOnClickListener(this);
		// layoutNews.setOnClickListener(this);

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
				ArrayList <Items> list = CCLDAO.getPhotoGallery();
				if (list == null || list.size() <= 0) {
					if (Util.getInstance().isOnline(activity)) {
						DownLoadAsynTask asyncTask = new DownLoadAsynTask(activity, MenuItems.this, false);
						asyncTask.execute(activity.getResources().getString(R.string.photo_album_url));
					}
					else {
						Toast.makeText(activity, activity.getResources().getString(R.string.network_error_message), Toast.LENGTH_LONG).show();
					}
				}
				else {
					Intent photoGalleryIntent = new Intent(activity, PhotoGalleryActivity.class);
					photoGalleryIntent.putParcelableArrayListExtra(Constants.EXTRA_PHOTO_KEY, list);
					photoGalleryIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
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
			 */case R.id.layout_teams:
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
						Intent teamIntent = new Intent(activity, TeamActivity.class);
						teamIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
						activity.startActivity(teamIntent);
					}
				}, 200);

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
				if (Util.getInstance().isOnline(activity)) {
					DownLoadAsynTask asyncTask = new DownLoadAsynTask(activity, MenuItems.this, false);
					asyncTask.execute(activity.getResources().getString(R.string.video_album_url));
				}
				else {
					Toast.makeText(activity, activity.getResources().getString(R.string.network_error_message), Toast.LENGTH_LONG).show();
				}

				break;
			case R.id.layout_home:
				if (mLayout.isShown()) {
					if (Util.getInstance().isOnline(activity)) {
						DownLoadAsynTask downLoadAsyncTask = new DownLoadAsynTask(activity, this, false);
						mRequestType = RequestType.BANNER_REQUEST;
						downLoadAsyncTask.execute(activity.getResources().getString(R.string.banner_url));
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

	@Override
	public void setData (String result) {
		if (result != null) {
			switch (mRequestType) {
				case NO_REQUEST:
					mRequestType = RequestType.NO_REQUEST;
					break;
				case BANNER_REQUEST:
					// parsing server banner items response.
					bannerList = CCLService.getBannerItems(result);
					if (Util.getInstance().isOnline(activity)) {
						// for downloading photo album data.
						DownLoadAsynTask asyncTask = new DownLoadAsynTask(activity, this, true);
						mRequestType = RequestType.PHOTO_ALBUMREQUEST;
						asyncTask.execute(activity.getResources().getString(R.string.photo_album_url));
					}
					else {
						Toast.makeText(activity, activity.getResources().getString(R.string.network_error_message), Toast.LENGTH_LONG).show();
					}

					break;
				case PHOTO_ALBUMREQUEST:
					// parsing server photo album responose.
					photoGalleryList = CCLService.getPhotoAlbums(result);
					if (Util.getInstance().isOnline(activity)) {
						// for downloading video albums.
						DownLoadAsynTask asyncTask = new DownLoadAsynTask(activity, this, true);
						mRequestType = RequestType.VIDEO_ALBUMREQUEST;
						asyncTask.execute(activity.getResources().getString(R.string.video_album_url));
					}
					else {
						Toast.makeText(activity, activity.getResources().getString(R.string.network_error_message), Toast.LENGTH_LONG).show();
					}

					break;
				case VIDEO_ALBUMREQUEST:
					// parsing video album response.
					videoGalleryList = CCLService.getVideoAlbums(result);
					// to finish the animation, it will be executed in onAnimation end method.
					Intent homeActivityIntent = new Intent(activity, HomeActivity.class);
					homeActivityIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					homeActivityIntent.putParcelableArrayListExtra(Constants.EXTRA_BANNER_KEY, bannerList);
					homeActivityIntent.putParcelableArrayListExtra(Constants.EXTRA_PHOTO_KEY, photoGalleryList);
					homeActivityIntent.putParcelableArrayListExtra(Constants.EXTRA_VIDEO_KEY, videoGalleryList);
					activity.startActivityForResult(homeActivityIntent, in.ccl.util.Constants.SPLASH_SCREEN_RESULT);
					break;
				case PHOTOS_REQUEST:
					Intent photoGalleryIntent = new Intent(activity, PhotoGalleryActivity.class);
					photoGalleryIntent.putParcelableArrayListExtra(Constants.EXTRA_PHOTO_KEY, CCLService.getPhotoAlbums(result));
					photoGalleryIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
					activity.startActivity(photoGalleryIntent);
					break;
				case VIDEOS_REQUEST:
					Intent videoGalleryIntent = new Intent(activity, VideoGalleryActivity.class);
					videoGalleryIntent.putParcelableArrayListExtra(Constants.EXTRA_VIDEO_KEY, CCLService.getVideoAlbums(result));
					videoGalleryIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

					activity.startActivity(videoGalleryIntent);
					break;
				default:
					break;
			}
		}
		else {
			// No network connection.

			Toast.makeText(activity, activity.getResources().getString(R.string.network_error_message), Toast.LENGTH_LONG).show();

		}

	}

}