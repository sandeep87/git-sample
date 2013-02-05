package in.ccl.ui;

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
import in.ccl.score.LiveScore;
import in.ccl.score.MatchesResponse;
import in.ccl.score.ScoreParser;
import in.ccl.util.Constants;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.ads.AdRequest;
import com.google.ads.AdView;

public class TopActivity extends Activity implements AnimationLayout.Listener, ServerResponse {

	// used as key of the logs.
	private static final String TAG = "MainActivity";

	// used to identifiy score view either visible or invisble.
	private boolean isScoreViewVisible;

	// initially animation is in end state, so isAnimationComplete is true.
	// to prevent multiple clicks when one animation is going on.
	private boolean isAnimationComplete = true;

	// dynamically adding score view to layout content
	// for displaying score when user selects dropdown, which is from score part.
	private View scoreView;

	// choose matches
	private View matcheswDetailsscoreView;

	// used to add inner views from the calling activity to top activity.
	private RelativeLayout layoutContent;

	// dropdown selection button from header layout.
	private ImageButton imgBtnScoreDropDown;

	// to prevent backbutton while score animationis showing.
	// in this case if user press back button should go animation out.
	private boolean isScoreAnimationHappend = false;
	private boolean isListMatchesAnimationHappend = false;

	private RelativeLayout scoreLayout;

	private TextView txtScore;

	private TextView txtScoreTitle;

	private AnimationLayout mLayout;

	private LinearLayout menuLayout;

	private DownloadStateReceiver mDownloadStateReceiver;

	private IntentFilter statusIntentFilter;


	private boolean isLiveScore = true;
	private boolean isSingleMatch;

	private LiveScore liveScore;

	private ImageView first_score_image_position;

	private ImageView second_score_image_position;

	private ImageView third_score_image_position;

	private ImageView first_wicket_image_position;

	private ImageView second_wicket_image_position;

	private ScoreParser parser;

	private ArrayList <MatchesResponse> matcheslist = null; /*
																													 * private TextView notificationTxt;
																													 * 
																													 * private TextView notificationTitle;
																													 */


	private LinearLayout adsLayout;

	private TextView txtScoreHeader;

	private TextView txtCurrentScore;

	private static String mCurrentScore;

	private static boolean isCurrentScoreTimerStarted;

	public static boolean isCurrentScoreTimerStarted () {
		return isCurrentScoreTimerStarted;
	}

	public static void setCurrentScoreTimerStarted (boolean isCurrentScoreTimerStarted) {
		TopActivity.isCurrentScoreTimerStarted = isCurrentScoreTimerStarted;
	}

	/*
	 * private TextView notificationTxt;
	 * 
	 * private TextView notificationTitle;
	 */
	// for displaying adds.
	private AdView adView;

	@Override
	public void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.common_layout);
		parser = new ScoreParser();

		txtScoreHeader = (TextView) findViewById(R.id.score_title_textview);
		txtCurrentScore = (TextView) findViewById(R.id.score_textview);

		// The filter's action is BROADCAST_ACTION
		statusIntentFilter = new IntentFilter(Constants.BROADCAST_ACTION);

		// Sets the filter's category to DEFAULT
		statusIntentFilter.addCategory(Intent.CATEGORY_DEFAULT);

		// Instantiates a new DownloadStateReceiver
		mDownloadStateReceiver = new DownloadStateReceiver();

		mLayout = (AnimationLayout) findViewById(R.id.animation_layout);

		// animationLayoutSlider = (LinearLayout) findViewById(R.id.animation_layout_sidebar);
		menuLayout = (LinearLayout) findViewById(R.id.menu_layout);

		// for adds
		adView = (AdView) findViewById(R.id.adMob);
		adsLayout = (LinearLayout) findViewById(R.id.admob_layout);
		adsLayout.setVisibility(View.VISIBLE);
		// Add the adView to it
		// LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		// lp.gravity = Gravity.CENTER_HORIZONTAL;
		// layout.addView(adView, lp);

		// Initiate a generic request to load it with an ad
		AdRequest adRequest = new AdRequest();
		/*
		 * <<<<<<< HEAD //adRequest.addTestDevice(AdRequest.TEST_EMULATOR); // Emulator adRequest.addTestDevice("TEST_DEVICE_ID"); // Test Android Device adView.loadAd(adRequest);//new AdRequest() =======
		 */
		adRequest.addTestDevice(AdRequest.TEST_EMULATOR); // Emulator

		adView.loadAd(adRequest);// new AdRequest()
		// adView.loadAd(new AdRequest());
		/*
		 * notificationTxt = (TextView) findViewById(R.id.notification_textview); notificationTitle = (TextView) findViewById(R.id.notification_title_textview); TextView notificationOneTxt = (TextView) findViewById(R.id.notification_item1); TextView notificationTwoTxt = (TextView)
		 * findViewById(R.id.notification_item2); TextView notificationThreeTxt = (TextView) findViewById(R.id.notification_item3); TextView notificationFourTxt = (TextView) findViewById(R.id.notification_item4);
		 */
		/*
		 * Util.setTextFont(this, notificationTitle); Util.setTextFont(this, notificationTxt); Util.setTextFont(this, notificationOneTxt); Util.setTextFont(this, notificationTwoTxt); Util.setTextFont(this, notificationThreeTxt); Util.setTextFont(this, notificationFourTxt);
		 */mLayout.setListener(this);

		// for user menu selection from top activity.
		ImageButton imgBtnMenu = (ImageButton) findViewById(R.id.img_btn_menu);

		// setting menu title font
		TextView menuTitleTxt = (TextView) findViewById(R.id.menu_title);
		Util.setTextFont(this, menuTitleTxt);

		// SlidingDrawer slidingDrawer = (SlidingDrawer) findViewById(R.id.slidingDrawer);
		/*
		 * slidingDrawer.setOnDrawerOpenListener(new OnDrawerOpenListener() {
		 * 
		 * @Override public void onDrawerOpened () { // notificationTxt.setVisibility(View.GONE); } });
		 * 
		 * slidingDrawer.setOnDrawerCloseListener(new OnDrawerCloseListener() {
		 * 
		 * @Override public void onDrawerClosed () { // notificationTxt.setVisibility(View.VISIBLE);
		 * 
		 * } });
		 */
		txtScore = (TextView) findViewById(R.id.score_textview);
		txtScoreTitle = (TextView) findViewById(R.id.score_title_textview);
		Util.setTextFont(this, txtScore);
		Util.setTextFont(this, txtScoreTitle);
		imgBtnScoreDropDown = (ImageButton) findViewById(R.id.imgbtn_score_dropdown);
		layoutContent = (RelativeLayout) findViewById(R.id.layout_content);
		scoreLayout = (RelativeLayout) findViewById(R.id.header);
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
				if (isLiveScore) {
					// callAsyncTask(getString(R.string.livematches_url));
					matcheslist = parser.liveMatchesRequest();
					if (matcheslist.size() < 2) {
						//parser.parseLiveScore();
						isSingleMatch = true;
						animationInit(true);
					}	else {
						isSingleMatch = false;
						isListMatchesAnimationHappend = true;
						animationInit(false);
					}
					// callAsyncTask(getString(R.string.livescore_url));
					isLiveScore = false;
				}
				else {
					//prepareAnimation(scoreView, 0.0f, -380.f, true);
					//prepareAnimation(matcheswDetailsscoreView, 0.0f, -380.f, true);
					if(isSingleMatch){
					
						isSingleMatch = false;
					}else{
						
						isSingleMatch = true;

					}
				
				

					isLiveScore = true;
				}
			}

		});

		imgBtnScoreDropDown.setOnClickListener(new OnClickListener() {

			public void onClick (View v) {
				// animationInit();
			}
		});
		MenuItems.getInstance().loadMenu(this, menuLayout, mLayout);

	}

	private void animationInit (boolean matchesstatus) {
		if (isScoreViewVisible && isAnimationComplete) {
			// start animation to hide the score view.
			isScoreAnimationHappend = false;

		}
		else if (isAnimationComplete) {
			if (matchesstatus) {
				// start animation to show the score view.
				displayLiveScore();
				prepareAnimation(scoreView, 0.0f, -380.f, true);
			}else {
				
				LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
				matcheswDetailsscoreView = inflate.inflate(R.layout.livematches_layout, null);
				// prepareAnimation(matcheswDetailsscoreView, 0.0f, -380.f, true);
				
				Button second_live_score = (Button)matcheswDetailsscoreView.findViewById(R.id.second_matches_livescore_btn);
				Button first_live_score = (Button)matcheswDetailsscoreView.findViewById(R.id.first_matches_livescore_btn);

				first_live_score.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick (View v) {
						displayLiveScore();
						
					}
				});
				
				
				

				second_live_score.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick (View v) {
						displayLiveScore();
						
					}
				});
				
				
				

			

				layoutContent.addView(matcheswDetailsscoreView, lp);
				prepareAnimation(matcheswDetailsscoreView, -380.0f, 0.0f, false);
			}
		}
	}

	/**
	 * 
	 * start animation fromYDelta and toYDelta parameters on view. flag is used to identify particular view either going to add or remove from layout content.
	 * 
	 * @param scoreView View
	 * @param fromYDelta float
	 * @param toYDelta float
	 * @param flag boolean
	 */

	private void prepareAnimation (View view, float fromYDelta, float toYDelta, boolean flag) {
		if (flag) {
			txtScoreTitle.setVisibility(View.VISIBLE);
			txtScore.setText(getResources().getString(R.string.sample_score));
		}
		else {
			txtScore.setText("BENGAL TIGERS  vs  TELUGU WARRIORS");
		}
		txtScoreTitle.setVisibility(View.GONE);
		AnimationSet as = new AnimationSet(false);
		TranslateAnimation ta = new TranslateAnimation(0.0f, 0.0f, fromYDelta, toYDelta);
		addAnimationLister(ta, flag);
		as.addAnimation(ta);
		as.setDuration(300);
		as.setFillAfter(true);
		view.setAnimation(as);
		view.startAnimation(as);
	}

	private void addAnimationLister (TranslateAnimation ta, final boolean removeView) {
		ta.setAnimationListener(new AnimationListener() {

			public void onAnimationStart (Animation animation) {

				isAnimationComplete = false;
			}

			public void onAnimationRepeat (Animation animation) {
			}

			public void onAnimationEnd (Animation animation) {
				isAnimationComplete = true;
				if (scoreView != null && removeView) {
					// when score view is going to hide, removing score view from layout content.
					isScoreViewVisible = false;
					// setting dropdown default image, when animation is done.

					imgBtnScoreDropDown.setBackgroundResource(R.drawable.dropdown);
					layoutContent.removeView(scoreView);
				}
				else { // setting dropdown up image, when score view is visible.
					imgBtnScoreDropDown.setBackgroundResource(R.drawable.dropdown_up);
					isScoreViewVisible = true;
				}
			}
		});

	}

	@Override
	protected void onRestart () {
		super.onRestart();
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

		if (mLayout.isOpening()) {
			System.out.println("open");
			// animationLayoutSlider.setVisibility(View.GONE);
			mLayout.closeSidebar();
		} else if (isScoreAnimationHappend) { 
				 // start animation to hide the score view.
			System.out.println("isScoreAnimationHApped");
				 prepareAnimation(scoreView, 0.0f, -380.f, true); 
				// prepareAnimation(matcheswDetailsscoreView, 0.0f, -380.f, true); 
				 
				 isScoreAnimationHappend = false;
				 }else if(isListMatchesAnimationHappend) {
					 prepareAnimation(matcheswDetailsscoreView, 0.0f, -380.f, true); 
					 isListMatchesAnimationHappend = false;
			
		}else{
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
		if (mCurrentScore == null) {
			txtScoreHeader.setVisibility(View.GONE);
			txtCurrentScore.setText(getResources().getString(R.string.score));
		}
		else {
			txtScoreHeader.setVisibility(View.VISIBLE);
			txtScoreHeader.setText("Score : ");
			txtCurrentScore.setText(mCurrentScore);
		}
		if (!isCurrentScoreTimerStarted) {
			// send request to get live matches schedule
			Intent mServiceIntent = new Intent(this, LiveScoreService.class).setData(Uri.parse(getResources().getString(R.string.dummy_livematches_url)));
			startService(mServiceIntent);

		}
		// CallLiveScoreService();
	}

	private void CallLiveScoreService () {
		Intent mServiceIntent = new Intent(this, LiveScoreService.class).setData(Uri.parse(getResources().getString(R.string.dummy_currentscore_url)));
		startService(mServiceIntent);
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
						if (currentScore == null) {
							txtScoreHeader.setVisibility(View.GONE);
							mCurrentScore = null;
							txtCurrentScore.setText(getResources().getString(R.string.score));
						}
						else {
							txtScoreHeader.setVisibility(View.VISIBLE);
							txtScoreHeader.setText("Score : ");
							mCurrentScore = currentScore;
							txtCurrentScore.setText(currentScore);
						}
					}
					break;
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

	

	private void displayLiveScore () {
		LiveScore liveScore = parser.parseLiveScore();
		isScoreAnimationHappend = true;
		//matcheswDetailsscoreView.setVisibility(View.GONE);
		LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
		scoreView = inflate.inflate(R.layout.score_view, null);

		//TextView txtStiker = (TextView) scoreView.findViewById(R.id.striker_batsman);

		TextView target_score = (TextView) scoreView.findViewById(R.id.target_team_score);

		TextView first_innings_wickets = (TextView) scoreView.findViewById(R.id.first_innings_wickets);

		TextView first_innings_overs = (TextView) scoreView.findViewById(R.id.first_innings_overs);

		TextView req_runs = (TextView) scoreView.findViewById(R.id.req_runs);

		first_score_image_position = (ImageView) scoreView.findViewById(R.id.score_first_position);

		second_score_image_position = (ImageView) scoreView.findViewById(R.id.score_second_position);

		third_score_image_position = (ImageView) scoreView.findViewById(R.id.score_third_position);

		first_wicket_image_position = (ImageView) scoreView.findViewById(R.id.first_wicket_img);
		
		second_wicket_image_position = (ImageView) scoreView.findViewById(R.id.second_wicket_img);
		
		TextView second_innings_overs = (TextView) scoreView.findViewById(R.id.second_inning_overs);

		TextView striker_name = (TextView) scoreView.findViewById(R.id.striker_batsman);

		TextView striker_runs_batsman = (TextView) scoreView.findViewById(R.id.striker_runs_batsman);

		TextView striker_balls = (TextView) scoreView.findViewById(R.id.striker_balls);

		TextView non_striker_batsman = (TextView) scoreView.findViewById(R.id.non_striker_batsman);

		TextView non_striker_runs_batsman = (TextView) scoreView.findViewById(R.id.non_striker_runs_batsman);

		TextView non_striker_balls = (TextView) scoreView.findViewById(R.id.non_striker_balls);

		TextView current_bowler_name = (TextView) scoreView.findViewById(R.id.bowler_statics);

		TextView current_bowler_overs = (TextView) scoreView.findViewById(R.id.bowler_overs);

		TextView current_bowler_runs = (TextView) scoreView.findViewById(R.id.bowler_runs);

		TextView current_bowler_wkts = (TextView) scoreView.findViewById(R.id.bowler_wkts);

		TextView current_bowler_mnds = (TextView) scoreView.findViewById(R.id.bowler_mdns);

		TextView previous_bowler_name = (TextView) scoreView.findViewById(R.id.non_bowler_statics);

		TextView previous_bowler_overs = (TextView) scoreView.findViewById(R.id.non_bowler_overs);

		TextView previous_bowler_runs = (TextView) scoreView.findViewById(R.id.non_bowler_runs);

		TextView previous_bowler_wkts = (TextView) scoreView.findViewById(R.id.non_bowler_wkts);

		TextView previous_bowler_mnds = (TextView) scoreView.findViewById(R.id.non_bowler_mdns);

		target_score.setText(liveScore.getTarget_score() + "" + "/");

		updateScore(liveScore.getCurrent_score_score());

		updateWickets(liveScore.getCurrent_score_wickets());

		first_innings_wickets.setText(liveScore.getTarget_wickets() + "");

		String overs = "(" + liveScore.getTarget_overs() + " Overs" + ")";

		first_innings_overs.setText(overs + "");

		String needScore = "SCORE : " + liveScore.getNeed_score();

		req_runs.setText(needScore);

		second_innings_overs.setText(liveScore.getCurrent_score_overs() + "");

		striker_name.setText(liveScore.getStriker_name());

		striker_runs_batsman.setText(liveScore.getStriker_score() + "");

		striker_balls.setText(liveScore.getStriker_balls() + "");

		non_striker_batsman.setText(liveScore.getNonstriker_name());

		non_striker_runs_batsman.setText(liveScore.getNonstriker_score() + "");

		non_striker_balls.setText(liveScore.getNonstriker_balls() + "");

		current_bowler_name.setText(liveScore.getCurrent_bowler_name());

		current_bowler_overs.setText(liveScore.getCurrent_bowler_overs() + "");

		current_bowler_runs.setText(liveScore.getCurrent_bowler_runs() + "");

		current_bowler_wkts.setText(liveScore.getCurrent_bowler_wickets() + "");

		current_bowler_mnds.setText(liveScore.getCurrent_bowler_madiens() + "");

		previous_bowler_name.setText(liveScore.getPrevious_bowler_name());

		previous_bowler_runs.setText(liveScore.getPrevious_bowler_runs() + "");

		previous_bowler_overs.setText(liveScore.getPrevious_bowler_overs() + "");

		previous_bowler_wkts.setText(liveScore.getPrevious_bowler_wickets() + "");

		previous_bowler_mnds.setText(liveScore.getPrevious_bowler_madiens() + "");

		/*
		 * live_score_lst = (ListView) scoreView.findViewById(R.id.livescore_list); live_score_lst.setCacheColorHint(color.transparent); live_score_lst.setFadingEdgeLength(1);
		 */

		// txtStiker.setText("INDRAJIT" + Html.fromHtml("<sup>*</sup>"));
		// Util.setTextFont(this, txtStiker);
		Button viewScoreBoard = (Button) scoreView.findViewById(R.id.btn_view_score_board);
		viewScoreBoard.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick (View v) {
				ScoreParser parser = new ScoreParser();
				parser.ParseJsonScoreBoard(TopActivity.this);
			}
		});
		layoutContent.addView(scoreView, lp);
		prepareAnimation(scoreView, -380.0f, 0.0f, false);
		matcheswDetailsscoreView.setVisibility(View.INVISIBLE);
		scoreView.setVisibility(View.VISIBLE);

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
}
