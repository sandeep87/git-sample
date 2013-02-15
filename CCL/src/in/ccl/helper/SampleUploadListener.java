package in.ccl.helper;

import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.android.FacebookError;
import com.facebook.android.Util;

public class SampleUploadListener extends BaseRequestListener {

	private DelegatesResponse delegateResponse;

	public SampleUploadListener (DelegatesResponse delegateResponse) {
		this.delegateResponse = delegateResponse;
	}

	/**
	 * Called when a request completes with the given response
	 */
	@Override
	public void onComplete (String response, Object state) {
		JSONObject json = null;
		System.out.println("response"+response);
		
		try {

			json = Util.parseJson(response);
			if (json.has("id")) {
				if (!json.isNull("id")) {
					System.out.println("sucess..........");
					delegateResponse.setData("Success","SampleUploadListener");

				}
			}
			else if (json.has("error")) {
				System.out.println("error");
				delegateResponse.setData("error","SampleUploadListener");

			}

		}
		catch (JSONException e) {
			System.out.println("JSONException error");
			delegateResponse.setData("error","SampleUploadListener");
		}
		catch (FacebookError e) {
			System.out.println("FacebookError error");
			delegateResponse.setData("error","SampleUploadListener");
		}

	}

}
