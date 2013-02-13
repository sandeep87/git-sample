package in.ccl.score;


import android.os.Parcel;
import android.os.Parcelable;

public class ScoreBoard implements Parcelable {

	private Innings firstInningsList;

	private Innings secondInningsList;

	public Innings getFirstInningsList () {
		return firstInningsList;
	}

	public void setFirstInningsList (Innings firstInningsList) {
		this.firstInningsList = firstInningsList;
	}

	public Innings getSecondInningsList () {
		return secondInningsList;
	}

	public void setSecondInningsList (Innings secondInningsList) {
		this.secondInningsList = secondInningsList;
	}

	@Override
	public int describeContents () {
		return 0;
	}

	@Override
	public void writeToParcel (Parcel dest, int flags) {

		dest.writeValue(getFirstInningsList());
		dest.writeValue(getSecondInningsList());
	}

	public static final Creator <ScoreBoard> CREATOR = new Creator <ScoreBoard>() {

		@Override
		public ScoreBoard createFromParcel (Parcel source) {
			return new ScoreBoard(source);
		}

		@Override
		public ScoreBoard[] newArray (int size) {
			return new ScoreBoard[size];
		}
	};

	public ScoreBoard (Parcel source) {
		setFirstInningsList((Innings)source.readValue(ScoreBoard.class.getClassLoader()));
		setSecondInningsList((Innings)source.readValue(ScoreBoard.class.getClassLoader()));
	}

	public ScoreBoard () {
	}
}
