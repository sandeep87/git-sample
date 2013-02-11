package in.ccl.ui;

import in.ccl.adapters.LiveScoreSlidingDrawer;
import in.ccl.database.BannerCursor;
import in.ccl.database.DataProviderContract;
import in.ccl.database.DownloadItemsCursor;
import in.ccl.helper.AnimationLayout;
import in.ccl.helper.ServerResponse;
import in.ccl.helper.Util;
import in.ccl.livescore.service.LiveScoreService;
import in.ccl.model.Items;
import in.ccl.model.TeamMember;
import in.ccl.model.Teams;
import in.ccl.photo.PhotoView;
import in.ccl.score.LiveScore;
import in.ccl.score.MatchesResponse;
import in.ccl.score.ScoreBoard;
import in.ccl.util.Constants;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.ads.AdRequest;
import com.google.ads.AdView;

public class TopActivity extends Activity implements AnimationLayout.Listener, ServerResponse, OnClickListener {

	// used as key of the logs.
	private static final String TAG = "MainActivity";

	// used to add inner views from the calling activity to top activity.
	private RelativeLayout layoutContent;

	// dropdown selection button from header layout.
	private ImageButton imgBtnScoreDropDown;

	private RelativeLayout scoreLayout;

	private TextView txtScore;

	private TextView txtScoreTitle;

	private LinearLayout menuLayout;

	private DownloadStateReceiver mDownloadStateReceiver;

	private IntentFilter statusIntentFilter;

	private ImageView first_score_image_position;

	private ImageView second_score_image_position;

	private ImageView third_score_image_position;

	private ImageView first_wicket_image_position;

	private ImageView second_wicket_image_position;

	private TextView target_score;

	private TextView second_innings_overs;

	private TextView striker_name;

	private TextView striker_runs_batsman;

	private TextView striker_balls;

	private TextView non_striker_batsman;

	private TextView non_striker_runs_batsman;

	private TextView non_striker_balls;

	private TextView current_bowler_name;

	private TextView current_bowler_overs;

	private TextView current_bowler_runs;

	private TextView current_bowler_wkts;

	private TextView current_bowler_mnds;

	private TextView previous_bowler_name;

	private TextView previous_bowler_overs;

	private TextView previous_bowler_runs;

	private TextView previous_bowler_wkts;

	private TextView previous_bowler_mnds;

	private TextView striker_strike_rate;

	private TextView non_striker_strike_rate;

	public LiveScoreSlidingDrawer mDrawer;

	private ArrayList <MatchesResponse> matcheslist;

	private LinearLayout adsLayout;

	private TextView txtScoreHeader;

	private TextView txtCurrentScore;

	private static String mCurrentScore;

	private PhotoView battingLogo;

	private AnimationLayout mLayout;

	private Button scoreBoard_btn;

	private int currentMatchId;

	private static boolean isTopHeaderSelected;

	public static boolean isTopHeaderSelected () {
		return isTopHeaderSelected;
	}

	public static void setTopHeaderSelected (boolean isTopHeaderSelected) {
		TopActivity.isTopHeaderSelected = isTopHeaderSelected;
	}

	public static String getCurrentScore () {
		return mCurrentScore;
	}

	public static void setCurrentScore (String mCurrentScore) {
		TopActivity.mCurrentScore = mCurrentScore;
	}

	private static boolean isCurrentScoreTimerStarted;

	public static boolean isCurrentScoreTimerStarted () {
		return isCurrentScoreTimerStarted;
	}

	public static void setCurrentScoreTimerStarted (boolean isCurrentScoreTimerStarted) {
		TopActivity.isCurrentScoreTimerStarted = isCurrentScoreTimerStarted;
	}

	public LiveScoreSlidingDrawer getDrawer () {
		return mDrawer;
	}

	// for displaying adds.
	private AdView adView;

	@Override
	public void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_layout);
		txtScoreHeader = (TextView) findViewById(R.id.score_title_textview);
		txtCurrentScore = (TextView) findViewById(R.id.score_textview);
		txtScore = (TextView) findViewById(R.id.score_textview);
		txtScoreTitle = (TextView) findViewById(R.id.score_title_textview);
		imgBtnScoreDropDown = (ImageButton) findViewById(R.id.imgbtn_score_dropdown);
		layoutContent = (RelativeLayout) findViewById(R.id.layout_content);
		scoreLayout = (RelativeLayout) findViewById(R.id.header);
		mLayout = (AnimationLayout) findViewById(R.id.animation_layout);
		mDrawer = (LiveScoreSlidingDrawer) findViewById(R.id.drawer);
		menuLayout = (LinearLayout) findViewById(R.id.menu_layout);

		// The filter's action is BROADCAST_ACTION
		statusIntentFilter = new IntentFilter(Constants.BROADCAST_ACTION);

		// Sets the filter's category to DEFAULT
		statusIntentFilter.addCategory(Intent.CATEGORY_DEFAULT);

		// Instantiates a new DownloadStateReceiver
		mDownloadStateReceiver = new DownloadStateReceiver();

		// for adds
		adView = (AdView) findViewById(R.id.adMob);
		adsLayout = (LinearLayout) findViewById(R.id.admob_layout);
		adsLayout.setVisibility(View.VISIBLE);
		// Initiate a generic request to load it with an ad
		// AdRequest adRequest = new AdRequest();
		// adRequest.addTestDevice(AdRequest.TEST_EMULATOR); // Emulator
		// adView.loadAd(adRequest);// new AdRequest()
		adView.loadAd(new AdRequest());
		// for user menu selection from top activity.
		ImageButton imgBtnMenu = (ImageButton) findViewById(R.id.img_btn_menu);

		// setting menu title font
		TextView menuTitleTxt = (TextView) findViewById(R.id.menu_title);
		Util.setTextFont(this, menuTitleTxt);
		Util.setTextFont(this, txtScore);
		Util.setTextFont(this, txtScoreTitle);
		// menu click lister, should start menu items activity.
		imgBtnMenu.setOnClickListener(new OnClickListener() {

			public void onClick (View v) {
				mLayout.toggleSidebar();
			}
		});
		// if layout select for score view should start animation.
		// click event to visible and invisible of score view.
		scoreLayout.setOnClickListener(new OnClickListener() {

			public void onClick (View v) {
				if (!isTopHeaderSelected()) {
					setTopHeaderSelected(true);
					showOrHideLiveScore();
				}
			}
		});

		imgBtnScoreDropDown.setOnClickListener(new OnClickListener() {

			public void onClick (View v) {
				if (!isTopHeaderSelected()) {
					setTopHeaderSelected(true);
					showOrHideLiveScore();
				}
			}
		});
		MenuItems.getInstance().loadMenu(this, menuLayout, mLayout);

	}

	protected void cancleUpdateLiveScore () {
		Intent mServiceIntent = new Intent(TopActivity.this, LiveScoreService.class).setData(Uri.parse(getResources().getString(R.string.live_score_url)));
		AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		PendingIntent pendingIntent = PendingIntent.getService(TopActivity.this, 0, mServiceIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		alarmManager.cancel(pendingIntent);
	}

	private void showOrHideLiveScore () {
		if (!mDrawer.isOpened()) {
			if (!(txtCurrentScore.getText().equals(getResources().getString(R.string.app_title)))) {
				Intent mServiceIntent = new Intent(TopActivity.this, LiveScoreService.class).setData(Uri.parse(getResources().getString(R.string.live_matches_urls)));
				startService(mServiceIntent);
			}
		}
		else {
			cancleUpdateLiveScore();
			showCurrentHeader();
			mDrawer.animateClose();
		}

	}

	@Override
	protected void onStop () {
		super.onStop();
		mCurrentScore = null;
		setCurrentScoreTimerStarted(false);
	}

	private void callLiveScoreService (int liveMatchId) {
		currentMatchId = liveMatchId;
		Intent mServiceIntent = new Intent(TopActivity.this, LiveScoreService.class).setData(Uri.parse(getResources().getString(R.string.live_score_url) + liveMatchId));
		mServiceIntent.putExtra("KEY", "livescore");
		startService(mServiceIntent);

	}

	@Override
	protected void onPause () {
		LocalBroadcastManager.getInstance(this).unregisterReceiver(mDownloadStateReceiver);
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run () {
				synchronized (this) {
					if (mLayout.isOpening()) {
						mLayout.closeSidebar();
					}
				}
			}
		}, 300);
		super.onPause();

	}

	@Override
	public void onDestroy () {
		if (adView != null) {
			adView.destroy();
		}
		super.onDestroy();
	}

	@Override
	public void onBackPressed () {
		cancleUpdateLiveScore();
		showCurrentHeader();
		if (mLayout.isOpening()) {
			mLayout.closeSidebar();
		}
		else if (mDrawer.isOpened()) {
			mDrawer.animateClose();
		}
		else {
			finish();
		}
	}

	/**
	 * Used to inflate required view in to content part of the top layout. it will take layout resource id otherwise throw an exception.
	 * 
	 * @param resourceId int
	 */
	public void addContent (int resourceId) {
		LayoutInflater inflate = LayoutInflater.from(this);
		try {
			View view = inflate.inflate(resourceId, null);
			layoutContent.addView(view, new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
		}
		catch (InflateException e) {
			Log.e(TAG, "Invalide resource id provided for adding content.");
		}
	}

	@Override
	public void onSidebarOpened () {
		// animationLayoutSlider.setVisibility(View.VISIBLE);
	}

	@Override
	public void onSidebarClosed () {
		// animationLayoutSlider.setVisibility(View.GONE);
	}

	@Override
	public boolean onContentTouchedWhenOpening () {
		mLayout.closeSidebar();
		// animationLayoutSlider.setVisibility(View.GONE);
		return true;

	}

	@Override
	protected void onResume () {
		super.onResume();
		LocalBroadcastManager.getInstance(this).registerReceiver(mDownloadStateReceiver, statusIntentFilter);
		showCurrentHeader();
		if (!isCurrentScoreTimerStarted) {
			// send request to get live matches schedule
			Intent mServiceIntent = new Intent(this, LiveScoreService.class).setData(Uri.parse(getResources().getString(R.string.match_schedule_url)));
			startService(mServiceIntent);
		}
	}

	private void showCurrentHeader () {
		if (mCurrentScore == null) {
			txtScoreHeader.setVisibility(View.GONE);
			imgBtnScoreDropDown.setVisibility(View.GONE);
			txtCurrentScore.setText(getResources().getString(R.string.app_title));
			txtCurrentScore.setGravity((Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL));
		}
		else {
			txtScoreHeader.setVisibility(View.VISIBLE);
			txtScoreHeader.setText("Score : ");
			imgBtnScoreDropDown.setVisibility(View.VISIBLE);
			imgBtnScoreDropDown.setBackgroundResource(R.drawable.dropdown);
			txtCurrentScore.setGravity(Gravity.LEFT);
			txtCurrentScore.setText(mCurrentScore);
		}
	}

	class DownloadStateReceiver extends BroadcastReceiver {

		DownloadStateReceiver () {
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

				case in.ccl.database.Constants.STATE_ACTION_DOWNLOAD_IMAGE_COMPLETE:

					Cursor cursor = getContentResolver().query(DataProviderContract.DOWNLOAD_IMAGE_TABLE_CONTENTURI, null, null, null, null);
					ArrayList <Items> downloadImageItems = DownloadItemsCursor.getItems(cursor);
					if (cursor != null) {
						cursor.close();
					}
					Intent downloadImageIntent = new Intent(TopActivity.this, DownloadActivity.class);
					if (downloadImageItems.size() > 0 && downloadImageItems != null) {
						downloadImageIntent.putParcelableArrayListExtra(Constants.EXTRA_DOWNLOAD_KEY, downloadImageItems);
						downloadImageIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
						startActivity(downloadImageIntent);
					}
					break;
				case in.ccl.database.Constants.STATE_ACTION_TEAM_MEMBERS_COMPLETE:
					ArrayList <Teams> teamLogoItems = null;
					ArrayList <TeamMember> teamMemberItems = null;

					cursor = getContentResolver().query(DataProviderContract.TEAMS_LOGO_TABLE_CONTENTURI, null, null, null, null);
					if (cursor.getCount() > 0) {
						teamLogoItems = BannerCursor.getTeamLogoItems(cursor);
					}
					if (cursor != null) {
						cursor.close();
					}
					cursor = getContentResolver().query(DataProviderContract.TEAM_MEMBERS_TABLE_CONTENTURI, null, null, null, null);
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
						Log.e(TAG, "Team Data is not availble");
					}
					break;
				case in.ccl.database.Constants.STATE_CURRENT_SCORE_TASK_COMPLETED:
					if (intent != null && intent.hasExtra("current_score")) {
						String currentScore = intent.getStringExtra("current_score");
						mCurrentScore = currentScore;

						if (currentScore == null && !mDrawer.isOpened()) {
							if (txtScoreHeader != null && imgBtnScoreDropDown != null && txtCurrentScore != null) {
								txtScoreHeader.setVisibility(View.GONE);
								imgBtnScoreDropDown.setVisibility(View.GONE);
								txtCurrentScore.setText(getResources().getString(R.string.app_title));
								txtCurrentScore.setGravity((Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL));
							}
						}
						else if (!mDrawer.isOpened()) {
							if (txtScoreHeader != null && imgBtnScoreDropDown != null && txtCurrentScore != null) {
								txtScoreHeader.setVisibility(View.VISIBLE);
								txtScoreHeader.setText("Score : ");
								imgBtnScoreDropDown.setVisibility(View.VISIBLE);
								imgBtnScoreDropDown.setBackgroundResource(R.drawable.dropdown);
								txtCurrentScore.setText(currentScore);
								txtCurrentScore.setGravity(Gravity.LEFT);
							}
						}
					}
					break;
				case in.ccl.database.Constants.STATE_MATCHES_TASK_COMPLETED:
					if (intent != null && intent.hasExtra("matches_list")) {
						matcheslist = intent.getParcelableArrayListExtra("matches_list");
						if (matcheslist != null) {
							if (matcheslist.size() > 0) {
								callLiveScoreService(matcheslist.get(0).getId());
							}
						}
					}
					break;
				case in.ccl.database.Constants.STATE_LIVE_SCORE_TASK_COMPLETED:
					if (intent != null && intent.hasExtra("livescore")) {
						LiveScore liveScore = intent.getParcelableExtra("livescore");
						if (imgBtnScoreDropDown != null) {
							imgBtnScoreDropDown.setBackgroundResource(R.drawable.dropdown_up);
						}
						addLiveScoreView();
						displayLiveScore(liveScore);
						setTopHeaderSelected(false);
						if (mDrawer != null) {
							mDrawer.animateOpen();
						}
					}
					break;
				case in.ccl.database.Constants.STATE_LIVE_SCORE_UPDATE_TASK_COMPLETED:
					if (intent != null && intent.hasExtra("livescore")) {
						LiveScore liveScore = intent.getParcelableExtra("livescore");
						displayLiveScore(liveScore);
					}
					break;
				case in.ccl.database.Constants.STATE_LIVE_SCOREBOARD_TASK_COMPLETED:
					if (intent != null && intent.hasExtra("scoreboard")) {
						ScoreBoard scoreBoard = intent.getParcelableExtra("scoreboard");

						if (scoreBoard != null) {
							Intent scoreBoardIntent = new Intent(TopActivity.this, ScoreBoardActivity.class);
							scoreBoardIntent.putExtra("scoreboard", scoreBoard);
							scoreBoardIntent.putExtra("match_id", currentMatchId);
							scoreBoardIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
							startActivity(scoreBoardIntent);
						}

					}

				default:
					break;
			}
		}
	}

	public void disableAds () {
		if (adsLayout != null) {
			adsLayout.setVisibility(View.GONE);
		}
	}

	private void callTeamIntent (ArrayList <Teams> teamLogoItems, ArrayList <TeamMember> teamMemberItems) {
		Intent teamActivityIntent = new Intent(this, TeamActivity.class);
		teamActivityIntent.putParcelableArrayListExtra(in.ccl.util.Constants.EXTRA_TEAM_LOGO_KEY, teamLogoItems);
		teamActivityIntent.putParcelableArrayListExtra(in.ccl.util.Constants.EXTRA_TEAM_MEMBER_KEY, teamMemberItems);
		startActivityForResult(teamActivityIntent, in.ccl.util.Constants.TEAM_RESULT);
	}

	private void addLiveScoreView () {
		target_score = (TextView) findViewById(R.id.team_score);

		battingLogo = (PhotoView) findViewById(R.id.batting_logo);
		battingLogo.setImageDrawable(getResources().getDrawable(R.drawable.imagenotqueued));
		first_score_image_position = (ImageView) findViewById(R.id.score_first_position);

		second_score_image_position = (ImageView) findViewById(R.id.score_second_position);

		third_score_image_position = (ImageView) findViewById(R.id.score_third_position);

		first_wicket_image_position = (ImageView) findViewById(R.id.first_wicket_img);

		second_wicket_image_position = (ImageView) findViewById(R.id.second_wicket_img);

		second_innings_overs = (TextView) findViewById(R.id.second_inning_overs);

		striker_name = (TextView) findViewById(R.id.striker_batsman);

		striker_runs_batsman = (TextView) findViewById(R.id.striker_runs_batsman);

		striker_balls = (TextView) findViewById(R.id.striker_balls);

		non_striker_batsman = (TextView) findViewById(R.id.non_striker_batsman);

		non_striker_runs_batsman = (TextView) findViewById(R.id.non_striker_runs_batsman);

		non_striker_balls = (TextView) findViewById(R.id.non_striker_balls);

		current_bowler_name = (TextView) findViewById(R.id.bowler_statics);

		current_bowler_overs = (TextView) findViewById(R.id.bowler_overs);

		current_bowler_runs = (TextView) findViewById(R.id.bowler_runs);

		current_bowler_wkts = (TextView) findViewById(R.id.bowler_wkts);

		current_bowler_mnds = (TextView) findViewById(R.id.bowler_mdns);

		previous_bowler_name = (TextView) findViewById(R.id.non_bowler_statics);

		previous_bowler_overs = (TextView) findViewById(R.id.non_bowler_overs);

		previous_bowler_runs = (TextView) findViewById(R.id.non_bowler_runs);

		previous_bowler_wkts = (TextView) findViewById(R.id.non_bowler_wkts);

		previous_bowler_mnds = (TextView) findViewById(R.id.non_bowler_mdns);

		scoreBoard_btn = (Button) findViewById(R.id.btn_view_score_board);

		scoreBoard_btn.setOnClickListener(this);

		striker_strike_rate = (TextView) findViewById(R.id.striker_strike_rate);

		non_striker_strike_rate = (TextView) findViewById(R.id.non_striker_strike_rate);

	}

	private void displayLiveScore (LiveScore liveScore) {
		// allow user to select again

		if (target_score != null) {
			if (liveScore.getTarget_score() == 0) {
				target_score.setVisibility(View.GONE);
			}
			else {
				String first_inning_overs = "(" + liveScore.getTarget_overs() + " Overs" + ")";
				target_score.setText("Target  :  " + liveScore.getTarget_score() +" "+ first_inning_overs + "" + "\n" + (liveScore.getNeed_score() == null ? "" : "\n" + "SCORE : " + liveScore.getNeed_score()));
			}
			TextView txtErrrorMessage = new TextView(this);

			battingLogo.setScaleType(ImageView.ScaleType.MATRIX);
			if (liveScore.getTeamLogo() != null) {
				battingLogo.setImageURL(liveScore.getTeamLogo(), true, getResources().getDrawable(R.drawable.photo_imagenotqueued), txtErrrorMessage, false);
			}
			else {
				battingLogo.setVisibility(View.INVISIBLE);
			}
			updateScore(liveScore.getCurrent_score_score());

			updateWickets(liveScore.getCurrent_score_wickets());
			second_innings_overs.setText(liveScore.getCurrent_score_overs() + "");

			striker_name.setText((liveScore.getStriker_name() == null ? "" : liveScore.getStriker_name()));

			striker_runs_batsman.setText(liveScore.getStriker_score() + "");

			striker_balls.setText(liveScore.getStriker_balls() + "");

			non_striker_batsman.setText((liveScore.getNonstriker_name() == null ? "" : liveScore.getNonstriker_name()));

			non_striker_runs_batsman.setText(liveScore.getNonstriker_score() + "");

			non_striker_balls.setText(liveScore.getNonstriker_balls() + "");

			current_bowler_name.setText((liveScore.getCurrent_bowler_name() == null ? "" : liveScore.getCurrent_bowler_name()));

			current_bowler_overs.setText(liveScore.getCurrent_bowler_overs() + "");

			current_bowler_runs.setText(liveScore.getCurrent_bowler_runs() + "");

			current_bowler_wkts.setText(liveScore.getCurrent_bowler_wickets() + "");

			current_bowler_mnds.setText(liveScore.getCurrent_bowler_madiens() + "");

			previous_bowler_name.setText((liveScore.getPrevious_bowler_name() == null ? "" : liveScore.getPrevious_bowler_name()));

			previous_bowler_runs.setText(liveScore.getPrevious_bowler_runs() + "");

			previous_bowler_overs.setText(liveScore.getPrevious_bowler_overs() + "");

			previous_bowler_wkts.setText(liveScore.getPrevious_bowler_wickets() + "");

			previous_bowler_mnds.setText(liveScore.getPrevious_bowler_madiens() + "");
			striker_strike_rate.setText(liveScore.getStriker_strikerate() != 0 ? String.format("%.2f", liveScore.getStriker_strikerate()) : "0");
			non_striker_strike_rate.setText(liveScore.getNonstriker_strikerate() != 0 ? String.format("%.2f", liveScore.getNonstriker_strikerate()) : "0");

			if (mDrawer.isOpened()) {
				txtScoreTitle.setVisibility(View.GONE);
				imgBtnScoreDropDown.setVisibility(View.VISIBLE);
				imgBtnScoreDropDown.setBackgroundResource(R.drawable.dropdown_up);
				if (liveScore.getTeam1() != null && liveScore.getTeam2() != null) {
					txtScore.setText(liveScore.getTeam1() + " vs " + liveScore.getTeam2());
				}
				else {
					if (mCurrentScore != null) {
						txtScore.setText(mCurrentScore);
					}
					else {
						txtScore.setText(getResources().getString(R.string.app_title));
					}
				}
			}
		}
	}

	@Override
	public void setData (String result) {
		// TODO Auto-generated method stub

	}

	private void updateScore (int score) {

		HashMap <Integer, Integer> numberMap = new HashMap <Integer, Integer>();
		numberMap.put(0, R.drawable.score_zero);
		numberMap.put(1, R.drawable.score_one);
		numberMap.put(2, R.drawable.score_two);
		numberMap.put(3, R.drawable.score_three);
		numberMap.put(4, R.drawable.score_four);
		numberMap.put(5, R.drawable.score_five);
		numberMap.put(6, R.drawable.score_six);
		numberMap.put(7, R.drawable.score_seven);
		numberMap.put(8, R.drawable.score_eight);
		numberMap.put(9, R.drawable.score_nine);

		if (score <= 9) {
			first_score_image_position.setVisibility(View.VISIBLE);
			first_score_image_position.setImageDrawable(getResources().getDrawable(numberMap.get(score)));

		}
		else if (score > 9 && score <= 99) {

			int[] two_digits = getDigitsOf(score);

			first_score_image_position.setImageDrawable(getResources().getDrawable(numberMap.get(two_digits[0])));

			second_score_image_position.setImageDrawable(getResources().getDrawable(numberMap.get(two_digits[1])));

			first_score_image_position.setVisibility(View.VISIBLE);
			second_score_image_position.setVisibility(View.VISIBLE);
		}
		else if (score > 99) {

			// int number = 12345;
			int[] digits = getDigitsOf(score);

			first_score_image_position.setImageDrawable(getResources().getDrawable(numberMap.get(digits[0])));

			second_score_image_position.setImageDrawable(getResources().getDrawable(numberMap.get(digits[1])));

			third_score_image_position.setImageDrawable(getResources().getDrawable(numberMap.get(digits[2])));

			first_score_image_position.setVisibility(View.VISIBLE);
			second_score_image_position.setVisibility(View.VISIBLE);
			third_score_image_position.setVisibility(View.VISIBLE);
		}

	}

	// for dividing score

	public int[] getDigitsOf (int num) {
		int digitCount = Integer.toString(num).length();

		if (num < 0)
			digitCount--;

		int[] result = new int[digitCount];

		while (digitCount-- > 0) {
			result[digitCount] = num % 10;
			num /= 10;
		}
		return result;
	}

	private void updateWickets (int noOfWickets) {

		HashMap <Integer, Integer> wicketsMap = new HashMap <Integer, Integer>();
		wicketsMap.put(0, R.drawable.wicket_zero);
		wicketsMap.put(1, R.drawable.wicket_one);
		wicketsMap.put(2, R.drawable.wicket_two);
		wicketsMap.put(3, R.drawable.wicket_three);
		wicketsMap.put(4, R.drawable.wicket_four);
		wicketsMap.put(5, R.drawable.wicket_five);
		wicketsMap.put(6, R.drawable.wicket_six);
		wicketsMap.put(7, R.drawable.wicket_seven);
		wicketsMap.put(8, R.drawable.wicket_eight);
		wicketsMap.put(9, R.drawable.wicket_nine);

		if (noOfWickets <= 9) {
			first_wicket_image_position.setVisibility(View.VISIBLE);
			first_wicket_image_position.setImageDrawable(getResources().getDrawable(wicketsMap.get(noOfWickets)));
		}
		else {
			int[] digits = getDigitsOf(noOfWickets);
			first_wicket_image_position.setVisibility(View.VISIBLE);

			second_wicket_image_position.setVisibility(View.VISIBLE);

			first_wicket_image_position.setImageDrawable(getResources().getDrawable(wicketsMap.get(digits[0])));
			second_wicket_image_position.setImageDrawable(getResources().getDrawable(wicketsMap.get(digits[1])));
		}
	}

	@Override
	public void onClick (View v) {
		switch (v.getId()) {
			case R.id.btn_view_score_board:
				if (mDrawer != null) {
					mDrawer.animateClose();
				}
				System.out.println("Score board match id is " + currentMatchId);
				Intent mServiceIntent = new Intent(this, LiveScoreService.class).setData(Uri.parse(getResources().getString(R.string.score_board_url) + currentMatchId));
				mServiceIntent.putExtra("KEY", "fullscore");
				startService(mServiceIntent);
		}
	}
}
