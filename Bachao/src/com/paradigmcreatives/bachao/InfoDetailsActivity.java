package com.paradigmcreatives.bachao;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

public class InfoDetailsActivity extends Activity {

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		((TextView) ((LinearLayout) ((ViewGroup) getWindow().getDecorView()).getChildAt(0)).getChildAt(0)).setBackgroundColor(getResources().getColor(R.color.TitleRed));

		((TextView) ((LinearLayout) ((ViewGroup) getWindow().getDecorView()).getChildAt(0)).getChildAt(0)).setGravity(Gravity.CENTER);

		setContentView(R.layout.activity_info);

		final WebView web = (WebView) findViewById(R.id.webView);
		web.setVisibility(ViewGroup.INVISIBLE);
		web.setVerticalScrollBarEnabled(false);
		web.loadData(getString(R.string.hello), "text/html", "utf-8");
		web.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageFinished (WebView view, String url) {
				super.onPageFinished(view, url);
				web.setVisibility(View.VISIBLE);
			}

		});

	}

}
