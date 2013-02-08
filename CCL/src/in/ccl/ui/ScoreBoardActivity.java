package in.ccl.ui;

import in.ccl.adapters.BowlerListAdapter;
import in.ccl.adapters.InningsAdapter;
import in.ccl.helper.Util;
import in.ccl.score.Innings;
import in.ccl.score.ScoreBoard;
import android.os.Bundle;
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

public class ScoreBoardActivity extends TopActivity {

	private Button btnFirstInnings;

	private Button btnSecondInnings;

	private TextView txtInningsTeamTitle;

	private TextView txtFirstInningsTeamTitle;

	private TextView txtSecondInningsTeamTitle;

	private TextView txtBowlingTeamTitle;

	//private ImageView imgInningsLogo;

	private TextView txtInningsScore;

	private ListView listInnings;

	private ListView bowlerListView;
	
	private ScoreBoard scoreBoard;

	//private RelativeLayout layoutBowlerBetails;

	//private RelativeLayout layoutBowlingTeamDetatils;

	private Innings firstInnings;

	private Innings secondInnings;
	
	private boolean isSecondInnings = true;

	public void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addContent(R.layout.scoreboard_layout);

		btnFirstInnings                = (Button) findViewById(R.id.btn_first_innings);
		btnSecondInnings               = (Button) findViewById(R.id.btn_second_innings);
		txtFirstInningsTeamTitle       = (TextView) findViewById(R.id.first_inns_team_txt);
		txtSecondInningsTeamTitle      = (TextView) findViewById(R.id.second_inns_team_txt);
		txtInningsTeamTitle            = (TextView) findViewById(R.id.txt_inningsteam_title);
	//	imgInningsLogo                 = (ImageView) findViewById(R.id.img_innings_logo);
		txtInningsScore                = (TextView) findViewById(R.id.txt_innings_score);
		listInnings                    = (ListView) findViewById(R.id.list_innings);
	//	layoutBowlerBetails            = (RelativeLayout) findViewById(R.id.layout_bowler_innings);
	//	layoutBowlingTeamDetatils      = (RelativeLayout) findViewById(R.id.layout_bowling_teamdetails);
		txtBowlingTeamTitle            = (TextView) findViewById(R.id.txt_bowlingteam_title);
		bowlerListView                 = (ListView) findViewById(R.id.bowler_list);

	//	firstInningsList               = getIntent().getParcelableArrayListExtra(Constants.FIRST_INNINGS_KEY);
	//	secondInningsList              = getIntent().getParcelableArrayListExtra(Constants.SECOND_INNINGS_KEY);
		//isSecondInnings                = getIntent().getBooleanExtra(Constants.SECOND_INNINGS_STATUS, false);  
     scoreBoard                     = getIntent().getParcelableExtra("scoreboard");
     
     if(scoreBoard.getFirstInningsList()!= null){
    	 firstInnings                   = scoreBoard.getFirstInningsList();
    	 
    	  txtFirstInningsTeamTitle.setText(firstInnings.getBatting_team());
   	  	txtSecondInningsTeamTitle.setText(firstInnings.getBowling_team());
     }
     if(scoreBoard.getSecondInningsList()!=null) {
    	 secondInnings                  = scoreBoard.getSecondInningsList();       
     }else{
    	 isSecondInnings = false;
     }
    
		Util.setTextFont(this, txtFirstInningsTeamTitle);
		Util.setTextFont(this, txtSecondInningsTeamTitle);
		Util.setTextFont(this, btnFirstInnings);
		Util.setTextFont(this, btnSecondInnings);
		
		
		
		//Her we Checked whether match is First innings or not.
		if(isSecondInnings){
			showDetailsInView(firstInnings);
		}else{
			btnSecondInnings.setVisibility(View.GONE); // Gone the Second innings Button
			//here we load the screen Controls and first innings data.
		  showDetailsInView(firstInnings);
			//change firstInningsButton to fillparent
			btnFirstInnings.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
		}
		
		//firstInnings Button OnCLickListener.
		// here we show the FirstInnings Data in screen ,when user taps FirstInnings Button 
		btnFirstInnings.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick (View v) {
				btnFirstInnings.setBackgroundResource(R.drawable.inningstab_tapped);
				btnSecondInnings.setBackgroundResource(R.drawable.innings_tab);
				//here we load the screen Controls and first innings data.
				showDetailsInView(firstInnings);

			}

		});
		
	  //SecondInnings Button OnCLickListener.
		// here we show the SecondInnings Data in screen ,when user taps SecondInnings Button 
		btnSecondInnings.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick (View v) {

				btnSecondInnings.setBackgroundResource(R.drawable.inningstab_tapped);
				btnFirstInnings.setBackgroundResource(R.drawable.innings_tab);
				
				//here we load the screen Controls and Second innings data.
		      showDetailsInView(secondInnings);

			}

		});

	}
/**
 * These method used to find the height of ListView Item
 * @param listView  ListView
 * @param items      listItem
 * @return            height of the ListView item.
 */
	public static int getItemHightofListView (ListView listView, int items) {

		ListAdapter mAdapter = listView.getAdapter();

		int listviewElementsheight = 0;
		// for listview total item hight
		// items = mAdapter.getCount();

		for (int i = 0; i < items; i++) {

			View childView = mAdapter.getView(i, null, listView);
			childView.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
			listviewElementsheight += childView.getMeasuredHeight();
		}

		return listviewElementsheight;

	}
/**
 * These Method used to show Screen Controls and Innings Data in ListView
 * @param listItems Innings ListItems
 */
	public void showDetailsInView (Innings listItems) {

		txtFirstInningsTeamTitle.setText(listItems.getBatting_team());
		txtSecondInningsTeamTitle.setText(listItems.getBowling_team());
		txtInningsTeamTitle.setText(listItems.getBatting_team());
		// imgInningsLogo.setBackgroundResource(R.drawable.teluguwarriors_logo_scoreboard_bowling);
		String score = listItems.getRuns() + "/" + listItems.getWickets();
		txtInningsScore.setText(score);
		txtBowlingTeamTitle.setText(listItems.getBowling_team());
		//System.out.println("batting team size "+listItems.getBatting_info().get(0).getName());
	/*	if (listItems != null ) {
			listInnings.setAdapter(new InningsAdapter(ScoreBoardActivity.this, listItems));

		}
		int height = getItemHightofListView(listInnings, listItems.getBatting_info().size());*/
		
	//	int listSize = height * (listItems.get(0).getBatting_info().size() + 1);
		
	//	listInnings.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, height));
	/*	if (listItems != null ) {
			bowlerListView.setAdapter(new BowlerListAdapter(ScoreBoardActivity.this, listItems));

		}
		height = getItemHightofListView(bowlerListView, listItems.getBowler_info().size());
		//listSize = height * (listItems.get(0).getBowler_info().size() + 1);
		bowlerListView.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, height));*/

		//txtFirstInningsTeamTitle.setText(listItems.get(0).getBatting_team());
	//	txtSecondInningsTeamTitle.setText(listItems.get(0).getBowling_team());
		//txtInningsTeamTitle.setText(listItems.get(0).getBatting_team());
	

		// imgInningsLogo.setBackgroundResource(R.drawable.teluguwarriors_logo_scoreboard_bowling);
		//String second_inns_score = listItems.get(0).getRuns() + "/" + listItems.get(0).getWickets();
	//	txtInningsScore.setText(second_inns_score);
	}
	
	
}