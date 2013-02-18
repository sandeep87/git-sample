package in.ccl.ui;

import in.ccl.helper.Util;
import in.ccl.livescore.service.LiveScoreService;
import in.ccl.photo.PhotoView;
import in.ccl.score.LiveScore;
import in.ccl.score.MatchesResponse;
import in.ccl.score.ScoreBoard;
import in.ccl.util.Constants;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LiveScoreActivity extends TopActivity {

	private int live_score_id;

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

	private PhotoView battingLogo;

	private DownloadStateReceiver mDownloadStateReceiver;

	private Button score_btn;

	private Button livestream_btn;

	private IntentFilter statusIntentFilter;

	private ArrayList <MatchesResponse> matcheslist;

	private static int currentMatchId;

	private Button btn_schedule;

	private TextView txt_no_live_match;
	private LinearLayout scoreLayout;
	private RelativeLayout backgroundLayout;

	@Override
	public void onCreate (Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		if (TopActivity.isLiveScore()) {
			addContent(R.layout.layout_live_score);
			backgroundLayout = (RelativeLayout)findViewById(R.id.layout_content);
			backgroundLayout.setBackgroundResource(R.drawable.score_bg);
      scoreLayout = (LinearLayout)findViewById(R.id.score_layout);
			first_score_image_position = (ImageView) findViewById(R.id.img_score_first_position);
			second_score_image_position = (ImageView) findViewById(R.id.img_score_second_position);
			third_score_image_position = (ImageView) findViewById(R.id.img_score_third_position);
			first_wicket_image_position = (ImageView) findViewById(R.id.img_first_wicket_img);
			second_wicket_image_position = (ImageView) findViewById(R.id.img_second_wicket_img);
			target_score = (TextView) findViewById(R.id.txt_target_score);
			second_innings_overs = (TextView) findViewById(R.id.txt_second_inning_overs);
			battingLogo = (PhotoView) findViewById(R.id.img_batting_logo);
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

			striker_strike_rate = (TextView) findViewById(R.id.striker_strike_rate);

			non_striker_strike_rate = (TextView) findViewById(R.id.non_striker_strike_rate);

			score_btn = (Button) findViewById(R.id.btn_score_board);
			livestream_btn = (Button) findViewById(R.id.btn_livestreaming);
			Util.setTextFontButton(this, score_btn);
			Util.setTextFontButton(this, livestream_btn);
			score_btn.setOnClickListener(this);
			livestream_btn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick (View v) {
					Intent mIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://reelax.in/live.php"));
					startActivity(mIntent);
				}
			});
			getLiveMatchId();

		}
		else {

			addContent(R.layout.layout_no_live_match);

			btn_schedule = (Button) findViewById(R.id.btn_schedule);

			txt_no_live_match = (TextView) findViewById(R.id.txt_no_live_match);

			btn_schedule.setOnClickListener(this);
			Util.setTextFont(this, txt_no_live_match);

		}

		// The filter's action is BROADCAST_ACTION
		statusIntentFilter = new IntentFilter(Constants.BROADCAST_ACTION);

		// Sets the filter's category to DEFAULT
		statusIntentFilter.addCategory(Intent.CATEGORY_DEFAULT);

		// Instantiates a new DownloadStateReceiver
		mDownloadStateReceiver = new DownloadStateReceiver();

	}

	private void getLiveMatchId () {
		Intent mServiceIntent = new Intent(LiveScoreActivity.this, LiveScoreService.class).setData(Uri.parse(getResources().getString(R.string.live_matches_urls)));
		startService(mServiceIntent);
	}

	@Override
	protected void onResume () {
		super.onResume();
		LocalBroadcastManager.getInstance(this).registerReceiver(mDownloadStateReceiver, statusIntentFilter);
		Intent mServiceIntent = new Intent(this, LiveScoreService.class).setData(Uri.parse(getResources().getString(R.string.live_score_url) + currentMatchId));
		mServiceIntent.putExtra("KEY", "update-livescore");
		startService(mServiceIntent);
	}

	@Override
	protected void onPause () {
		super.onPause();
		Intent mServiceIntent = new Intent(this, LiveScoreService.class).setData(Uri.parse(getResources().getString(R.string.live_score_url) + currentMatchId));
		AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		PendingIntent pendingIntent = PendingIntent.getService(this, 0, mServiceIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		alarmManager.cancel(pendingIntent);
		LocalBroadcastManager.getInstance(this).unregisterReceiver(mDownloadStateReceiver);
	}

	@Override
	public void onClick (View v) {
		switch (v.getId()) {
			case R.id.btn_score_board:
				Intent mServiceIntent = new Intent(this, LiveScoreService.class).setData(Uri.parse(getResources().getString(R.string.score_board_url) + currentMatchId));
				mServiceIntent.putExtra("KEY", "fullscore");
				startService(mServiceIntent);
				break;
			case R.id.btn_schedule:
				Intent schedule_intent = new Intent(LiveScoreActivity.this, ScheduleActivity.class);
				startActivity(schedule_intent);
		}
	}

	private void callLiveScoreService (int liveMatchId) {
		currentMatchId = liveMatchId;
		Intent mServiceIntent = new Intent(LiveScoreActivity.this, LiveScoreService.class).setData(Uri.parse(getResources().getString(R.string.live_score_url) + liveMatchId));
		mServiceIntent.putExtra("KEY", "livescore-activity");
		startService(mServiceIntent);

	}

	private class DownloadStateReceiver extends BroadcastReceiver {

		private DownloadStateReceiver () {
			// prevents instantiation by other packages.
		}

		@Override
		public void onReceive (Context context, Intent intent) {
			switch (intent.getIntExtra(Constants.EXTENDED_DATA_STATUS, Constants.STATE_ACTION_COMPLETE)) {
				case in.ccl.database.Constants.STATE_LIVE_SCORE_ACTIVITY_TASK_COMPLETED:
					if (intent != null && intent.hasExtra("livescore")) {
						LiveScore liveScore = intent.getParcelableExtra("livescore");
						displayScore(liveScore);

					}
					break;
				case in.ccl.database.Constants.STATE_LIVE_SCORE_UPDATE_TASK_COMPLETED:
					if (intent != null && intent.hasExtra("livescore")) {
						LiveScore liveScore = intent.getParcelableExtra("livescore");
						displayScore(liveScore);

					}
					break;
				case in.ccl.database.Constants.STATE_MATCHES_TASK_COMPLETED:
					if (intent != null && intent.hasExtra("matches_list")) {

						matcheslist = intent.getParcelableArrayListExtra("matches_list");
						System.out.println("match list " + matcheslist.size());
						if (matcheslist != null) {
							if (matcheslist.size() > 0) {
								System.out.println("live id " + matcheslist.get(0).getId());

								callLiveScoreService(matcheslist.get(0).getId());

							}
						}
					}
					break;
				case in.ccl.database.Constants.STATE_LIVE_SCOREBOARD_TASK_COMPLETED:
					if (intent != null && intent.hasExtra("scoreboard")) {
						ScoreBoard scoreBoard = intent.getParcelableExtra("scoreboard");
						if (scoreBoard != null) {
							Intent scoreBoardIntent = new Intent(LiveScoreActivity.this, ScoreBoardActivity.class);
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

	public void displayScore (LiveScore liveScore) {
		// allow user to select again

		if (target_score != null) {
			if (liveScore.getTarget_score() == 0) {
				target_score.setVisibility(View.GONE);
			}
			else {
				String first_inning_overs = "(" + liveScore.getTarget_overs() + " Overs" + ")";
				if (liveScore.getStatus().equals("over")) {
					target_score.setText("");
				}
				else {
					target_score.setText("Target  :  " + liveScore.getTarget_score() + " " + first_inning_overs + "" + "\n" + (liveScore.getNeed_score() == null ? "" : "\n" + "SCORE : " + liveScore.getNeed_score()));

				}
			}

			TextView txtErrrorMessage = new TextView(this);

			battingLogo.setScaleType(ImageView.ScaleType.FIT_CENTER);
			if (liveScore.getTeamLogo() != null) {
				battingLogo.setImageURL(liveScore.getTeamLogo(), true, getResources().getDrawable(R.drawable.photo_imagenotqueued), txtErrrorMessage);
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

		}
		scoreLayout.setVisibility(View.VISIBLE);
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
