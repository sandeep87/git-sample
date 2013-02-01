package in.ccl.ui;

import in.ccl.adapters.InningsAdapter;
import in.ccl.model.Innings;

import java.util.ArrayList;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ScoreBoardActivity extends TopActivity {

	private Button btnFirstInnings;
	private Button btnSecondInnings;

	private TextView txtInningsTeamTitle;
	private ImageView imgInningsLogo;
	private TextView txtInningsScore;
	private ListView listInnings;
	private RelativeLayout layoutBowlerBetails;
	private RelativeLayout layoutBowlingTeamDetatils;
	private Innings firstInnings;
	private Innings secondInnings;

	private ArrayList<Innings> firstInningsList;
	private ArrayList<Innings> secondInningsList;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addContent(R.layout.scoreboard_layout);

		btnFirstInnings = (Button) findViewById(R.id.btn_first_innings);
		btnSecondInnings = (Button) findViewById(R.id.btn_second_innings);

		txtInningsTeamTitle = (TextView) findViewById(R.id.txt_inningsteam_title);
		imgInningsLogo = (ImageView) findViewById(R.id.img_innings_logo);
		txtInningsScore = (TextView) findViewById(R.id.txt_innings_score);
		listInnings = (ListView) findViewById(R.id.list_innings);
		layoutBowlerBetails = (RelativeLayout) findViewById(R.id.layout_bowler_innings);
		layoutBowlingTeamDetatils = (RelativeLayout) findViewById(R.id.layout_bowling_teamdetails);

		firstInningsList = new ArrayList<Innings>();
		secondInningsList = new ArrayList<Innings>();

		listInnings.setAdapter(new InningsAdapter(ScoreBoardActivity.this,
				firstInningsList, "first_innings"));

		btnFirstInnings.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				btnFirstInnings
						.setBackgroundResource(R.drawable.inningstab_tapped);
				btnSecondInnings.setBackgroundResource(R.drawable.innings_tab);

				txtInningsTeamTitle.setText("TELUGU WARRIORS");
				imgInningsLogo
						.setBackgroundResource(R.drawable.teluguwarriors_logo_scoreboard_bowling);
				txtInningsScore.setText("130/9");

				layoutBowlingTeamDetatils.setVisibility(View.VISIBLE);
				layoutBowlerBetails.setVisibility(View.VISIBLE);

				listInnings.setAdapter(new InningsAdapter(
						ScoreBoardActivity.this, firstInningsList,
						"first_innings"));

			}

		});

		btnSecondInnings.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				btnSecondInnings
						.setBackgroundResource(R.drawable.inningstab_tapped);
				btnFirstInnings.setBackgroundResource(R.drawable.innings_tab);

				txtInningsTeamTitle.setText("BENGAL TIGERS");
				imgInningsLogo
						.setBackgroundResource(R.drawable.bengalore_tigers_logo);
				txtInningsScore.setText("112/6");

				layoutBowlerBetails.setVisibility(View.GONE);
				layoutBowlingTeamDetatils.setVisibility(View.GONE);
				listInnings.setAdapter(new InningsAdapter(
						ScoreBoardActivity.this, secondInningsList,
						"second_innings"));

			}

		});

		firstInnings = new Innings();

		firstInnings.setTeam_A_Name("TELUGU WARRIORS");
		firstInnings.setTeam_A_LogoUrl("dummyUrl");
		firstInnings.setTeam_A_Score("130/9");

		firstInnings.setTeam_B_Name("BENGAL TIGERS");
		firstInnings.setTeam_B_LogoUrl("dummyUrl");
		firstInnings.setTeam_B_Score("112/6");

		firstInnings.setCurrentBowlerName("");
		firstInnings.setCurrentBowlerGivenRuns("22");
		firstInnings.setCurrentBowlerDeliveredOvers("3.5");
		firstInnings.setCurrentBowlerExtras("3");

		firstInnings.setPlayerNameOne("VENKATESH");
		firstInnings.setFirstPlayerScore("30");
		firstInnings.setFirstPlayedBalls("(24)");
		firstInningsList.add(firstInnings);

		System.out.println("kranthi getplayer runs"
				+ firstInningsList.get(0).getFirstPlayerScore());

		firstInnings = new Innings();
		firstInnings.setPlayerNameOne("SRIKANTH");
		firstInnings.setFirstPlayerScore("20");
		firstInnings.setFirstPlayedBalls("(24)");
		firstInningsList.add(firstInnings);

		firstInnings = new Innings();
		firstInnings.setPlayerNameOne("TARUN");
		firstInnings.setFirstPlayerScore("30");
		firstInnings.setFirstPlayedBalls("(24)");
		firstInningsList.add(firstInnings);

		firstInnings = new Innings();
		firstInnings.setPlayerNameOne("NITIN");
		firstInnings.setFirstPlayerScore("10");
		firstInnings.setFirstPlayedBalls("(24)");
		firstInningsList.add(firstInnings);

		firstInnings = new Innings();
		firstInnings.setPlayerNameOne("AADARSH");
		firstInnings.setFirstPlayerScore("30");
		firstInnings.setFirstPlayedBalls("(18)");
		firstInningsList.add(firstInnings);

		firstInnings = new Innings();
		firstInnings.setPlayerNameOne("TARAK RATNA");
		firstInnings.setFirstPlayerScore("14");
		firstInnings.setFirstPlayedBalls("(10)");
		firstInningsList.add(firstInnings);

		firstInnings = new Innings();
		firstInnings.setPlayerNameOne("AJAY");
		firstInnings.setFirstPlayerScore("01");
		firstInnings.setFirstPlayedBalls("(02)");
		firstInningsList.add(firstInnings);

		firstInnings = new Innings();
		firstInnings.setPlayerNameOne("SAMRAT");
		firstInnings.setFirstPlayerScore("01");
		firstInnings.setFirstPlayedBalls("(02)");
		firstInningsList.add(firstInnings);

		firstInnings = new Innings();
		firstInnings.setPlayerNameOne("RAGHU");
		firstInnings.setFirstPlayerScore("02");
		firstInnings.setFirstPlayedBalls("(02)");
		firstInningsList.add(firstInnings);

		firstInnings = new Innings();
		firstInnings.setPlayerNameOne("PRABHU");
		firstInnings.setFirstPlayerScore("00");
		firstInnings.setFirstPlayedBalls("(00)");
		firstInningsList.add(firstInnings);

		firstInnings = new Innings();
		firstInnings.setPlayerNameOne("AKHIL");
		firstInnings.setFirstPlayerScore("00");
		firstInnings.setFirstPlayedBalls("(20)");
		firstInningsList.add(firstInnings);

		/*
		 * firstInnings.setPlayerNameTwo("SRIKANTH");
		 * 
		 * firstInnings.setPlayerNameThree("TARUN");
		 * firstInnings.setPlayerNameFour("NITIN");
		 * 
		 * firstInnings.setPlayerNameFive("AADARSH");
		 * firstInnings.setPlayerNameSix("TARAK RATNA");
		 * 
		 * firstInnings.setPlayerNameSeven("AJAY");
		 * firstInnings.setPlayerNameEight("SAMRAT");
		 * 
		 * firstInnings.setPlayerNameNine("RAGHU");
		 * firstInnings.setPlayerNameTen("PRABHU");
		 * 
		 * firstInnings.setPlayerNameLeven("AKHIL");
		 */

		/*
		 * firstInnings.setSecondPlayerScore("20");
		 * 
		 * firstInnings.setThirdPlayerScore("40");
		 * firstInnings.setFourthPlayerScore("00");
		 * 
		 * firstInnings.setFifthPlayerScore("20");
		 * firstInnings.setSixthPlayerScore("20");
		 * 
		 * firstInnings.setSeventhPlayerScore("00");
		 * firstInnings.setEightPlayerScore("00");
		 * firstInnings.setNinethPlayerScore("00");
		 * firstInnings.setTenthPlayerScore("00");
		 * firstInnings.setLeventhPlayerScore("00");
		 * 
		 * firstInnings.setFirstPlayedBalls("(24)");
		 * firstInnings.setSecondPlayedBalls("(18)");
		 * 
		 * firstInnings.setThirdPlayedBalls("(24)");
		 * firstInnings.setFourthPlayedBalls("(12)");
		 * 
		 * firstInnings.setFifthPlayedBalls("(24)");
		 * firstInnings.setSixthPlayerBalls("(18)");
		 * 
		 * firstInnings.setSeventhPlayedBalls("(00)");
		 * firstInnings.setEightPlayeredBalls("(00)");
		 * firstInnings.setNinethPlayedBalls("(00)");
		 * firstInnings.setTenthPlayedBalls("(00)");
		 * firstInnings.setLeventhPlayedBalls("(00)");
		 */

		secondInnings = new Innings();
		secondInnings.setTeam_B_Name("TELUGU WARRIORS");
		secondInnings.setTeam_B_LogoUrl("dummyUrl");
		secondInnings.setTeam_B_Score("130/9");

		secondInnings.setTeam_A_Name("BENGAL TIGERS");
		secondInnings.setTeam_A_LogoUrl("dummyUrl");
		secondInnings.setTeam_A_Score("112/6");

		secondInnings.setCurrentBowlerName("");
		secondInnings.setCurrentBowlerGivenRuns("22");
		secondInnings.setCurrentBowlerDeliveredOvers("3.5");
		secondInnings.setCurrentBowlerExtras("3");

		secondInnings.setPlayerNameOne("JEET");
		secondInnings.setFirstPlayerScore("20");
		secondInnings.setFirstPlayedBalls("(24)");
		secondInningsList.add(secondInnings);

		secondInnings = new Innings();
		secondInnings.setPlayerNameOne("JISSHU");
		secondInnings.setFirstPlayerScore("20");
		secondInnings.setFirstPlayedBalls("(24)");
		secondInningsList.add(secondInnings);

		secondInnings = new Innings();
		secondInnings.setPlayerNameOne("JOY");
		secondInnings.setFirstPlayerScore("30");
		secondInnings.setFirstPlayedBalls("(24)");
		secondInningsList.add(secondInnings);

		secondInnings = new Innings();
		secondInnings.setPlayerNameOne("RAJA");
		secondInnings.setFirstPlayerScore("10");
		secondInnings.setFirstPlayedBalls("(12)");
		secondInningsList.add(secondInnings);

		secondInnings = new Innings();
		secondInnings.setPlayerNameOne("TABUN");
		secondInnings.setFirstPlayerScore("30");
		secondInnings.setFirstPlayedBalls("(12)");
		secondInningsList.add(secondInnings);

		secondInnings = new Innings();
		secondInnings.setPlayerNameOne("SAUGATA");
		secondInnings.setFirstPlayerScore("2");
		secondInnings.setFirstPlayedBalls("(14)");
		secondInningsList.add(secondInnings);

		secondInnings = new Innings();
		secondInnings.setPlayerNameOne("AMITABH");
		secondInnings.setFirstPlayerScore("00");
		secondInnings.setFirstPlayedBalls("(00)");
		secondInningsList.add(secondInnings);

		secondInnings = new Innings();
		secondInnings.setPlayerNameOne("INDRAJIT");
		secondInnings.setFirstPlayerScore("00");
		secondInnings.setFirstPlayedBalls("(00)");
		secondInningsList.add(secondInnings);

		secondInnings = new Innings();
		secondInnings.setPlayerNameOne("SUMAN");
		secondInnings.setFirstPlayerScore("00");
		secondInnings.setFirstPlayedBalls("(00)");
		secondInningsList.add(secondInnings);

		secondInnings = new Innings();
		secondInnings.setPlayerNameOne("SANDY");
		secondInnings.setFirstPlayerScore("00");
		secondInnings.setFirstPlayedBalls("(00)");
		secondInningsList.add(secondInnings);

		secondInnings = new Innings();
		secondInnings.setPlayerNameOne("KAUSHIK");
		secondInnings.setFirstPlayerScore("00");
		secondInnings.setFirstPlayedBalls("(20)");
		secondInningsList.add(secondInnings);

	}

}