package in.ccl.net;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;


public class PhotoUploadTask extends AsyncTask <Bitmap, Void, String> {
  private String accessToken;
  public PhotoUploadTask(Context mContext,String accessToken){
  	
  }
	@Override
	protected String doInBackground (Bitmap... params) {
		return null;
	}

}
