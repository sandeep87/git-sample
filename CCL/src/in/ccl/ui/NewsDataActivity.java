package in.ccl.ui;

import in.ccl.helper.Util;
import in.ccl.util.Constants;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class NewsDataActivity extends Activity {

	private WebView newsDownloadImagewebView;

	private String newsDownloadImageUrl;

	private ProgressDialog mProgressDialog;

	private int deviceDisplayDensity;

	private int initialScale;

	@Override
	public void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news_download_layout);
		// assigning id to the webview
		newsDownloadImagewebView = (WebView) findViewById(R.id.news_download_webView);
		// get data from calling activity
		if (getIntent().hasExtra(Constants.EXTRA_NEWS_DOWNLOAD_IMAGE_KEY)) {
			newsDownloadImageUrl = getIntent().getStringExtra(Constants.EXTRA_NEWS_DOWNLOAD_IMAGE_KEY);
		}
		// create a progress dialog
		mProgressDialog = new ProgressDialog(this);
		// showing loading text ina progress
		mProgressDialog.setMessage(getResources().getString(R.string.loading));
		// get the setting from the webview set zoom controls
		WebSettings settings = newsDownloadImagewebView.getSettings();
		settings.setBuiltInZoomControls(true);

		deviceDisplayDensity = getResources().getDisplayMetrics().densityDpi;
		if (deviceDisplayDensity <= DisplayMetrics.DENSITY_LOW) {
			initialScale = 25;
		}
		else if (deviceDisplayDensity <= DisplayMetrics.DENSITY_MEDIUM) {
			initialScale = 40;
		}
		else if (deviceDisplayDensity <= DisplayMetrics.DENSITY_HIGH) {
			initialScale = 70;
		}

		// check network connection when loading url in a webview.if network is not available show toast message.
		if (Util.getInstance().isOnline(NewsDataActivity.this)) {

			newsDownloadImagewebView.setWebViewClient(new myWebClient());
			newsDownloadImagewebView.loadUrl(newsDownloadImageUrl);
			newsDownloadImagewebView.getSettings().setJavaScriptEnabled(true);
			newsDownloadImagewebView.setInitialScale(initialScale);
			newsDownloadImagewebView.getSettings().setSupportZoom(true);
			newsDownloadImagewebView.getSettings().setBuiltInZoomControls(true);
		}
		else {
			Toast.makeText(NewsDataActivity.this, getResources().getString(R.string.network_error_message), Toast.LENGTH_LONG).show();
			finish();
		}

	}

	public class myWebClient extends WebViewClient {

		@Override
		public void onPageStarted (WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
			mProgressDialog.show();
		}

		@Override
		public boolean shouldOverrideUrlLoading (WebView view, String url) {
			view.loadUrl(url);
			return true;

		}

		@Override
		public void onPageFinished (WebView view, String url) {
			super.onPageFinished(view, url);
			if (mProgressDialog != null) {
				mProgressDialog.dismiss();
			}

		}

		@Override
		public void onReceivedError (WebView view, int errorCode, String description, String failingUrl) {
			super.onReceivedError(view, errorCode, description, failingUrl);

			view.loadData("<html><body><h1>" + description + "</h1></body></html>", "text/html", "utf-8");
			if (mProgressDialog != null) {

				mProgressDialog.dismiss();
			}

		}
	}

	// To handle "Back" key press event for WebView to go back to previous screen.
	@Override
	public boolean onKeyDown (int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK) && newsDownloadImagewebView.canGoBack()) {
			newsDownloadImagewebView.goBack();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
