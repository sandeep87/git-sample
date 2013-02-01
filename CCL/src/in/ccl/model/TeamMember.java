package in.ccl.model;

import android.os.Parcel;
import android.os.Parcelable;

public class TeamMember implements Parcelable{

	private int id;

	private String personName;

	private String memberThumbUrl;

	private String teamName;

	private String role;

	


	public int getId () {
		return id;
	}

	
	public void setId (int id) {
		this.id = id;
	}

	
	public String getPersonName () {
		return personName;
	}

	
	public void setPersonName (String personName) {
		this.personName = personName;
	}

	
	public String getMemberThumbUrl () {
		return memberThumbUrl;
	}

	
	public void setMemberThumbUrl (String memberThumbUrl) {
		this.memberThumbUrl = memberThumbUrl;
	}

	
	public String getTeamName () {
		return teamName;
	}

	
	public void setTeamName (String teamName) {
		this.teamName = teamName;
	}

	
	public String getRole () {
		return role;
	}

	
	public void setRole (String role) {
		this.role = role;
	}

 public static final Creator <TeamMember> CREATOR = new Creator <TeamMember>() {

	@Override
	public TeamMember createFromParcel (Parcel source) {
		
		return new TeamMember(source);
	}

	@Override
	public TeamMember[] newArray (int size) {
		
		return new TeamMember[size];
	}
};
	@Override
	public int describeContents () {
		// TODO Auto-generated method stub
		return 0;
	}


	public TeamMember (Parcel source) {

		setId(source.readInt());
		setTeamName(source.readString());
		setPersonName(source.readString());
		setMemberThumbUrl(source.readString());
		setRole(source.readString());
	}

	public TeamMember () {
		// TODO Auto-generated constructor stub
	}


	@Override
	public void writeToParcel (Parcel dest, int flags) {

		dest.writeInt(getId());
		dest.writeString(getTeamName());
		dest.writeString(getPersonName());
		dest.writeString(getMemberThumbUrl());
		dest.writeString(getRole());
	}

	
	
}
