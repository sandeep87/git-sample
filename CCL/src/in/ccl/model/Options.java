package in.ccl.model;

import android.os.Parcel;
import android.os.Parcelable;


public class Options implements Parcelable{

	private int optionId;
	private String optionValue;
	private String actionValue;
	private int actionId;
	private boolean isChecked;
	
	
	public Options () {
		
	}

	public int getOptionId () {
		return optionId;
	}

	
	public void setOptionId (int optionId) {
		this.optionId = optionId;
	}

	
	public String getOptionValue () {
		return optionValue;
	}

	
	public void setOptionValue (String optionValue) {
		this.optionValue = optionValue;
	}

	
	public String getActionValue () {
		return actionValue;
	}

	
	public void setActionValue (String actionValue) {
		this.actionValue = actionValue;
	}

	
	public int getActionId() {
		return actionId;
	}

	public void setActionId(int actionId) {
		this.actionId = actionId;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}


	public static final Creator <Options> CREATOR = new Creator <Options>() {

		@Override
		public Options createFromParcel (Parcel source) {
			
			return new Options(source);
		}

		@Override
		public Options[] newArray (int size) {
			return new Options[size];
		}
	};
	@Override
	public int describeContents () {
		return 0;
	}

	public Options (Parcel source) {
		setOptionId(source.readInt());
		setOptionValue(source.readString());
		setActionValue(source.readString());
		//setActionId(source.readInt());
		//setChecked(isChecked);
	}

	

	@Override
	public void writeToParcel (Parcel dest, int flags) {
		dest.writeInt(getOptionId());
		dest.writeString(getOptionValue());
		dest.writeString(getActionValue());
		//dest.writeInt(getActionId());
		//dest.writeboolean(getActionId());
	}

}
