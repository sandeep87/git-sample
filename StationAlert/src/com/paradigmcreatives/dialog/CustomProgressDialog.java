package com.paradigmcreatives.dialog;

import android.app.ProgressDialog;
import android.content.Context;

public class CustomProgressDialog {

	private Context mContext;
	private ProgressDialog mProgressDialog;
	public CustomProgressDialog(Context context){
		mContext = context;
	}
	
	
	public void loading(String title,String message, boolean indeterminate ){
	mProgressDialog = ProgressDialog.show(mContext, title,message, true);
	}
	
	public void dismiss(){
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
		}
	}
	
}
