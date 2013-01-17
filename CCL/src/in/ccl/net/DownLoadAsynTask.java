package in.ccl.net;

import in.ccl.helper.ServerResponse;
import in.ccl.ui.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class DownLoadAsynTask extends AsyncTask <String, Void, String> {

	private static final String TAG = "DownLoadAsynTask";

	private Context context;

	private String value;

	private StringBuilder sb;

	private ProgressDialog progressDialog;

	private ServerResponse serverResponse;

	public DownLoadAsynTask (Context context, ServerResponse serverResponse) {
		this.context = context;
		this.serverResponse = serverResponse;
	}

	@Override
	protected void onPreExecute () {

		super.onPreExecute();
	/*	progressDialog = new ProgressDialog(context);
		progressDialog.setMessage(context.getResources().getString(R.string.loading));
		progressDialog.show();
	*/}

	@Override
	protected String doInBackground (String... params) {
		try {
			DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
			HttpGet httpGet = new HttpGet(params[0]);
			HttpResponse response = defaultHttpClient.execute(httpGet);
			HttpEntity entity = response.getEntity();
			if (entity != null && response.getStatusLine().getStatusCode() == 200) {
				InputStream is = entity.getContent();
				BufferedReader br = new BufferedReader(new InputStreamReader(is));
				sb = new StringBuilder();
				String data = null;
				while ((data = br.readLine()) != null) {
					sb.append(data);
				}
				value = sb.toString();
				return value;
			}
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
