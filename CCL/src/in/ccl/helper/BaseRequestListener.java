package in.ccl.helper;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import com.facebook.android.AsyncFacebookRunner.RequestListener;
import com.facebook.android.FacebookError;

/**
 * This class handles the requests
 * 
 */
public class BaseRequestListener implements RequestListener {

	/**
	 * Called when a request completes with the given response
	 */
	@Override
	public void onComplete(String response, Object state) {

	}

	/**
	 * Called when a request has a network or request error
	 */
	@Override
	public void onIOException(IOException e, Object state) {

	}

	/**
	 * Called when a request fails because the requested resource is invalid or
	 * does not exist
	 */
	@Override
	public void onFileNotFoundException(FileNotFoundException e, Object state) {

	}

	/**
	 * Called if an invalid graph path is provided
	 */
	@Override
	public void onMalformedURLException(MalformedURLException e, Object state) {

	}

	/**
	 * Called when the server-side Facebook method fails
	 */
	@Override
	public void onFacebookError(FacebookError e, Object state) {

	}

}
