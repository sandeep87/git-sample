package in.ccl.ui;

import in.ccl.adapters.ScheduleAdapter;
import in.ccl.helper.Util;
import in.ccl.model.DayMatches;
import in.ccl.model.Match;

import java.util.ArrayList;
import java.util.Locale;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

public class ScheduleActivity extends TopActivity {

	private ListView listSchedule;

	private ScheduleAdapter scheduleAdapter;

	@Override
	public void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addContent(R.layout.shedule_layout);
		TextView scheduleTitle = (TextView) findViewById(R.id.schedule_title);
		Util.setTextFont(this, scheduleTitle);

		listSchedule = (ListView) findViewById(R.id.list_schedule);
		ArrayList <DayMatches> totalNumberOfMatches = new ArrayList <DayMatches>();

		DayMatches matches = new DayMatches();
		matches.setDate("09 FEB");
		matches.setDay("SATURDAY");
		matches.setPalce("KOCHI");

		ArrayList <Match> noOfMatches = new ArrayList <Match>();
		Match match = new Match();
		match.setHostingTeam("Chennai Rhinos".toUpperCase(Locale.getDefault()));
		match.setOpponentTeam("Bhojpuri Dabangs".toUpperCase(Locale.getDefault()));
		match.setTime("3:00pm - 7:00pm".toUpperCase(Locale.getDefault()));
		match.setMatchId(1);

		noOfMatches.add(match);

		match = new Match();
		match.setHostingTeam("Kerala Strikers".toUpperCase(Locale.getDefault()));
		match.setOpponentTeam("Mumbai Heroes".toUpperCase(Locale.getDefault()));
		match.setTime("7:00pm - 11:00pm".toUpperCase(Locale.getDefault()));
		match.setMatchId(2);

		noOfMatches.add(match);
		matches.setDayMatches(noOfMatches);
		totalNumberOfMatches.add(matches);

		matches = new DayMatches();
		matches.setDate("10 FEB");
		matches.setDay("SUNDAY");
		matches.setPalce("Siliguri".toUpperCase(Locale.getDefault()));
		noOfMatches = new ArrayList <Match>();

		match = new Match();
		match.setHostingTeam("Veer Marathi".toUpperCase(Locale.getDefault()));
		match.setOpponentTeam("Karnataka Bulldozers".toUpperCase(Locale.getDefault()));
		match.setTime("1:00pm - 5:00pm".toUpperCase(Locale.getDefault()));
		match.setMatchId(3);

		noOfMatches.add(match);

		match = new Match();
		match.setHostingTeam("Telugu Warriors".toUpperCase(Locale.getDefault()));
		match.setOpponentTeam("Bengal Tigers".toUpperCase(Locale.getDefault()));
		match.setTime("5:00pm - 9:00pm".toUpperCase(Locale.getDefault()));
		match.setMatchId(4);

		noOfMatches.add(match);
		matches.setDayMatches(noOfMatches);
		totalNumberOfMatches.add(matches);

		matches = new DayMatches();
		matches.setDate("16 FEB");
		matches.setDay("SATURDAY".toUpperCase(Locale.getDefault()));
		matches.setPalce("Hyderabad".toUpperCase(Locale.getDefault()));
		noOfMatches = new ArrayList <Match>();

		match = new Match();
		match.setHostingTeam(" Kerala Strikers".toUpperCase(Locale.getDefault()));
		match.setOpponentTeam("Bhojpuri Dabanggs".toUpperCase(Locale.getDefault()));
		match.setTime("3:00pm - 7:00pm".toUpperCase(Locale.getDefault()));
		match.setMatchId(5);

		noOfMatches.add(match);

		match = new Match();
		match.setHostingTeam("Chennai Rhinos".toUpperCase(Locale.getDefault()));
		match.setOpponentTeam("Karnataka Bulldozers".toUpperCase(Locale.getDefault()));
		match.setTime("7:00pm - 11:00pm".toUpperCase(Locale.getDefault()));
		match.setMatchId(6);

		noOfMatches.add(match);
		matches.setDayMatches(noOfMatches);
		totalNumberOfMatches.add(matches);

		matches = new DayMatches();
		matches.setDate("17 FEB");
		matches.setDay("Sunday".toUpperCase(Locale.getDefault()));
		matches.setPalce("Hyderabad".toUpperCase(Locale.getDefault()));
		noOfMatches = new ArrayList <Match>();

		match = new Match();
		match.setHostingTeam("Veer Marathi".toUpperCase(Locale.getDefault()));
		match.setOpponentTeam("Bengal Tigers".toUpperCase(Locale.getDefault()));
		match.setTime("3:00pm - 7:00pm".toUpperCase(Locale.getDefault()));
		match.setMatchId(7);

		noOfMatches.add(match);

		match = new Match();
		match.setHostingTeam("Telugu Warriors".toUpperCase(Locale.getDefault()));
		match.setOpponentTeam("Mumbai Heroes".toUpperCase(Locale.getDefault()));
		match.setTime("7:00pm - 11:00pm".toUpperCase(Locale.getDefault()));
		match.setMatchId(8);

		noOfMatches.add(match);
		matches.setDayMatches(noOfMatches);
		totalNumberOfMatches.add(matches);

		matches = new DayMatches();
		matches.setDate("23 FEB");
		matches.setDay("Saturday".toUpperCase(Locale.getDefault()));
		matches.setPalce("Dubai".toUpperCase(Locale.getDefault()));
		noOfMatches = new ArrayList <Match>();

		match = new Match();
		match.setHostingTeam("Kerala Strikers ".toUpperCase(Locale.getDefault()));
		match.setOpponentTeam("Bengal Tigers".toUpperCase(Locale.getDefault()));
		match.setTime("4:30pm - 8:30pm".toUpperCase(Locale.getDefault()));
		match.setMatchId(9);

		noOfMatches.add(match);

		match = new Match();
		match.setHostingTeam("Chennai Rhinos".toUpperCase(Locale.getDefault()));
		match.setOpponentTeam(" Mumbai Heroes".toUpperCase(Locale.getDefault()));
		match.setTime("8:30pm - 0:30pm".toUpperCase(Locale.getDefault()));
		match.setMatchId(10);

		noOfMatches.add(match);
		matches.setDayMatches(noOfMatches);
		totalNumberOfMatches.add(matches);

		matches = new DayMatches();
		matches.setDate("24 FEB");
		matches.setDay("Sunday".toUpperCase(Locale.getDefault()));
		matches.setPalce("Ranchi".toUpperCase(Locale.getDefault()));
		noOfMatches = new ArrayList <Match>();

		match = new Match();
		match.setHostingTeam("Telugu Warriors ".toUpperCase(Locale.getDefault()));
		match.setOpponentTeam("Karnataka Bulldozers".toUpperCase(Locale.getDefault()));
		match.setTime("3:00pm - 7:00pm".toUpperCase(Locale.getDefault()));
		match.setMatchId(11);

		noOfMatches.add(match);

		match = new Match();
		match.setHostingTeam("Veer Marathi".toUpperCase(Locale.getDefault()));
		match.setOpponentTeam("Bhojpuri Dabanggs".toUpperCase(Locale.getDefault()));
		match.setTime("7:00pm - 11:00pm".toUpperCase(Locale.getDefault()));
		match.setMatchId(12);

		noOfMatches.add(match);
		matches.setDayMatches(noOfMatches);
		totalNumberOfMatches.add(matches);

		matches = new DayMatches();
		matches.setDate("2 MAR");
		matches.setDay("Saturday".toUpperCase(Locale.getDefault()));
		matches.setPalce("Chennai".toUpperCase(Locale.getDefault()));
		noOfMatches = new ArrayList <Match>();

		match = new Match();
		match.setHostingTeam("Kerala Strikers".toUpperCase(Locale.getDefault()));
		match.setOpponentTeam("Karnataka Bulldozers".toUpperCase(Locale.getDefault()));
		match.setTime("3:00pm - 7:00pm".toUpperCase(Locale.getDefault()));
		match.setMatchId(13);

		noOfMatches.add(match);

		match = new Match();
		match.setHostingTeam("Chennai Rhinos".toUpperCase(Locale.getDefault()));
		match.setOpponentTeam("Bengal Tigers".toUpperCase(Locale.getDefault()));
		match.setTime("7:00pm - 11:00pm".toUpperCase(Locale.getDefault()));
		match.setMatchId(14);

		noOfMatches.add(match);
		matches.setDayMatches(noOfMatches);
		totalNumberOfMatches.add(matches);

		matches = new DayMatches();
		matches.setDate("3 MAR");
		matches.setDay("Sunday".toUpperCase(Locale.getDefault()));
		matches.setPalce("Pune".toUpperCase(Locale.getDefault()));
		noOfMatches = new ArrayList <Match>();

		match = new Match();
		match.setHostingTeam("Telugu warriors".toUpperCase(Locale.getDefault()));
		match.setOpponentTeam("Bhojpuri Dabanggs".toUpperCase(Locale.getDefault()));
		match.setTime("3:00pm - 7:00pm".toUpperCase(Locale.getDefault()));
		match.setMatchId(15);

		noOfMatches.add(match);

		match = new Match();
		match.setHostingTeam("Veer Marathi".toUpperCase(Locale.getDefault()));
		match.setOpponentTeam("Mumbai Heroes".toUpperCase(Locale.getDefault()));
		match.setTime("7:00pm - 11:00pm".toUpperCase(Locale.getDefault()));
		match.setMatchId(16);

		noOfMatches.add(match);
		matches.setDayMatches(noOfMatches);
		totalNumberOfMatches.add(matches);

		matches = new DayMatches();
		matches.setDate("9 MAR");
		matches.setDay("Saturday".toUpperCase(Locale.getDefault()));
		matches.setPalce("Hyderabad".toUpperCase(Locale.getDefault()));
		noOfMatches = new ArrayList <Match>();

		match = new Match();
		match.setHostingTeam("Semi Final 1".toUpperCase(Locale.getDefault()));
		match.setOpponentTeam("");
		match.setTime("3:00pm - 7:00pm".toUpperCase(Locale.getDefault()));
		match.setMatchId(10);

		noOfMatches.add(match);

		match = new Match();
		match.setHostingTeam("Semi Final 2".toUpperCase(Locale.getDefault()));
		match.setOpponentTeam("");
		match.setTime("7:00pm - 11:00pm".toUpperCase(Locale.getDefault()));
		match.setMatchId(10);

		noOfMatches.add(match);
		matches.setDayMatches(noOfMatches);
		totalNumberOfMatches.add(matches);

		matches = new DayMatches();
		matches.setDate("10 MAR");
		matches.setDay("Sunday".toUpperCase(Locale.getDefault()));
		matches.setPalce("Bengaluru".toUpperCase(Locale.getDefault()));
		noOfMatches = new ArrayList <Match>();

		match = new Match();
		match.setHostingTeam("Final".toUpperCase(Locale.getDefault()));
		match.setOpponentTeam("");
		match.setTime("7:00pm - 11:00pm".toUpperCase(Locale.getDefault()));
		match.setMatchId(10);

		noOfMatches.add(match);

		match = new Match();
		match.setHostingTeam("");
		match.setOpponentTeam("");
		match.setTime("");

		noOfMatches.add(match);
		matches.setDayMatches(noOfMatches);
		totalNumberOfMatches.add(matches);

		scheduleAdapter = new ScheduleAdapter(ScheduleActivity.this, totalNumberOfMatches);
		listSchedule.setAdapter(scheduleAdapter);
		listSchedule.setFadingEdgeLength(1);
		listSchedule.setCacheColorHint(Color.TRANSPARENT);
	}

}