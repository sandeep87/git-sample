package in.ccl.score;

import android.os.Parcel;
import android.os.Parcelable;

public class Bowler implements Parcelable {

	private int madiens;

	private int bowlerRuns;

	private int bowlerWickets;

	private String bowlerName;

	private double bowlerOvers;

	public int getBowlerRuns () {
		return bowlerRuns;
	}

	public void setBowlerRuns (int bowlerRuns) {
		this.bowlerRuns = bowlerRuns;
	}

	public int getBowlerWickets () {
		return bowlerWickets;
	}

	public void setBowlerWickets (int bowlerWickets) {
		this.bowlerWickets = bowlerWickets;
	}

	public String getBowlerName () {
		return bowlerName;
	}

	public void setBowlerName (String bowlerName) {
		this.bowlerName = bowlerName;
	}

	public double getBowlerOvers () {
		return bowlerOvers;
	}

	public void setBowlerOvers (double bowlerOvers) {
		this.bowlerOvers = bowlerOvers;
	}

	public int getMadiens () {
		return madiens;
	}

	public void setMadiens (int madiens) {
		this.madiens = madiens;
	}

	public static final Creator <Bowler> CREATOR = new Creator <Bowler>() {

		@Override
		public Bowler createFromParcel (Parcel source) {
			return new Bowler(source);
		}

		@Override
		public Bowler[] newArray (int size) {
			return new Bowler[size];
		}
	};

	@Override
	public int describeContents () {
		return 0;
	}

	public Bowler (Parcel source) {
		setMadiens(source.readInt());
		setBowlerName(source.readString());
		setBowlerOvers(source.readDouble());
		setBowlerRuns(source.readInt());
		setBowlerWickets(source.readInt());
	}

	public Bowler () {
	}

	@Override
	public void writeToParcel (Parcel dest, int flags) {
		dest.writeInt(madiens);
		dest.writeString(bowlerName);
		dest.writeDouble(bowlerOvers);
		dest.writeInt(bowlerRuns);
		dest.writeInt(bowlerWickets);

	}

}
