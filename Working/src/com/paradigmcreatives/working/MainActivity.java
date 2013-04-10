package com.paradigmcreatives.working;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher.ViewFactory;

public class MainActivity extends Activity   {

	private TextSwitcher text;
	private ImageSwitcher image;
	private Button next;
	private Context context;
	private String[] message;
	private Integer[] imag;
    static int i=0;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//DatePickerDialog dialog = new DatePickerDialog(context, datelistener, year, monthOfYear, dayOfMonth);
	     message = new String[]{"How are you","what are you doing","What about your work"};
		 imag = new Integer[]{R.drawable.ic_launcher,R.drawable.ic_launcher,R.drawable.ic_launcher};
	     text = (TextSwitcher)findViewById(R.id.textswitcher);
		 text.setFactory(new Mytextswitcher());
		 //text.setCurrentText("Hi,how are you");
		 image = (ImageSwitcher)findViewById(R.id.imageswitcher);
		 image.setFactory(new MyImageSwitcher());
		 next =(Button)findViewById(R.id.next);
		 next.setText("Next");
		 next.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(i <= message.length) {
				try  {
					text.setText(message[i]);
					image.setImageResource(imag[i]);
					i++;
				}catch(Exception e) {
					
				}
				}
				else {
					next.setClickable(false);
				}
				
			}
		});
		 Button previous =(Button)findViewById(R.id.previous);
		previous.setText("Previous");
		previous.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				try {
					--i;
					text.setText(message[i]);
				}catch(Exception e) {
					
				}
				
			}
		});

	}
	
	public class Mytextswitcher implements ViewFactory {

		public View makeView() {
			// TODO Auto-generated method stub
			//LayoutInflater inflator = LayoutInflater.from(MainActivity.this);
			//View view = inflator.inflate(R.layout.switcher, null);
			//TextView tv = (TextView)view.findViewById(R.id.text_view);
			TextView tv = new TextView(MainActivity.this);
			tv.setTextSize(20);
			return tv;
		}
		
	}
	public class MyImageSwitcher implements ViewFactory {

		
		public View makeView() {
			// TODO Auto-generated method stub
			ImageView iv = new ImageView(MainActivity.this);
			
			return iv;
		}
		
		
		
	}
	
	
}
