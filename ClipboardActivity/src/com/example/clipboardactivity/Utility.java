package com.example.clipboardactivity;

import android.content.Context;
import android.widget.Toast;

public class Utility {
	public static void showToastMessage(Context context, String message, int duration){
        Toast.makeText(context, message, duration).show();
    }
}
