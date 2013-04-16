package com.paradigmcreatives.mediarecorder;

import java.io.File;
import java.io.IOException;

import android.app.Activity;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MediaRecorderActivity extends Activity {
    
	MediaRecorder mediaRecorder;
	File audioFile = null;
	private static final String TAG = "Sound recording activity";
	private View startButton;
	private View stopButton;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        startButton = findViewById(R.id.start);
        stopButton = findViewById(R.id.stop);
    }
    
    public void startRecording(View view) throws IOException {
    	
    	startButton.setEnabled(false);
    	stopButton.setEnabled(true);
    	
    	File simpledir = Environment.getExternalStorageDirectory();
    	try {
    	audioFile = File.createTempFile("sound", "3gp", simpledir);
    	}
    	catch (IOException e) {
			Log.e(TAG, "sdcard access error");
			return;
		}
    	mediaRecorder = new MediaRecorder();
    	mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
    	mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
    	mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
    	mediaRecorder.setOutputFile(audioFile.getAbsolutePath());
    	mediaRecorder.prepare();
    	mediaRecorder.start();
    	
    }
    
    public void stopRecording(View view) {
    	startButton.setEnabled(true);
    	stopButton.setEnabled(false);
    	mediaRecorder.stop();
    	mediaRecorder.release();
    	addRecordingToMediaLibrary();
    }

	private void addRecordingToMediaLibrary() {
		ContentValues values = new ContentValues(4);
		long current = System.currentTimeMillis();
		
		values.put(MediaStore.Audio.Media.TITLE, "audio file" +audioFile.getName());
		values.put(MediaStore.Audio.Media.DATE_ADDED, (int)(current / 1000));
		values.put(MediaStore.Audio.Media.MIME_TYPE, "audio / 3gpp");
		values.put(MediaStore.Audio.Media.DATA, audioFile.getAbsolutePath());
		ContentResolver contentResolver = getContentResolver();
		
		Uri base = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
		Uri newUri = contentResolver.insert(base, values);
		
		sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, newUri));
		Toast.makeText(this, "AddedFile" +newUri, Toast.LENGTH_LONG).show();
		
	}
}