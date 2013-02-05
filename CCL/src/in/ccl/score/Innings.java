package in.ccl.score;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class Innings implements Parcelable{

	private String batting_team;

	private String bowling_team;

	private int runs;

	private int wickets;

	private double overs;

	private int legbyes;

	private int wides;

	private int byes;

	private int noballs;

	private ArrayList <Batting> batting_info;

	private ArrayList <Bowler> bowler_info;

	

	public ArrayList <Batting> getBatting_info () {
		return batting_info;
	}

	public void setBatting_info (ArrayList <Batting> batting_info) {
		this.batting_info = batting_info;
	}

	public ArrayList <Bowler> getBowler_info () {
		return bowler_info;
	}

	public void setBowler_info (ArrayList <Bowler> bowler_info) {
		this.bowler_info = bowler_info;
	}

	public void setOvers (double overs) {
		this.overs = overs;
	}

	public String getBatting_team () {
		return batting_team;
	}

	public void setBatting_team (String batting_team) {
		this.batting_team = batting_team;
	}

	public String getBowling_team () {
		return bowling_team;
	}

	public void setBowling_team (String bowling_team) {
		this.bowling_team = bowling_team;
	}

	public int getRuns () {
		return runs;
	}

	public void setRuns (int runs) {
		this.runs = runs;
	}

	public int getWickets () {
		return wickets;
	}

	public void setWickets (int wickets) {
		this.wickets = wickets;
	}

	public double getOvers () {
		return overs;
	}

	public void setOvers (Double overs) {
		this.overs = overs;
	}

	public int getLegbyes () {
		return legbyes;
	}

	public void setLegbyes (int legbyes) {
		this.legbyes = legbyes;
	}

	public int getWides () {
		return wides;
	}

	public void setWides (int wides) {
		this.wides = wides;
	}

	public int getByes () {
		return byes;
	}

	public void setByes (int byes) {
		this.byes = byes;
	}

	public int getNoballs () {
		return noballs;
	}

	public void setNoballs (int noballs) {
		this.noballs = noballs;
	}

	@Override
	public int describeContents () {
		return 0;
	}
public static final Creator <Innings> CREATOR = new Creator <Innings>() {

	@Override
	public Innings createFromParcel (Parcel source) {
		return new Innings(source);
	}

	@Override
	public Innings[] newArray (int size) {
		// TODO Auto-generated method stub
		return new Innings[size];
	}
};
public Innings (Parcel source) {
	setRuns(source.readInt());
	setWickets(source.readInt());
	setOvers(source.readDouble());
	setLegbyes(source.readInt());
	setByes(source.readInt());
	setWides(source.readInt());
	setNoballs(source.readInt());
	setBatting_team(source.readString());
	setBowling_team(source.readString());
	setBatting_info(source.readArrayList(Innings.class.getClassLoader()));
	setBowler_info(source.readArrayList(Innings.class.getClassLoader()));
}
	@Override
	public void writeToParcel (Parcel dest, int flags) {
		dest.writeInt(runs);
		dest.writeInt(wickets);
		dest.writeDouble(overs);
		dest.writeInt(legbyes);
		dest.writeInt(byes);
		dest.writeInt(wides);
		dest.writeInt(noballs);
		dest.writeString(batting_team);
		dest.writeString(bowling_team);
		dest.writeList(batting_info);
		dest.writeList(bowler_info);
	}

	

	public Innings () {
		// TODO Auto-generated constructor stub
	}
	
}
