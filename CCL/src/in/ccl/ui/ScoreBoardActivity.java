package in.ccl.ui;

import in.ccl.adapters.BowlerListAdapter;
import in.ccl.adapters.InningsAdapter;
import in.ccl.helper.Util;
import in.ccl.livescore.service.LiveScoreService;
import in.ccl.score.Innings;
import in.ccl.score.ScoreBoard;
import in.ccl.util.Constants;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ScoreBoardActivity extends TopActivity {

	private Button btnFirstInnings;

	private Button btnSecondInnings;

	private TextView txtInningsTeamTitle;

	private TextView txtFirstInningsTeamTitle;

	private TextView txtSecondInningsTeamTitle;

	private TextView txtBowlingTeamTitle;

	// private ImageView imgInningsLogo;

	private TextView txtInningsScore;

	private ListView listInnings;

	private ListView bowlerListView;

	private ScoreBoard scoreBoard;

	private int liveMatchId;

	// private RelativeLayout layoutBowlerBetails;

	// private RelativeLayout layoutBowlingTeamDetatils;
	private DownloadStateReceiver mDownloadStateReceiver;

	private IntentFilter statusIntentFilter;

	private Innings firstInnings;

	private Innings secondInnings;

	private boolean isSecondInnings = true;
	
	private boolean IsSecondInningsBtnClkd = false;

	public void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addContent(R.layout.scoreboard_layout);

		btnFirstInnings = (Button) findViewById(R.id.btn_first_innings);
		btnSecondInnings = (Button) findViewById(R.id.btn_second_innings);
		txtFirstInningsTeamTitle = (TextView) findViewById(R.id.first_inns_team_txt);
		txtSecondInningsTeamTitle = (TextView) findViewById(R.id.second_inns_team_txt);
		txtInningsTeamTitle = (TextView) findViewById(R.id.txt_inningsteam_title);
		// imgInningsLogo = (ImageView) findViewById(R.id.img_innings_logo);
		txtInningsScore = (TextView) findViewById(R.id.txt_innings_score);
		listInnings = (ListView) findViewById(R.id.list_innings);
		// layoutBowlerBetails = (RelativeLayout) findViewById(R.id.layout_bowler_innings);
		// layoutBowlingTeamDetatils = (RelativeLayout) findViewById(R.id.layout_bowling_teamdetails);
		txtBowlingTeamTitle = (TextView) findViewById(R.id.txt_bowlingteam_title);
		bowlerListView = (ListView) findViewById(R.id.bowler_list);

		// The filter's action is BROADCAST_ACTION
		statusIntentFilter = new IntentFilter(Constants.BROADCAST_ACTION);

		// Sets the filter's category to DEFAULT
		statusIntentFilter.addCategory(Intent.CATEGORY_DEFAULT);

		// Instantiates a new DownloadStateReceiver
		mDownloadStateReceiver = new DownloadStateReceiver();

		scoreBoard = getIntent().getParcelableExtra("scoreboard");
		liveMatchId = getIntent().getIntExtra("match_id", 0);
		Util.setTextFont(this, txtFirstInningsTeamTitle);
		Util.setTextFont(this, txtSecondInningsTeamTitle);
		Util.setTextFont(this, btnFirstInnings);
		Util.setTextFont(this, btnSecondInnings);

		insertDatainList(scoreBoard);
		// here we show the FirstInnings Data in screen ,when user taps FirstInnings Button
		btnFirstInnings.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick (View v) {
				btnFirstInnings.setBackgroundResource(R.drawable.inningstab_tapped);
				btnSecondInnings.setBackgroundResource(R.drawable.innings_tab);
				IsSecondInningsBtnClkd = false;
				// here we load the screen Controls and first innings data.
				showDetailsInView(firstInnings);

			}

		});

		// SecondInnings Button OnCLickListener.
		// here we show the SecondInnings Data in screen ,when user taps SecondInnings Button
		btnSecondInnings.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick (View v) {

				btnSecondInnings.setBackgroundResource(R.drawable.inningstab_tapped);
				btnFirstInnings.setBackgroundResource(R.drawable.innings_tab);
				IsSecondInningsBtnClkd = true;

				// here we load the screen Controls and Second innings data.
				showDetailsInView(secondInnings);

			}

		});

	}

	private void insertDatainList (ScoreBoard scoreBoard) {
		if (scoreBoard.getFirstInningsList() != null) {
			firstInnings = scoreBoard.getFirstInningsList();
			txtFirstInningsTeamTitle.setText(firstInnings.getBatting_team());
			txtSecondInningsTeamTitle.setText(firstInnings.getBowling_team());
		}
		if (scoreBoard.getSecondInningsList() != null) {
			secondInnings = scoreBoard.getSecondInningsList();
			if(secondInnings.getBatting_info() == null){
				isSecondInnings = false;
			}
		}
		else {
			isSecondInnings = false;
		}

		// Her we Checked whether match is First innings or not.
		if (isSecondInnings) {
			btnSecondInnings.setVisibility(View.VISIBLE); // Gone the Second innings Button
			int width = 0;
			int deviceDisplayDensity = getResources().getDisplayMetrics().densityDpi;
			if (deviceDisplayDensity == DisplayMetrics.DENSITY_LOW) {
				width = 115;
			}
			else if (deviceDisplayDensity == DisplayMetrics.DENSITY_MEDIUM) {
				width = 155;
			}
			else if (deviceDisplayDensity == DisplayMetrics.DENSITY_HIGH) {
				width = 225;
			}
			else if (deviceDisplayDensity == DisplayMetrics.DENSITY_XHIGH) {
				width = android.app.ActionBar.LayoutParams.WRAP_CONTENT;
			}
			btnFirstInnings.setLayoutParams(new RelativeLayout.LayoutParams(width, LayoutParams.WRAP_CONTENT));
			if(IsSecondInningsBtnClkd){
				showDetailsInView(secondInnings);
			}else{
				showDetailsInView(firstInnings);
			}
			
		}
		else {
			btnSecondInnings.setVisibility(View.GONE); // Gone the Second innings Button
			// here we load the screen Controls and first innings data.
			showDetailsInView(firstInnings);
			// change firstInningsButton to fillparent
			btnFirstInnings.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		}
	}

	/**
	 * These method used to find the height of ListView Item
	 * 
	 * @param listView ListView
	 * @param items listItem
	 * @return height of the ListView item.
	 */
	public static int getItemHightofListView (ListView listView,Context ctx) {

		ListAdapter mAdapter = listView.getAdapter();

		int listviewElementsheight = 0;
		// for listview total item hight
		 int items = mAdapter.getCount();

		for (int i = 0; i < items; i++) {

			View childView = mAdapter.getView(i, null, listView);
			childView.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
			int width = 0;
			int deviceDisplayDensity = ctx.getResources().getDisplayMetrics().densityDpi;
			if (deviceDisplayDensity == DisplayMetrics.DENSITY_LOW) {
				width = 24;
			}
			else if (deviceDisplayDensity == DisplayMetrics.DENSITY_MEDIUM) {
				width = 28;
			}
			else if (deviceDisplayDensity == DisplayMetrics.DENSITY_HIGH) {
				width = 39;
			}
			else if (deviceDisplayDensity == DisplayMetrics.DENSITY_XHIGH) {
				width = 43;
			}
			listviewElementsheight +=width;
		}

		return listviewElementsheight;

	}

	/**
	 * These Method used to show Screen Controls and Innings Data in ListView
	 * 
	 * @param listItems Innings ListItems
	 */
	public void showDetailsInView (Innings listItems) {

		if (listItems != null) {
			txtFirstInningsTeamTitle.setText(listItems.getBatting_team());
			txtSecondInningsTeamTitle.setText(listItems.getBowling_team());
			txtInningsTeamTitle.setText(listItems.getBatting_team());
			// imgInningsLogo.setBackgroundResource(R.drawable.teluguwarriors_logo_scoreboard_bowling);
			String score = listItems.getRuns() + "/" + listItems.getWickets();
			txtInningsScore.setText(score);
			txtBowlingTeamTitle.setText(listItems.getBowling_team());
			// System.out.println("batting team size "+listItems.getBatting_info().get(0).getName());
			if (listItems != null && listItems.getBatting_info() != null) {
				listInnings.setAdapter(new InningsAdapter(ScoreBoardActivity.this, listItems));

			}
			if (listInnings != null && listItems.getBatting_info() != null) {
				int height = getItemHightofListView(listInnings, this);

				int listSize = height * (listItems.getBatting_info().size());

				listInnings.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, height));
				if (listItems != null) {
					bowlerListView.setAdapter(new BowlerListAdapter(ScoreBoardActivity.this, listItems));

				}
				height = getItemHightofListView(bowlerListView, this);
				listSize = height * (listItems.getBowler_info().size());
				bowlerListView.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, height));
			}
		}
	}

	@Override
	protected void onResume () {
		super.onResume();
		LocalBroadcastManager.getInstance(this).registerReceiver(mDownloadStateReceiver, statusIntentFilter);
		if (Util.getInstance().isOnline(this)) {

			Intent mServiceIntent = new Intent(this, LiveScoreService.class).setData(Uri.parse(getResources().getString(R.string.score_board_url)+liveMatchId));
			mServiceIntent.putExtra("KEY", "score_board_update");
			startService(mServiceIntent);
		}
		else {
			Toast.makeText(this, getResources().getString(R.string.network_error_message), Toast.LENGTH_LONG).show();
		}

	}

	@Override
	protected void onPause () {
		super.onPause();
		Intent mServiceIntent = new Intent(this, LiveScoreService.class).setData(Uri.parse(getResources().getString(R.string.live_score_url)+liveMatchId));
		AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
		PendingIntent pendingIntent = PendingIntent.getService(this, 0, mServiceIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		alarmManager.cancel(pendingIntent);
		LocalBroadcastManager.getInstance(this).unregisterReceiver(mDownloadStateReceiver);

	}

	private class DownloadStateReceiver extends BroadcastReceiver {

		private DownloadStateReceiver () {
			// prevents instantiation by other packages.
		}

		@Override
		public void onReceive (Context context, Intent intent) {
			switch (intent.getIntExtra(Constants.EXTENDED_DATA_STATUS, Constants.STATE_ACTION_COMPLETE)) {
				case in.ccl.database.Constants.STATE_LIVE_SCOREBOARD_UPDATE_TASK_COMPLETED:
					if (intent != null && intent.hasExtra("scoreboard")) {
						scoreBoard = intent.getParcelableExtra("scoreboard");
						if (scoreBoard != null) {
							insertDatainList(scoreBoard);
						}

					}
			}
		}
	}
}