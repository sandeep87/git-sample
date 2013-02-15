package in.ccl.net;

import in.ccl.helper.ServerResponse;
import in.ccl.helper.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.Log;

public class NotificationAsyncTask extends AsyncTask<String, Void, String> {

	private ServerResponse mServerResponse;

	private String mRequestType;

	private List<NameValuePair> nameValuePairs;

	private String key;

	private HttpURLConnection mConnection;

	private static final String TAG = "CommonAsyncTask";

	private String mValue = "";

	public NotificationAsyncTask(ServerResponse serverResponse,
			String requestType, List<NameValuePair> params, String key) {
		// Assigning parameter values to local variable to use entire class.
		mServerResponse = serverResponse;
		mRequestType = requestType;
		nameValuePairs = params;
		this.key = key;
	}

	/**
	 * This is override method of AsyncTask class, it will execute before
	 * starting download. So here it will display progress dialog.
	 * */

	@Override
	protected void onPreExecute() {

	}

	/**
	 * This method also override method of AsyncTask class, it is mainly used to
	 * sending request to server and getting response from the server in the
	 * background. Whenever the response is there it will return response as a
	 * string and call onPostExecute method.
	 * */

	@Override
	protected String doInBackground(String... params) {

		if (mRequestType.equals("POST")) {
			try {
				if (key.equals("PushNotification")) {
					return getPushNotificationResult(params[0], nameValuePairs);
				}
				if (key.equalsIgnoreCase("SetNotification")) {
					return getPushNotificationResult(params[0], nameValuePairs);
				}

			} catch (Exception e) {
				Log.e("NotificationAsyncTask", e.toString());
			}

		} else {
			int responseCode = 0;
			try {
				URL mURL = new URL(params[0]);
				mConnection = (HttpURLConnection) mURL.openConnection();
				mConnection.setDoOutput(true);
				mConnection.setDoInput(true);
				mConnection.setUseCaches(false);

				responseCode = mConnection.getResponseCode();
				BufferedReader reader = null;
				if (responseCode == 200) {
					if (mConnection != null) {

						reader = new BufferedReader(new InputStreamReader(
								mConnection.getInputStream()));

						StringBuilder sb = new StringBuilder();
						String strLine;
						if (reader != null) {
							while ((strLine = reader.readLine()) != null) {
								// Print the content on the console
								sb.append(strLine + "\n");
							}
						}
						mValue = sb.toString();
						System.out.println("nagesh mValue" + " " + mValue);

						return mValue;

					}// end of else
				}// end of if connection
				else {
					return null;
				}
			} catch (Exception e) {
				Log.e(TAG, e.getMessage());
			}

		}
		return null;
	}

	/**
	 * This method is an override method of the AsyncTask class, and will
	 * execute whenever server send some response. Here the progress dialog will
	 * be dismissed and set server response to appropriate ServerResponse object
	 * call. (Note: what ever class is required to send server request, the
	 * response is send to back to requested class, based on the object passed
	 * in the constructor.)
	 * */
	@Override
	protected void onPostExecute(String result) {

		if (mServerResponse != null) {
			mServerResponse.setData(result);
		}

	}

	private String getPushNotificationResult(String url,
			List<NameValuePair> params) {

		DefaultHttpClient client = new DefaultHttpClient();
		HttpPost postMethod = new HttpPost(url);

		try {
			postMethod.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse httpResponse = client.execute(postMethod);
			if (httpResponse != null) {
				if (httpResponse.getStatusLine().getStatusCode() == 200) {
					HttpEntity entity = httpResponse.getEntity();
					InputStream is = entity.getContent();
					String result = Util.getInstance()
							.convertStreamToString(is);
					return result;
				} else {
					return null;
				}
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;

	}
}