package in.ccl.helper;
import in.ccl.adapters.FullPagerAdapter;
import in.ccl.ui.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class CommonAsync extends AsyncTask<String, String, String> {

	/**
	 * Instance of Context
	 */
	private Context mContext;

	/**
	 * Instance of DelegatesResponse
	 */
	private DelegatesResponse mDelegatesResponse;

	/**
	 * Instance of ProgressDialog
	 */
	private ProgressDialog mProgressDialog;

	public CommonAsync(Context context, FullPagerAdapter fullPagerAdapter) {
		mContext = context;
		mDelegatesResponse = fullPagerAdapter;
	}

	@Override
	protected void onPreExecute() {
		mProgressDialog = new ProgressDialog(mContext);
		mProgressDialog.setMessage(mContext.getResources().getString(R.string.signin));
		mProgressDialog.setCancelable(false);
		mProgressDialog.show();

	}

	@Override
	protected String doInBackground(String... params) {

		try {
			URL url = new URL(params[0]);
			URLConnection conn = url.openConnection();
			// Get the response
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					conn.getInputStream(), "UTF-8"));
			String line = null;
			String response = "";
			while ((line = rd.readLine()) != null) {
				response += line;
			}
			rd.close();
			return response;
		} catch (Exception e) {
		}
		return null;
	}// end of the doInBackground

	protected void onPostExecute(String str) {
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
		}
		if (str != null) {
			mDelegatesResponse.setData(str,"CommonAsync");
		}

	}
}