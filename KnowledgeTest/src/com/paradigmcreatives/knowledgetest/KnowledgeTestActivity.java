package com.paradigmcreatives.knowledgetest;

import android.app.Activity;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class KnowledgeTestActivity extends Activity implements OnClickListener {
    /** Called when the activity is first created. */
	private Button begin, answer1, answer2, answer3, answer4;
	private Button Buttons[] = {begin, answer1, answer2, answer3, answer4};
	private TextView display_1, question;
	private String a1,a2,a3,a4 = "";
	private String[] types = {
								qusetion1, 
								question2,
								question3,
								question4,
								question5 };
	 private String correctList[] = {
	            "Correct answer 1", 
	            "Correct answer 2", 
	            "Correct answer 3", 
	            "Correct answer 4", 
	            "Correct answer 5"}; 
	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        getData();
        begin.setOnClickListener(this);
        answer1.setOnClickListener(this);
        answer2.setOnClickListener(this);
        answer3.setOnClickListener(this);
        answer4.setOnClickListener(this);
        
    }
}