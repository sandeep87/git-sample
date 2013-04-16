package com.paradigmcreatives.networkstatus;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Toast;

public class NetworkStatusActivity extends Activity {
    /** Called when the activity is first created. */
	private ConnectivityManager mConnectivityManager;
	private NetworkInfo mNetworkInfo;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        mConnectivityManager = (ConnectivityManager)this.getSystemService(this.CONNECTIVITY_SERVICE);
        mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        boolean isConnected = mNetworkInfo.isConnectedOrConnecting();
        boolean isWifi = mNetworkInfo.getType() == mConnectivityManager.TYPE_WIFI;
        
        if(isConnected) {
        	Toast.makeText(this, "Network available", 600000).show();
        }
        else {
        	Toast.makeText(this, "Network not available", 600000).show();
        }
        if(isWifi) {
        	Toast.makeText(this, "WIFI available", 600000).show();
        }
        else {
        	Toast.makeText(this, "WIFI not available", 600000).show();
        }
        
    }
}