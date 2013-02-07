package in.ccl.score;

import android.os.Parcel;
import android.os.Parcelable;

public class MatchesResponse implements Parcelable {

	private int id;

	private String mathesName;

	public int getId () {
		return id;
	}

	public void setId (int id) {
		this.id = id;
	}

	public String getMathesName () {
		return mathesName;
	}

	public void setMathesName (String mathesName) {
		this.mathesName = mathesName;
	}

	public static final Creator <MatchesResponse> CREATOR = new Creator <MatchesResponse>() {

		@Override
		public MatchesResponse createFromParcel (Parcel source) {

			return new MatchesResponse(source);
		}

		@Override
		public MatchesResponse[] newArray (int size) {
			return new MatchesResponse[size];
		}
	};

	@Override
	public int describeContents () {

		return 0;
	}

	public MatchesResponse (Parcel in) {
		setId(in.readInt());
		setMathesName(in.readString());
	}

	public MatchesResponse () {
	}

	@Override
	public void writeToParcel (Parcel dest, int flags) {
		dest.writeInt(getId());
		dest.writeString(getMathesName());
	}

}
