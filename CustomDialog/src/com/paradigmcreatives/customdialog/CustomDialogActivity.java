package com.paradigmcreatives.customdialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomDialogActivity extends Activity {
    /** Called when the activity is first created. */
	final Context context = this;
	private Button mButton;
	private TextView mTextView;
	private ImageView mImageView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mButton = (Button)findViewById(R.id.mainbtn);
        mButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
			final Dialog dialog = new Dialog(context);
			dialog.setContentView(R.layout.custom);
			dialog.setTitle("Android Cutom Dialog");
			
			mTextView = (TextView) dialog.findViewById(R.id.textview);
			mTextView.setText("Paradigm Creatives");
			
			mImageView = (ImageView)dialog.findViewById(R.id.imageview);
			mImageView.setImageResource(R.drawable.ic_launcher);
			
			Button dialogbtn = (Button)dialog.findViewById(R.id.okbtn);
			dialogbtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					dialog.dismiss();
					
				}
			});
			
			dialog.show();
				
			}
		});
    }
}