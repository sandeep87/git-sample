package in.ccl.score;

import android.os.Parcel;
import android.os.Parcelable;

public class Batting implements Parcelable {

	private String name;

	private int score;

	private int balls;

	private int fours;

	private int sixes;

	private String caught;

	private String bowled;

	private String hitwicket;

	private String retiredhurt;

	private String runout;

	private String notout;

	private String stumped;

	private String candb;

	private String lbw;

	private String handledtheball;

	private String didnotbat;

	public String getName () {
		return name;
	}

	public void setName (String name) {
		this.name = name;
	}

	public int getScore () {
		return score;
	}

	public void setScore (int score) {
		this.score = score;
	}

	public int getBalls () {
		return balls;
	}

	public void setBalls (int balls) {
		this.balls = balls;
	}

	public int getFours () {
		return fours;
	}

	public void setFours (int fours) {
		this.fours = fours;
	}

	public int getSixes () {
		return sixes;
	}

	public void setSixes (int sixes) {
		this.sixes = sixes;
	}

	public String getCaught () {
		return caught;
	}

	public void setCaught (String caught) {
		this.caught = caught;
	}

	public String getBowled () {
		return bowled;
	}

	public void setBowled (String bowled) {
		this.bowled = bowled;
	}

	public String getHitwicket () {
		return hitwicket;
	}

	public void setHitwicket (String hitwicket) {
		this.hitwicket = hitwicket;
	}

	public String getRetiredhurt () {
		return retiredhurt;
	}

	public void setRetiredhurt (String retiredhurt) {
		this.retiredhurt = retiredhurt;
	}

	public String getRunout () {
		return runout;
	}

	public void setRunout (String runout) {
		this.runout = runout;
	}

	public String getNotout () {
		return notout;
	}

	public void setNotout (String notout) {
		this.notout = notout;
	}

	public String getStumped () {
		return stumped;
	}

	public void setStumped (String stumped) {
		this.stumped = stumped;
	}

	public String getCandb () {
		return candb;
	}

	public void setCandb (String candb) {
		this.candb = candb;
	}

	public String getLbw () {
		return lbw;
	}

	public void setLbw (String lbw) {
		this.lbw = lbw;
	}

	public String getHandledtheball () {
		return handledtheball;
	}

	public void setHandledtheball (String handledtheball) {
		this.handledtheball = handledtheball;
	}

	public String getDidnotbat () {
		return didnotbat;
	}

	public void setDidnotbat (String didnotbat) {
		this.didnotbat = didnotbat;
	}

	public static final Creator <Batting> CREATOR = new Creator <Batting>() {

		@Override
		public Batting createFromParcel (Parcel source) {
			return new Batting(source);
		}

		@Override
		public Batting[] newArray (int size) {
			// TODO Auto-generated method stub
			return new Batting[size];
		}
	};

	public Batting (Parcel source) {

		setName(source.readString());
		setScore(source.readInt());
		setBalls(source.readInt());
		setFours(source.readInt());
		setSixes(source.readInt());
		setCaught(source.readString());
		setBowled(source.readString());
		setHitwicket(source.readString());
		setRetiredhurt(source.readString());
		setRunout(source.readString());
		setNotout(source.readString());
		setStumped(source.readString());
		setCandb(source.readString());
		setLbw(source.readString());
		setHandledtheball(source.readString());
		setDidnotbat(source.readString());
	}

	public Batting () {

	}

	@Override
	public int describeContents () {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel (Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeInt(score);
		dest.writeInt(balls);
		dest.writeInt(fours);
		dest.writeInt(sixes);
		dest.writeString(caught);
		dest.writeString(bowled);
		dest.writeString(hitwicket);
		dest.writeString(retiredhurt);
		dest.writeString(runout);
		dest.writeString(notout);
		dest.writeString(stumped);
		dest.writeString(candb);
		dest.writeString(lbw);
		dest.writeString(handledtheball);
		dest.writeString(didnotbat);

	}
}
