package in.ccl.model;

import java.util.ArrayList;

public class DayMatches {
	private ArrayList<Match> dayMatches;


	private String date;

	private String day;

	private String palce;

	public ArrayList<Match> getDayMatches() {
		return dayMatches;
	}

	public void setDayMatches(ArrayList<Match> dayMatches) {
		this.dayMatches = dayMatches;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getPalce() {
		return palce;
	}

	public void setPalce(String palce) {
		this.palce = palce;
	}

}
