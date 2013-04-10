package com.example.internalstore;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Internal extends Activity implements OnClickListener {
	
	private Button save;
	private EditText filename, entry;
	private String FILENAME;
	private String JOUR;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.internal);
		save = (Button) findViewById(R.id.button1);
		save.setOnClickListener(this);
		filename = (EditText) findViewById(R.id.edittext1);
		entry = (EditText) findViewById(R.id.edittext2);
	}

	@Override
	public void onClick(View v) {
		FILENAME = filename.getText().toString();
		
		if (FILENAME.contentEquals("")) {
			FILENAME = "UNTITLED";
		}
		
		JOUR = entry.getText().toString();
		FileOutputStream fos;
		
		try {
			fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
			fos.write(JOUR.getBytes());
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
