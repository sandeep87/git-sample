package in.ccl.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Items implements Parcelable {

	private int id;

	private String url;

	private String thumbUrl;

	private int numberOfPages;
	
	private String personRoles;
	

	
	public String getPersonRoles () {
		return personRoles;
	}

	
	public void setPersonRoles (String personRoles) {
		this.personRoles = personRoles;
	}

	public int getNumberOfPages () {
		return numberOfPages;
	}

	public void setNumberOfPages (int numberOfPages) {
		this.numberOfPages = numberOfPages;
	}

	public String getThumbUrl () {
		return thumbUrl;
	}

	public void setThumbUrl (String thumb_url) {
		this.thumbUrl = thumb_url;
	}

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
		setThumbUrl(in.readString());
		setNumberOfPages(in.readInt());
		setPersonRoles(in.readString());
	}

	@Override
	public void writeToParcel (Parcel dest, int flags) {
		dest.writeInt(getId());
		dest.writeString(getTitle());
		dest.writeString(getUrl());
		dest.writeString(getVideoUrl());
		dest.writeString(getThumbUrl());
		dest.writeInt(getNumberOfPages());
		dest.writeString(getPersonRoles());
	}

}