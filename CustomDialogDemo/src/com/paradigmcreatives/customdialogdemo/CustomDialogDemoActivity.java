package com.paradigmcreatives.customdialogdemo;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

public class CustomDialogDemoActivity extends Activity {
    /** Called when the activity is first created. */
	private Dialog dialog;
	final int DIALOG_GAME_RESTART = 100;
	private Button restart;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*setContentView(R.layout.main);*/
    }
    public Dialog onCreateDialog(int id) {
    	switch (id) {
		case DIALOG_GAME_RESTART:
			Context context = CustomDialogDemoActivity.this;
			dialog = new Dialog(context);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.main);
			restart = (Button)findViewById(R.id.restart);
			restart.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					
				}
			});
			break;

		default:
			break;
		}
		return dialog;
    	
    }
}