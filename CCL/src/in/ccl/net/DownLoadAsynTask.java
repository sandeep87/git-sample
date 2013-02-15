package in.ccl.net;

import in.ccl.helper.ServerResponse;
import in.ccl.helper.Util;
import in.ccl.ui.R;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.client.ClientProtocolException;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.WindowManager.BadTokenException;

public class DownLoadAsynTask extends AsyncTask <String, Void, String> {

	private static final String TAG = "DownLoadAsynTask";

	private Context context;

	private ProgressDialog progressDialog;

	private ServerResponse serverResponse;

	// if this class is called from home screen so that shouldn't display progress.
	private boolean isHomeCall;

	public DownLoadAsynTask (Context context, ServerResponse serverResponse, boolean isHomeCall) {
		this.context = context;
		this.isHomeCall = isHomeCall;
		this.serverResponse = serverResponse;
	}

	@Override
	protected void onPreExecute () {

		super.onPreExecute();
		if (!isHomeCall) {
			progressDialog = new ProgressDialog(context);
			progressDialog.setMessage(context.getResources().getString(R.string.loading));
			try {
				progressDialog.show();
			}
			catch (BadTokenException e) {
			}
		}
	}

	@Override
	protected String doInBackground (String... params) {
		try {
			int timeoutConnection = 3000;
			URL url = new URL(params[0]);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setConnectTimeout(timeoutConnection);
			return Util.getInstance().convertStreamToString(con.getInputStream());
		}
		catch (ClientProtocolException e) {
			Log.e(TAG, e.toString());
		}
		catch (IOException e) {
			Log.e(TAG, e.toString());
		}
		return null;

	}

	@Override
	protected void onPostExecute (String result) {
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
		if (serverResponse != null) {
			serverResponse.setData(result);
		}

	}
}
