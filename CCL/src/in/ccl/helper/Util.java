package in.ccl.helper;

import in.ccl.ui.R;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Util {

	private static Util singleInstance;

//	private ArrayList <Items> teamMembersList;

//	private Items membersItem;

	/**
	 * private constructor
	 */
	private Util () {
	}

	/**
	 * Creating single object of this class. not required to create a new object each time when it was invoked.
	 * 
	 * @return single object of Util class.
	 */
	public static Util getInstance () {

		if (singleInstance == null) {
			synchronized (Util.class) {
				if (singleInstance == null) {
					singleInstance = new Util();
				}
			}
		}
		return singleInstance;
	}

	/**
	 * Checks network information i.e network is available or not.
	 * 
	 * @param context Context
	 * @return true if network is connected or connecting, else false.
	 */
	public boolean isOnline (Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		if (networkInfo != null) {
			return networkInfo.isConnectedOrConnecting();
		}
		return false;
	}

	/**
	 * This method is used to get the path of the SD card
	 * 
	 * @return: Path to the SD card
	 */
	public static String getSdCardPath () {
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			return Environment.getExternalStorageDirectory().getPath() + File.separator;
		}
		return null;

	}

	public static void CopyStream (InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);
			}
		}
		catch (Exception ex) {
		}
	}

	public static void setPageIndicator (int position, LinearLayout pageIndicatorLayout) {
		ImageView imgPageOne = (ImageView) pageIndicatorLayout.findViewById(R.id.page_one_indicator);
		ImageView imgPageTwo = (ImageView) pageIndicatorLayout.findViewById(R.id.page_two_indicator);
		ImageView imgPageThree = (ImageView) pageIndicatorLayout.findViewById(R.id.page_three_indicator);
		if (position == 0) {
			imgPageOne.setImageResource(R.drawable.scroll_currentpage);
			imgPageTwo.setImageResource(R.drawable.scroll);
			imgPageThree.setImageResource(R.drawable.scroll);
		}
		else if (position == 1) {
			imgPageOne.setImageResource(R.drawable.scroll);
			imgPageTwo.setImageResource(R.drawable.scroll_currentpage);
			imgPageThree.setImageResource(R.drawable.scroll);
		}
		else {
			imgPageOne.setImageResource(R.drawable.scroll);
			imgPageTwo.setImageResource(R.drawable.scroll);
			imgPageThree.setImageResource(R.drawable.scroll_currentpage);
		}
	}

	public static void setTextFont (Activity activity, TextView txtView) {
		Typeface tf = Typeface.createFromAsset(activity.getAssets(), "fonts/VonnesMediumCompressed.ttf");
		txtView.setTypeface(tf);

	}

/*	public ArrayList <Items> getChnnaiTeamMembersList () {
		teamMembersList = new ArrayList <Items>();
		membersItem = new Items();
		membersItem.setId(1);
		membersItem.setTitle("Vishal".toUpperCase());
		membersItem.setUrl("http://ccl.in/images/vishal.jpg");
		membersItem.setPersonRoles("Captain");
		teamMembersList.add(membersItem);

		membersItem = new Items();
		membersItem.setId(2);
		membersItem.setTitle("Vikraanth".toUpperCase());
		membersItem.setUrl("http://ccl.in/images/vikraanth.jpg");
		membersItem.setPersonRoles("Vice Captain");
		teamMembersList.add(membersItem);

		membersItem = new Items();
		membersItem.setId(3);
		membersItem.setTitle("Arya".toUpperCase());
		membersItem.setUrl("http://202.140.56.122/ccl/default.png");
		membersItem.setPersonRoles("Off Spinner");
		teamMembersList.add(membersItem);

		membersItem = new Items();
		membersItem.setId(4);
		membersItem.setTitle("Jiiva".toUpperCase());
		membersItem.setUrl("http://202.140.56.122/ccl/default.png");
		membersItem.setPersonRoles("Pace Bowler");
		teamMembersList.add(membersItem);

		membersItem = new Items();
		membersItem.setId(5);
		membersItem.setTitle("Bharath".toUpperCase());
		membersItem.setUrl("http://202.140.56.122/ccl/default.png");
		membersItem.setPersonRoles("Medium Pace Bowler");
		teamMembersList.add(membersItem);

		membersItem = new Items();
		membersItem.setId(6);
		membersItem.setTitle("Abbas".toUpperCase());
		membersItem.setUrl("http://202.140.56.122/ccl/default.png");
		membersItem.setPersonRoles("Left Arm Off Spinner");
		teamMembersList.add(membersItem);

		membersItem = new Items();
		membersItem.setId(7);
		membersItem.setTitle("Prithivi".toUpperCase());
		membersItem.setUrl("http://202.140.56.122/ccl/default.png");
		membersItem.setPersonRoles("Batsman");
		teamMembersList.add(membersItem);

		membersItem = new Items();
		membersItem.setId(8);
		membersItem.setTitle("Jithan Ramesh".toUpperCase());
		membersItem.setUrl("http://202.140.56.122/ccl/default.png");
		membersItem.setPersonRoles("Medium Pace Bowler");
		teamMembersList.add(membersItem);

		membersItem = new Items();
		membersItem.setId(9);
		membersItem.setTitle("Shiva".toUpperCase());
		membersItem.setUrl("http://202.140.56.122/ccl/default.png");
		membersItem.setPersonRoles("Medium Pace Bowler");
		teamMembersList.add(membersItem);

		return teamMembersList;
	}
*/
/*	public ArrayList <Items> getTeluguWarriorsTeamMembersList () {
		teamMembersList = new ArrayList <Items>();
		membersItem = new Items();
		membersItem.setId(1);
		membersItem.setTitle("Venkatesh".toUpperCase());
		membersItem.setUrl("http://ccl.in/images/venkatesh.jpg");
		membersItem.setPersonRoles("Captain");
		teamMembersList.add(membersItem);

		membersItem = new Items();
		membersItem.setId(2);
		membersItem.setTitle("Tarun".toUpperCase());
		membersItem.setUrl("http://ccl.in/images/tharun.jpg");
		membersItem.setPersonRoles("Vice Captain");
		teamMembersList.add(membersItem);

		membersItem = new Items();
		membersItem.setId(3);
		membersItem.setTitle("Srikanth".toUpperCase());
		membersItem.setUrl("http://202.140.56.122/ccl/default.png");
		membersItem.setPersonRoles("Batsman");
		teamMembersList.add(membersItem);

		membersItem = new Items();
		membersItem.setId(4);
		membersItem.setTitle("Nitin".toUpperCase());
		membersItem.setUrl("http://202.140.56.122/ccl/default.png");
		membersItem.setPersonRoles("All Rounder");
		teamMembersList.add(membersItem);

		membersItem = new Items();
		membersItem.setId(5);
		membersItem.setTitle("Aadarsh".toUpperCase());
		membersItem.setUrl("http://202.140.56.122/ccl/default.png");
		membersItem.setPersonRoles("All Rounder");
		teamMembersList.add(membersItem);

		membersItem = new Items();
		membersItem.setId(6);
		membersItem.setTitle("Tarak Ratna".toUpperCase());
		membersItem.setUrl("http://202.140.56.122/ccl/default.png");
		membersItem.setPersonRoles("Batsman");
		teamMembersList.add(membersItem);

		membersItem = new Items();
		membersItem.setId(7);
		membersItem.setTitle("Ajay".toUpperCase());
		membersItem.setUrl("http://202.140.56.122/ccl/default.png");
		membersItem.setPersonRoles("Medium Pace Bowler");
		teamMembersList.add(membersItem);

		membersItem = new Items();
		membersItem.setId(8);
		membersItem.setTitle("Samrat".toUpperCase());
		membersItem.setUrl("http://202.140.56.122/ccl/default.png");
		membersItem.setPersonRoles("Medium Pace Bowler");
		teamMembersList.add(membersItem);

		membersItem = new Items();
		membersItem.setId(9);
		membersItem.setTitle("Khayyum".toUpperCase());
		membersItem.setUrl("http://202.140.56.122/ccl/default.png");
		membersItem.setPersonRoles("All Rounder");
		teamMembersList.add(membersItem);

		return teamMembersList;
	}
*/
/*	public ArrayList <Items> getKarnatakaTeamMembersList () {
		teamMembersList = new ArrayList <Items>();
		membersItem = new Items();
		membersItem.setId(1);
		membersItem.setTitle("Sudeep".toUpperCase());
		membersItem.setUrl("http://ccl.in/images/sudeep-team.jpg");
		membersItem.setPersonRoles("Captain");
		teamMembersList.add(membersItem);

		membersItem = new Items();
		membersItem.setId(2);
		membersItem.setTitle("Dhruv".toUpperCase());
		membersItem.setUrl("http://ccl.in/images/dhruv.jpg");
		membersItem.setPersonRoles("Vice Captain");
		teamMembersList.add(membersItem);

		membersItem = new Items();
		membersItem.setId(3);
		membersItem.setTitle("Pradeep".toUpperCase());
		membersItem.setUrl("http://202.140.56.122/ccl/default.png");
		membersItem.setPersonRoles("All Rounder");
		teamMembersList.add(membersItem);

		membersItem = new Items();
		membersItem.setId(4);
		membersItem.setTitle("Rahul".toUpperCase());
		membersItem.setUrl("http://202.140.56.122/ccl/default.png");
		membersItem.setPersonRoles("All Rounder");
		teamMembersList.add(membersItem);

		membersItem = new Items();
		membersItem.setId(5);
		membersItem.setTitle("Chiranjeevi Sarja".toUpperCase());
		membersItem.setUrl("http://202.140.56.122/ccl/default.png");
		membersItem.setPersonRoles("All Rounder");
		teamMembersList.add(membersItem);

		membersItem = new Items();
		membersItem.setId(6);
		membersItem.setTitle("Tharun Chandra".toUpperCase());
		membersItem.setUrl("http://202.140.56.122/ccl/default.png");
		membersItem.setPersonRoles("Batsman");
		teamMembersList.add(membersItem);

		membersItem = new Items();
		membersItem.setId(7);
		membersItem.setTitle("Saurav".toUpperCase());
		membersItem.setUrl("http://202.140.56.122/ccl/default.png");
		membersItem.setPersonRoles("Batsman");
		teamMembersList.add(membersItem);

		membersItem = new Items();
		membersItem.setId(8);
		membersItem.setTitle("Vishwas".toUpperCase());
		membersItem.setUrl("http://202.140.56.122/ccl/default.png");
		membersItem.setPersonRoles("Batsman");
		teamMembersList.add(membersItem);

		membersItem = new Items();
		membersItem.setId(9);
		membersItem.setTitle("Karthik".toUpperCase());
		membersItem.setUrl("http://202.140.56.122/ccl/default.png");
		membersItem.setPersonRoles("All Rounder");
		teamMembersList.add(membersItem);

		return teamMembersList;
	}
*/
/*	public ArrayList <Items> getKeralaTeamMembersList () {
		teamMembersList = new ArrayList <Items>();
		membersItem = new Items();
		membersItem.setId(1);
		membersItem.setTitle("Mohan Lal".toUpperCase());
		membersItem.setUrl("http://ccl.in/images/mohan-lal.jpg");
		membersItem.setPersonRoles("Captain");
		teamMembersList.add(membersItem);

		membersItem = new Items();
		membersItem.setId(2);
		membersItem.setTitle("Indrajith".toUpperCase());
		membersItem.setUrl("http://ccl.in/images/indrajith.jpg");
		membersItem.setPersonRoles("Vice Captain");
		teamMembersList.add(membersItem);

		membersItem = new Items();
		membersItem.setId(3);
		membersItem.setTitle("Rajeev Pillai".toUpperCase());
		membersItem.setUrl("http://202.140.56.122/ccl/default.png");
		membersItem.setPersonRoles("All Rounder");
		teamMembersList.add(membersItem);

		membersItem = new Items();
		membersItem.setId(4);
		membersItem.setTitle("Nivin Pauly".toUpperCase());
		membersItem.setUrl("http://202.140.56.122/ccl/default.png");
		membersItem.setPersonRoles("Left Arm Batsman");
		teamMembersList.add(membersItem);

		membersItem = new Items();
		membersItem.setId(5);
		membersItem.setTitle("Vivek Gopan".toUpperCase());
		membersItem.setUrl("http://202.140.56.122/ccl/default.png");
		membersItem.setPersonRoles("All Rounder");
		teamMembersList.add(membersItem);

		membersItem = new Items();
		membersItem.setId(6);
		membersItem.setTitle("Manikuttan".toUpperCase());
		membersItem.setUrl("http://202.140.56.122/ccl/default.png");
		membersItem.setPersonRoles("All Rounder");
		teamMembersList.add(membersItem);

		membersItem = new Items();
		membersItem.setId(7);
		membersItem.setTitle("Bineesh Kodiyeri".toUpperCase());
		membersItem.setUrl("http://202.140.56.122/ccl/default.png");
		membersItem.setPersonRoles("All Rounder");
		teamMembersList.add(membersItem);

		membersItem = new Items();
		membersItem.setId(8);
		membersItem.setTitle("Saiju Kurup".toUpperCase());
		membersItem.setUrl("http://202.140.56.122/ccl/default.png");
		membersItem.setPersonRoles("All Rounder");
		teamMembersList.add(membersItem);

		membersItem = new Items();
		membersItem.setId(9);
		membersItem.setTitle("Prajod Kalabhavan".toUpperCase());
		membersItem.setUrl("http://202.140.56.122/ccl/default.png");
		membersItem.setPersonRoles("Batsman");
		teamMembersList.add(membersItem);

		return teamMembersList;
	}
*/
/*	public ArrayList <Items> getBangalTeamMembersList () {
		teamMembersList = new ArrayList <Items>();
		membersItem = new Items();
		membersItem.setId(1);
		membersItem.setTitle("Jeet".toUpperCase());
		membersItem.setUrl("http://ccl.in/images/jeet.jpg");
		membersItem.setPersonRoles("Captain");
		teamMembersList.add(membersItem);

		membersItem = new Items();
		membersItem.setId(2);
		membersItem.setTitle("Jisshu".toUpperCase());
		membersItem.setUrl("http://ccl.in/images/jisshu.jpg");
		membersItem.setPersonRoles("Vice Captain");
		teamMembersList.add(membersItem);

		membersItem = new Items();
		membersItem.setId(3);
		membersItem.setTitle("Indraneil".toUpperCase());
		membersItem.setUrl("http://202.140.56.122/ccl/default.png");
		membersItem.setPersonRoles("Wicket Keeper,Batsman");
		teamMembersList.add(membersItem);

		membersItem = new Items();
		membersItem.setId(4);
		membersItem.setTitle("Joy".toUpperCase());
		membersItem.setUrl("http://202.140.56.122/ccl/default.png");
		membersItem.setPersonRoles("Batsman");
		teamMembersList.add(membersItem);

		membersItem = new Items();
		membersItem.setId(5);
		membersItem.setTitle("Raja".toUpperCase());
		membersItem.setUrl("http://202.140.56.122/ccl/default.png");
		membersItem.setPersonRoles("All Rounder");
		teamMembersList.add(membersItem);

		membersItem = new Items();
		membersItem.setId(6);
		membersItem.setTitle("Tabun".toUpperCase());
		membersItem.setUrl("http://202.140.56.122/ccl/default.png");
		membersItem.setPersonRoles("All Rounder");
		teamMembersList.add(membersItem);

		membersItem = new Items();
		membersItem.setId(7);
		membersItem.setTitle("Saugata".toUpperCase());
		membersItem.setUrl("http://202.140.56.122/ccl/default.png");
		membersItem.setPersonRoles("All Rounder");
		teamMembersList.add(membersItem);

		membersItem = new Items();
		membersItem.setId(8);
		membersItem.setTitle("Babul".toUpperCase());
		membersItem.setUrl("http://202.140.56.122/ccl/default.png");
		membersItem.setPersonRoles("Batsman");
		teamMembersList.add(membersItem);

		membersItem = new Items();
		membersItem.setId(9);
		membersItem.setTitle("Amitabh".toUpperCase());
		membersItem.setUrl("http://202.140.56.122/ccl/default.png");
		membersItem.setPersonRoles("Wicket Keeper");
		teamMembersList.add(membersItem);

		return teamMembersList;
	}
*/
/*	public ArrayList <Items> getMarathiTeamMembersList () {
		teamMembersList = new ArrayList <Items>();
		membersItem = new Items();
		membersItem.setId(1);
		membersItem.setTitle("Riteish Deshmukh".toUpperCase());
		membersItem.setUrl("http://ccl.in/images/ritesh-deshmukh.jpg");
		membersItem.setPersonRoles("Captain");
		teamMembersList.add(membersItem);

		membersItem = new Items();
		membersItem.setId(2);
		membersItem.setTitle("Adinath Kothare".toUpperCase());
		membersItem.setUrl("http://202.140.56.122/ccl/default.png");
		membersItem.setPersonRoles("No Role");
		teamMembersList.add(membersItem);

		membersItem = new Items();
		membersItem.setId(3);
		membersItem.setTitle("Ajit Parab".toUpperCase());
		membersItem.setUrl("http://202.140.56.122/ccl/default.png");
		membersItem.setPersonRoles("No Role");
		teamMembersList.add(membersItem);

		membersItem = new Items();
		membersItem.setId(4);
		membersItem.setTitle("Aniket Vishwasrao".toUpperCase());
		membersItem.setUrl("http://202.140.56.122/ccl/default.png");
		membersItem.setPersonRoles("No Role");
		teamMembersList.add(membersItem);

		membersItem = new Items();
		membersItem.setId(5);
		membersItem.setTitle("Ankush Chaudhary".toUpperCase());
		membersItem.setUrl("http://202.140.56.122/ccl/default.png");
		membersItem.setPersonRoles("No Role");
		teamMembersList.add(membersItem);

		membersItem = new Items();
		membersItem.setId(6);
		membersItem.setTitle("Mahesh Manjrekar".toUpperCase());
		membersItem.setUrl("http://202.140.56.122/ccl/default.png");
		membersItem.setPersonRoles("No Role");
		teamMembersList.add(membersItem);

		membersItem = new Items();
		membersItem.setId(7);
		membersItem.setTitle("Manoje Biddvai".toUpperCase());
		membersItem.setUrl("http://202.140.56.122/ccl/default.png");
		membersItem.setPersonRoles("No Role");
		teamMembersList.add(membersItem);

		membersItem = new Items();
		membersItem.setId(8);
		membersItem.setTitle("Nupur Dhudwadkar".toUpperCase());
		membersItem.setUrl("http://202.140.56.122/ccl/default.png");
		membersItem.setPersonRoles("No Role");
		teamMembersList.add(membersItem);

		membersItem = new Items();
		membersItem.setId(9);
		membersItem.setTitle("Rahul Gore".toUpperCase());
		membersItem.setUrl("http://202.140.56.122/ccl/default.png");
		membersItem.setPersonRoles("No Role");
		teamMembersList.add(membersItem);

		return teamMembersList;
	}
*/
/*	public ArrayList <Items> getBhojpuriTeamMembersList () {
		teamMembersList = new ArrayList <Items>();
		membersItem = new Items();
		membersItem.setId(1);
		membersItem.setTitle("Manoj Tiwari".toUpperCase());
		membersItem.setUrl("http://ccl.in/images/manoj-tiwari.jpg");
		membersItem.setPersonRoles("Captain");
		teamMembersList.add(membersItem);

		membersItem = new Items();
		membersItem.setId(2);
		membersItem.setTitle("Dinesh Lal Yadav".toUpperCase());
		membersItem.setUrl("http://ccl.in/images/dinesh-lal-yadav.jpg");
		membersItem.setPersonRoles("No Role");
		teamMembersList.add(membersItem);

		membersItem = new Items();
		membersItem.setId(3);
		membersItem.setTitle("Ravi Kishen".toUpperCase());
		membersItem.setUrl("http://202.140.56.122/ccl/default.png");
		membersItem.setPersonRoles("No Role");
		teamMembersList.add(membersItem);

		membersItem = new Items();
		membersItem.setId(4);
		membersItem.setTitle("Vikrant Singh".toUpperCase());
		membersItem.setUrl("http://202.140.56.122/ccl/default.png");
		membersItem.setPersonRoles("No Role");
		teamMembersList.add(membersItem);

		membersItem = new Items();
		membersItem.setId(5);
		membersItem.setTitle("Pravesh Lal Yadav".toUpperCase());
		membersItem.setUrl("http://202.140.56.122/ccl/default.png");
		membersItem.setPersonRoles("No Role");
		teamMembersList.add(membersItem);

		membersItem = new Items();
		membersItem.setId(6);
		membersItem.setTitle("Ajhoy Sharma".toUpperCase());
		membersItem.setUrl("http://202.140.56.122/ccl/default.png");
		membersItem.setPersonRoles("No Role");
		teamMembersList.add(membersItem);

		membersItem = new Items();
		membersItem.setId(7);
		membersItem.setTitle("Uttam Tiwari".toUpperCase());
		membersItem.setUrl("http://202.140.56.122/ccl/default.png");
		membersItem.setPersonRoles("No Role");
		teamMembersList.add(membersItem);

		membersItem = new Items();
		membersItem.setId(8);
		membersItem.setTitle("Aditya Ojha".toUpperCase());
		membersItem.setUrl("http://202.140.56.122/ccl/default.png");
		membersItem.setPersonRoles("No Role");
		teamMembersList.add(membersItem);

		membersItem = new Items();
		membersItem.setId(9);
		membersItem.setTitle("Sushil Singh".toUpperCase());
		membersItem.setUrl("http://202.140.56.122/ccl/default.png");
		membersItem.setPersonRoles("No Role");
		teamMembersList.add(membersItem);

		return teamMembersList;
	}
*/
}
