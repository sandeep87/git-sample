package in.ccl.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Items implements Parcelable {

	private int id;

	private String url;

	private String title;

	private String videoUrl;

	public Items () {
	}

	public String getVideoUrl () {
		return videoUrl;
	}

	public void setVideoUrl (String videoUrl) {
		this.videoUrl = videoUrl;
	}

	public int getId () {
		return id;
	}

	public void setId (int id) {
		this.id = id;
	}

	public String getUrl () {
		return url;
	}

	public void setUrl (String url) {
		this.url = url;
	}

	public String getTitle () {
		return title;
	}

	public void setTitle (String title) {
		this.title = title;
	}

	public static final Creator <Items> CREATOR = new Creator <Items>() {

		@Override
		public Items createFromParcel (Parcel source) {

			return new Items(source);
		}

		@Override
		public Items[] newArray (int size) {

			return new Items[size];
		}
	};

	@Override
	public int describeContents () {

		return 0;
	}

	public Items (Parcel in) {
		setId(in.readInt());
		setTitle(in.readString());
		setUrl(in.readString());
		setVideoUrl(in.readString());
	}

	@Override
	public void writeToParcel (Parcel dest, int flags) {
		dest.writeInt(getId());
		dest.writeString(getTitle());
		dest.writeString(getUrl());
		dest.writeString(getVideoUrl());
	}

}