package in.ccl.ui;

import in.ccl.helper.Util;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MenuItems implements OnClickListener {

	private static MenuItems singleInstance;

	private Activity activity;

	/**
	 * private constructor
	 */
	private MenuItems() {
	}

	/**
	 * Creating single object of this class. not required to create a new object
	 * each time when it was invoked.
	 * 
	 * @return single object of MenuItems class.
	 */
	public static MenuItems getInstance() {

		if (singleInstance == null) {
			synchronized (Util.class) {
				if (singleInstance == null) {
					singleInstance = new MenuItems();
				}
			}
		}
		return singleInstance;
	}

	public void loadMenu(Activity activity, LinearLayout layout) {
		this.activity = activity;
		// getting menu image button id for invisible button when user in menu
		// screen.
		RelativeLayout layoutPhotos = (RelativeLayout) layout
				.findViewById(R.id.layout_photos);
		RelativeLayout layoutSchedule = (RelativeLayout) layout
				.findViewById(R.id.layout_schedule);
		RelativeLayout layoutNotifications = (RelativeLayout) layout
				.findViewById(R.id.layout_notifications);
		RelativeLayout layoutTeams = (RelativeLayout) layout
				.findViewById(R.id.layout_teams);
		RelativeLayout layoutOwner = (RelativeLayout) layout
				.findViewById(R.id.layout_ownerslounge);
		RelativeLayout layoutHome = (RelativeLayout) layout
				.findViewById(R.id.layout_home);
		RelativeLayout layoutNews = (RelativeLayout) layout
				.findViewById(R.id.layout_news);
		RelativeLayout layoutVideo = (RelativeLayout) layout
				.findViewById(R.id.layout_videos);
		RelativeLayout layoutScore = (RelativeLayout) layout
				.findViewById(R.id.layout_scores);
		RelativeLayout layoutDownloads = (RelativeLayout) layout
				.findViewById(R.id.layout_downloads);

		TextView photoTxt = (TextView) layout.findViewById(R.id.txt_photo);
		TextView newsTxt = (TextView) layout.findViewById(R.id.txt_news);
		TextView homeTxt = (TextView) layout.findViewById(R.id.txt_home);
		TextView ownersTxt = (TextView) layout.findViewById(R.id.txt_owners);
		TextView downloadsTxt = (TextView) layout
				.findViewById(R.id.txt_downloads);
		TextView scheduleTxt = (TextView) layout
				.findViewById(R.id.txt_schedule);
		TextView scoreTxt = (TextView) layout.findViewById(R.id.txt_score);
		TextView teamsTxt = (TextView) layout.findViewById(R.id.txt_tems);
		TextView videosTxt = (TextView) layout.findViewById(R.id.txt_video);
		TextView notificationsTxt = (TextView) layout
				.findViewById(R.id.txt_notifications);

		Util.setTextFont(activity, photoTxt);
		Util.setTextFont(activity, newsTxt);
		Util.setTextFont(activity, homeTxt);
		Util.setTextFont(activity, ownersTxt);
		Util.setTextFont(activity, downloadsTxt);
		Util.setTextFont(activity, scheduleTxt);
		Util.setTextFont(activity, scoreTxt);
		Util.setTextFont(activity, videosTxt);
		Util.setTextFont(activity, notificationsTxt);
		Util.setTextFont(activity, teamsTxt);

		layoutPhotos.setOnClickListener(this);
		layoutSchedule.setOnClickListener(this);
		layoutNotifications.setOnClickListener(this);
		layoutTeams.setOnClickListener(this);
		layoutOwner.setOnClickListener(this);
		layoutVideo.setOnClickListener(this);
		layoutDownloads.setOnClickListener(this);
		layoutHome.setOnClickListener(this);
		layoutScore.setOnClickListener(this);
		layoutNews.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
	
		int key = v.getId();
		switch (key) {
		case R.id.layout_photos:
			Intent photoGalleryIntent = new Intent(activity,
					PhotoGalleryActivity.class);
			activity.startActivity(photoGalleryIntent);
			break;
		case R.id.layout_schedule:
			Intent scheduleIntent = new Intent(activity, ScheduleActivity.class);
			activity.startActivity(scheduleIntent);
			break;
		case R.id.layout_notifications:
			Toast.makeText(activity, "Need to implement", Toast.LENGTH_LONG)
					.show();
			/*
			 * Intent notificationIntent = new Intent(activity,
			 * NotificationActivity.class);
			 * activity.startActivity(notificationIntent);
			 */
			break;
		case R.id.layout_teams:
			Toast.makeText(activity, "Need to implement", Toast.LENGTH_LONG)
					.show();
			/*
			 * Intent teamIntent = new Intent(activity, TeamActivity.class);
			 * activity.startActivity(teamIntent);
			 */
			break;
		case R.id.layout_ownerslounge:
			Intent ownerIntent = new Intent(activity,
					OwnersLoungueActivity.class);
			activity.startActivity(ownerIntent);
			break;
		case R.id.layout_videos:
			Intent videoGalleryIntent = new Intent(activity,
					VideoGalleryActivity.class);
			activity.startActivity(videoGalleryIntent);
			break;
		case R.id.layout_downloads:
			/*
			 * Intent DownloadsIntent = new Intent(activity,
			 * VideoGalleryActivity.class);
			 * activity.startActivity(DownloadsIntent); break;
			 */
		case R.id.layout_news:
			Toast.makeText(activity, "Need to implement", Toast.LENGTH_LONG)
					.show();
			break;
		/*
		 * Intent newsIntent = new Intent(activity, VideoGalleryActivity.class);
		 * activity.startActivity(newsIntent); break;
		 */
		case R.id.layout_home:
			Intent homeIntent = new Intent(activity, HomeActivity.class);
			homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			activity.startActivity(homeIntent);
			break;
		case R.id.layout_scores:
			Toast.makeText(activity, "Need to implement", Toast.LENGTH_LONG)
					.show();
			/*
			 * Intent scoreIntent = new Intent(activity,
			 * VideoGalleryActivity.class); activity.startActivity(scoreIntent);
			 */
			break;

		default:
			break;
		}

	}

}