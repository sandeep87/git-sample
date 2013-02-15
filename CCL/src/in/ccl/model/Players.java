package in.ccl.model;

import android.os.Parcel;
import android.os.Parcelable;


public class Players implements Parcelable{

	private int playerId;
	private String playerName;
	private String playerThumbUrl;
	private int playerTeamId;
	
	public Players () {
		
	}


	public int getPlayerId () {
		return playerId;
	}

	
	public void setPlayerId (int playerId) {
		this.playerId = playerId;
	}

	
	public String getPlayerName () {
		return playerName;
	}

	
	public void setPlayerName (String playerName) {
		this.playerName = playerName;
	}

	
	public String getPlayerThumbUrl () {
		return playerThumbUrl;
	}

	
	public void setPlayerThumbUrl (String playerThumbUrl) {
		this.playerThumbUrl = playerThumbUrl;
	}

	
	
	
	public int getPlayerTeamId() {
		return playerTeamId;
	}


	public void setPlayerTeamId(int playerTeamId) {
		this.playerTeamId = playerTeamId;
	}




	public static final Creator <Players> CREATOR = new Creator <Players>() {

		@Override
		public Players createFromParcel (Parcel source) {
			
			return new Players(source);
		}

		@Override
		public Players[] newArray (int size) {
			return new Players[size];
		}
	};
	@Override
	public int describeContents () {
		return 0;
	}


	public Players (Parcel source) {
	setPlayerId(source.readInt());
	setPlayerName(source.readString());
	setPlayerThumbUrl(source.readString());
	setPlayerTeamId(source.readInt());
	}



	@Override
	public void writeToParcel (Parcel dest, int flags) {
		dest.writeInt(getPlayerId());
		dest.writeString(getPlayerName());
		dest.writeString(getPlayerThumbUrl());
		dest.writeInt(getPlayerTeamId());
		
	}

	
}
