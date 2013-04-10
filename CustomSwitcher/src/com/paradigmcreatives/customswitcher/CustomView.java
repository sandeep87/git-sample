package com.paradigmcreatives.customswitcher;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewSwitcher;

public class CustomView extends ViewSwitcher {
	private Context mContext;
	private URL imageUrl;
	private Bitmap mBitmap;
	private static final String TAG = "MyAppTag";
	
	public CustomView (Context context, AttributeSet attr) {
		super(context, attr);
		mContext = context;
	}
	public void setData (String name, String image) {
		View mView = getCurrentView();
		TextView mTextView = (TextView) mView.findViewById(R.id.text);
		mTextView.setText(name);
		ImageView mImageView = (ImageView) mView.findViewById(R.id.image);
		
		
			try {
				imageUrl = new URL(image);
				try {
					mBitmap = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream());
					mImageView.setImageBitmap(mBitmap);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	}

}
