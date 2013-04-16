package com.paradigmcreatives.telephonydemo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.TextView;

public class TelePhonyDemoActivity extends Activity {
    /** Called when the activity is first created. */
	private TextView mTextView;
	private TelephonyManager mTelephonyManager;
	private PhoneStateListener mPhoneStateListener;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mTextView = (TextView) findViewById(R.id.text_view);
        mTelephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        
        mPhoneStateListener = new PhoneStateListener() {
        	public void onCallStateChanged(int state, String incomingNumber) {
        		String stateString = "N/A";
        		switch (state) {
				case TelephonyManager.CALL_STATE_IDLE:
					stateString = "Idle";
					
					break;
				case TelephonyManager.CALL_STATE_OFFHOOK:
					stateString = "OffHook";
					break;
					case TelephonyManager.CALL_STATE_RINGING:
						stateString = "Ringing";
						break;

				default:
					break;
				}
        		mTextView.append(String.format("\n Non call state changed:%s",stateString));
        	}
        };
        	mTelephonyManager.listen(mPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        	
    }     
    
}