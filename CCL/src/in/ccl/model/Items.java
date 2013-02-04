package in.ccl.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Items implements Parcelable {

	private int id;

	private String thumbUrl;

	private int numberOfPages;

	private String personRoles;

	private String title;

	private String photoOrVideoUrl;
	
	private int albumId;

	
	public int getAlbumId () {
		return albumId;
	}

	
	public void setAlbumId (int albumId) {
		this.albumId = albumId;
	}

	public String getPhotoOrVideoUrl () {
		return photoOrVideoUrl;
	}

	public void setPhotoOrVideoUrl (String photoOrVideoUrl) {
		this.photoOrVideoUrl = photoOrVideoUrl;
	}

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

	public Items () {
	}

	public int getId () {
		return id;
	}

	public void setId (int id) {
		this.id = id;
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
		setThumbUrl(in.readString());
		setNumberOfPages(in.readInt());
		setPersonRoles(in.readString());
		setPhotoOrVideoUrl(in.readString());
		setAlbumId(in.readInt());
	}

	@Override
	public void writeToParcel (Parcel dest, int flags) {
		dest.writeInt(getId());
		dest.writeString(getTitle());
		dest.writeString(getThumbUrl());
		dest.writeInt(getNumberOfPages());
		dest.writeString(getPersonRoles());
		dest.writeString(getPhotoOrVideoUrl());
		dest.writeInt(getAlbumId());
	}

}