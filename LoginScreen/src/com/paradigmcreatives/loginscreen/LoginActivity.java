package com.paradigmcreatives.loginscreen;

import com.paradigmcreatives.loginscreen.LoginActivity;
import com.paradigmcreatives.loginscreen.R;
import com.paradigmcreatives.loginscreen.WelcomeActivity1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
	/** Called when the activity is first created. */
	private EditText usrEditText;
	private EditText pwdEditText;
	private Button submitbtn;
	private static final String TAG = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		usrEditText = (EditText) findViewById(R.id.username);
		pwdEditText = (EditText) findViewById(R.id.password);

		submitbtn = (Button) findViewById(R.id.submitbtn);

		submitbtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String usrName = usrEditText.getText().toString().trim();
				String psd = pwdEditText.getText().toString().trim();
				//Here we are going to check the validation
				if (usrName.length() > 0 && psd.length() > 0) {

					Intent intent = new Intent(LoginActivity.this,
							WelcomeActivity1.class);
					intent.putExtra("key", usrName);
					startActivityForResult(intent, 1);
				} else {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							LoginActivity.this);
					builder.setCancelable(true);
					builder.setTitle("Fill all the fields");
					builder.setInverseBackgroundForced(true);
					builder.setPositiveButton("Back",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							});
					builder.setNegativeButton("Exit",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							});
					AlertDialog alert = builder.create();
					alert.show();
				}
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1) {
			finish();
		}
	}
}
