package com.paradigmcreatives.crashreport;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;

/**
 * A wrapper for calling GET and POST web-services with the given input data.
 * The web-services as expected to return a <code>String</code>.
 * 
 * @author robin
 * 
 */

public class WebServiceWrapper extends
		AsyncTask<ArrayList<NameValuePair>, Void, String> {

	enum Method {
		GET, POST
	}

	private Context context;
	private String url;
	private String message;
	private ProgressDialog progressDialog;
	private String response;
	private Method method;
	private Handler handler;
	private PendingIntent intent;
//	private int responseCode;


	/**
	 * Constructor
	 * 
	 * @param context
	 *            if <code>null</code> then the <code>ProgressDialog</code> will
	 *            not be shown.
	 * @param url
	 *            The URL string for the web-service. If its invalid or
	 *            <code>null</code> then the response would contain
	 *            <code>INVALID URL</code>
	 * @param message
	 *            Message to be displayed when the process is going on
	 * @param method
	 *            Tells whether its a <code>GET</code> or a <code>POST</code>
	 *            request
	 * @param handler
	 * 			  <code>Handler</code> for communicating finish of process
	 */
	public WebServiceWrapper(Context context, String url, String message,
			Method method, Handler handler, PendingIntent intent) {
		super();
		this.context = context;

		if (url == null) {
			response = "INVALID URL";
		} else {
			this.url = url;
		}

		if (message == null || message == "") {
			message = "Please Wait...";
		} else {
			this.message = message;
		}

		if (method == null) {
			this.method = Method.POST;
		} else {
			this.method = method;
		}
		
		this.handler = handler;
		this.intent = intent;
	}

	/**
	 * Constructor
	 * 
	 * @param url
	 */
	public WebServiceWrapper(String url) {
		this(null, url, null, null, null, null);

	}

	/**
	 * Constructor
	 * 
	 * @param url
	 * @param method
	 */
	public WebServiceWrapper(String url, Method method) {
		this(null, url, null, method, null, null);
	}

	/**
	 * Constructor
	 * 
	 * @param context
	 * @param url
	 * @param message
	 */
	public WebServiceWrapper(Context context, String url, String message) {
		this(context, url, message, null, null, null);
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		if (context != null) {
			progressDialog = new ProgressDialog(context);
			progressDialog.setTitle("Please Wait");
			progressDialog.setMessage(message);
			progressDialog.show();
		}
	}

	@Override
	protected String doInBackground(ArrayList<NameValuePair>... params) {
		if(isOnline()) {
			try {
				// Check for calling the web-service
				if (url != null && params != null && params.length > 0) {
					int paramsLength = params.length;
	
					switch (method) {
					case GET:
						// add parameters
						String combinedParams = "";
						if (!params[0].isEmpty()) {
							combinedParams += "?";
							for (NameValuePair p : params[0]) {
								String paramString = p.getName() + "="
										+ URLEncoder.encode(p.getValue(), "UTF-8");
								if (combinedParams.length() > 1) {
									combinedParams += "&" + paramString;
								} else {
									combinedParams += paramString;
								}
							}
						}
	
						HttpGet request = new HttpGet(url + combinedParams);
	
						// add headers
						if (paramsLength == 2) {
							for (NameValuePair h : params[1]) {
								request.addHeader(h.getName(), h.getValue());
							}
						}
	
						executeRequest(request, url);
	
						break;
	
					case POST:
						
						HttpPost postRequest = new HttpPost(url);
						System.out.println(url);
						 
		                //add headers
						if(paramsLength == 2) {
			                for(NameValuePair h : params[1])
			                {
			                    postRequest.addHeader(h.getName(), h.getValue());
			                }
						}
		 
		                if(!params[0].isEmpty()){
		                    postRequest.setEntity(new UrlEncodedFormEntity(params[0], HTTP.UTF_8));
		                }
		 
		                executeRequest(postRequest, url);
	
						break;
					}
	
				}
	
			} catch (Exception e) {
				System.out.println("WebServiceWrapper.doInBackground()" + e);
			}
	
			//Pass a message via handler to the calling thread
			if(handler != null) {
				android.os.Message mMessage = handler.obtainMessage();
				mMessage.arg1 = 1;
				handler.sendMessage(mMessage);
			}

		}
		
		restartApp();
		return response;
	}

	@Override
	protected void onPostExecute(String response) {
		// TODO Auto-generated method stub
		super.onPostExecute(response);

		if (progressDialog != null) {
			progressDialog.dismiss();
		}
	}

	private void executeRequest(HttpUriRequest request, String url) {
		HttpClient client = new DefaultHttpClient();

		HttpResponse httpResponse;

		try {
			httpResponse = client.execute(request);
//			responseCode = httpResponse.getStatusLine().getStatusCode();
			message = httpResponse.getStatusLine().getReasonPhrase();

			HttpEntity entity = httpResponse.getEntity();

			if (entity != null) {

				InputStream instream = entity.getContent();
				response = convertStreamToString(instream);
				System.out.println("Response: " + response);
				
				// Closing the input stream will trigger connection release
				instream.close();
			}

		} catch (ClientProtocolException e) {
			client.getConnectionManager().shutdown();
			e.printStackTrace();
		} catch (IOException e) {
			client.getConnectionManager().shutdown();
			e.printStackTrace();
		}
	}
	
	private static String convertStreamToString(InputStream is) {

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	
	private void restartApp() {
		if(context != null && intent != null) {
			AlarmManager mgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
			mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 2000, intent);
			System.exit(2);
		}
	}
	
	/**
	 * Checks for internet connectivity of the device
	 * @return true if internet connectivity is there, else false
	 */
	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}
}
