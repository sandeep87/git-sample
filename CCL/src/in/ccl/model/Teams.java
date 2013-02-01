package in.ccl.model;

import android.os.Parcel;
import android.os.Parcelable;


public class Teams implements Parcelable {

	private int id;
	private String name;
	private String logo;
	private String banner;
	
	public int getId () {
		return id;
	}
	
	public void setId (int id) {
		this.id = id;
	}
	
	public String getName () {
		return name;
	}
	
	public void setName (String name) {
		this.name = name;
	}
	
	public String getLogo () {
		return logo;
	}
	
	public void setLogo (String logo) {
		this.logo = logo;
	}
	
	public String getBanner () {
		return banner;
	}
	
	public void setBanner (String banner) {
		this.banner = banner;
	}

	public static final Creator <Teams> CREATOR = new Creator <Teams>() {

		@Override
		public Teams createFromParcel (Parcel source) {

			return new Teams(source);
		}

		@Override
		public Teams[] newArray (int size) {

			return new Teams[size];
		}
	};

	@Override
	public int describeContents () {
		// TODO Auto-generated method stub
		return 0;
	}

	 
	public Teams (Parcel in) {
		setId(in.readInt());
		setName(in.readString());
		setLogo(in.readString());
		setBanner(in.readString());
	
	}

	public Teams () {
	}

	@Override
	public void writeToParcel (Parcel dest, int flags) {
		dest.writeInt(getId());
		dest.writeString(getName());
		dest.writeString(getLogo());
		dest.writeString(getBanner());
	}



	
	
}
