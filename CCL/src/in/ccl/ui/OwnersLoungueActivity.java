package in.ccl.ui;

import in.ccl.adapters.CustomAdaptor;
import in.ccl.helper.Util;
import android.graphics.Color;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class OwnersLoungueActivity extends TopActivity {

	private ListView ownerList;

	private GestureDetector mDetector;

	private int[] ownerImages = { R.drawable.owner1, R.drawable.owner2,
			R.drawable.owner1, R.drawable.owner2 };

	private int[] logos = { R.drawable.mumbaiheroeslogo_teams,
			R.drawable.chennairhinos_teamslogo,
			R.drawable.bengaltigerslogo_fullscoreboard,
			R.drawable.bengalururoyalslogo_teams };

	private String[] names = { "SUNEEL SHETTY", "VENKATESH", "SALMAN KHAN",
			"VISHAL" };

	private String[] teams = { "MUMBAI HEROS", "BANGALORE ROYALS",
			"CHENNAI RHINOS", "BANGALORE TIGERS" };

	private String[] franchaseNames = { "FRANCHISE OWNER", "FRANCHISE OWNER",
			"CHENNAI RHINOS", "CHENNAI RHINOS" };

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addContent(R.layout.owners_lounge);
		TextView ownerTitle = (TextView) findViewById(R.id.owners_title);
		TextView ownerName = (TextView) findViewById(R.id.txt_name);
		TextView ownerProf = (TextView) findViewById(R.id.txt_proffession_name);

		Util.setTextFont(this, ownerTitle);
		Util.setTextFont(this, ownerName);
		Util.setTextFont(this, ownerProf);

		ownerList = (ListView) findViewById(R.id.owners_list);
		ownerList.setFadingEdgeLength(1);
		ownerList.setCacheColorHint(Color.TRANSPARENT);
		ownerList.setAdapter(new CustomAdaptor(OwnersLoungueActivity.this,
				logos, ownerImages, names, teams, franchaseNames));
	}
}