package com.paradigmcreatives.sharedpreferences;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SharedPreferencesActivity extends Activity {
	/** Called when the activity is first created. */
	private EditText mEditText1, mEditText2;
	private TextView mTextView1, mTextView2;
	private Button mButton1, mButton2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		mEditText1 = (EditText) findViewById(R.id.edittext1);
		mEditText2 = (EditText) findViewById(R.id.edittext2);
		mTextView1 = (TextView) findViewById(R.id.savedmemo1);
		mTextView2 = (TextView) findViewById(R.id.savedmemo2);
		storage();
		mButton1 = (Button) findViewById(R.id.memorybtn1);
		mButton1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
				SharedPreferences.Editor editor = sharedPreferences.edit();
				editor.putString("MEMORY1", mEditText1.getText().toString());
				editor.commit();
				storage();
			}
		});
		mButton2 = (Button) findViewById(R.id.memorybtn2);
		mButton2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
				SharedPreferences.Editor editor = sharedPreferences.edit();
				editor.putString("MEMORY2", mEditText2.getText().toString());
				editor.commit();
				storage();

			}
		});
	}
		private void storage() {
			SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
			String savedmemor1 = sharedPreferences.getString("MEMORY1", "");
			String savedmemor2 = sharedPreferences.getString("MEMORY2", "");
			mTextView1.setText(savedmemor1);
			mTextView2.setText(savedmemor2);
		
	}
}