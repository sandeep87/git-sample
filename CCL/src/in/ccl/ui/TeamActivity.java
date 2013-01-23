package in.ccl.ui;

import in.ccl.adapters.TeamGridAdapter;
import in.ccl.helper.Util;
import in.ccl.model.TeamItems;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

public class TeamActivity extends TopActivity {

	private static final int NO_OF_PAGES = 2;

	protected RelativeLayout teamLayout;

	private ArrayList <TeamItems> items;

	private ImageView indicatorOneImage;

	private ImageView indicatorTwoImage;

	private ImageView indicatorThreeImage;

	private ImageView indicatorFourImage;	
	
	private ViewPager pager;
	
	private LinearLayout IndicatorLayout;

	private int previousState;

	private int currentState;

	@Override
	public void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addContent(R.layout.team_layout);
		pager = (ViewPager) findViewById(R.id.team_view_pager);
		teamLayout = (RelativeLayout) findViewById(R.id.team_desc);
		indicatorOneImage = (ImageView) findViewById(R.id.indicator_one);
		indicatorTwoImage = (ImageView) findViewById(R.id.indicator_two);
		indicatorThreeImage = (ImageView) findViewById(R.id.indicator_three);
		indicatorFourImage = (ImageView) findViewById(R.id.indicator_four);
		IndicatorLayout = (LinearLayout) findViewById(R.id.team_page_indicator_layout);
		TextView teamTitle = (TextView)findViewById(R.id.team_title);
		Util.setTextFont(this,teamTitle);
		items = new ArrayList <TeamItems>();

		TeamItems sevenTeam = new TeamItems();
		sevenTeam.setTeamPlayersResource(R.drawable.mumbai);
		sevenTeam.setTeamDetailsResource(R.drawable.mumbai_desc);
		items.add(sevenTeam);
 
		TeamItems thirdTeam = new TeamItems();
		thirdTeam.setTeamPlayersResource(R.drawable.chennai_players);
		thirdTeam.setTeamDetailsResource(R.drawable.chennai);
		items.add(thirdTeam);
		
		TeamItems sixTeam = new TeamItems();
		sixTeam.setTeamPlayersResource(R.drawable.telugu_team);
		sixTeam.setTeamDetailsResource(R.drawable.telugu_desc);
		items.add(sixTeam);

		TeamItems fourTeam = new TeamItems();
		fourTeam.setTeamPlayersResource(R.drawable.karnataka_team);
		fourTeam.setTeamDetailsResource(R.drawable.karnataka);
		items.add(fourTeam);


		TeamItems fiveTeam = new TeamItems();
		fiveTeam.setTeamPlayersResource(R.drawable.kerala);
		fiveTeam.setTeamDetailsResource(R.drawable.keralastrikers);
		items.add(fiveTeam);

		
		TeamItems firstTeam = new TeamItems();
		firstTeam.setTeamPlayersResource(R.drawable.bengal);
		firstTeam.setTeamDetailsResource(R.drawable.bengaltigers);
		items.add(firstTeam);

		TeamItems eightTeam = new TeamItems();
		eightTeam.setTeamPlayersResource(R.drawable.marathi);
		eightTeam.setTeamDetailsResource(R.drawable.marathi_desc);
		items.add(eightTeam);
	
		TeamItems secondTeam = new TeamItems();
		secondTeam.setTeamPlayersResource(R.drawable.bhojpuridabanngs);
		secondTeam.setTeamDetailsResource(R.drawable.bhojpuri);
		items.add(secondTeam);

	
		
		
	
		pager.setAdapter(new TeamPagerAdapter(this, items));
		
		pager.setOnPageChangeListener(new OnPageChangeListener() {

		

			@Override
			public void onPageScrolled (int pageno, float arg1, int arg2) {
				indicatorOneImage.setVisibility(View.VISIBLE);
				indicatorTwoImage.setVisibility(View.INVISIBLE);
				indicatorThreeImage.setVisibility(View.INVISIBLE);
				indicatorFourImage.setVisibility(View.INVISIBLE);

				if (pageno == 0) {
					teamLayout.setBackgroundResource(items.get(0).getTeamDetailsResource());
					ImageView teamPlayers = (ImageView) teamLayout.findViewById(R.id.team_player);
					teamPlayers.setBackgroundResource(items.get(0).getTeamPlayersResource());

				}
				else {
					teamLayout.setBackgroundResource(items.get(4).getTeamDetailsResource());
					ImageView teamPlayers = (ImageView) teamLayout.findViewById(R.id.team_player);
					teamPlayers.setBackgroundResource(items.get(4).getTeamPlayersResource());
				}
			}
		
			@Override
			public void onPageScrollStateChanged(int state) {
				int currentPage = pager.getCurrentItem();
				if (currentPage == 1 || currentPage == 0) {
					previousState = currentState;
					currentState = state;
					if (previousState == 1 && currentState == 0) {
						pager.setCurrentItem(currentPage == 0 ? 2 : 0);
					}
				}
			}

			@Override
			public void onPageSelected(int position) {
				if (IndicatorLayout != null) {
					Util.setTeamPageIndicator(position, IndicatorLayout);
				}
			}
		});
	}

	public class TeamPagerAdapter extends PagerAdapter {

		private Activity activity;

	//	private ArrayList <TeamItems> teamItems;

		private LayoutInflater inflater;

		public TeamPagerAdapter (Activity ctx, ArrayList <TeamItems> list) {
			activity = ctx;
		//	teamItems = list;
			inflater = activity.getLayoutInflater();

		}

		@Override
		public int getCount () {
			return NO_OF_PAGES;
		}

		@Override
		public View instantiateItem (ViewGroup views, final int position) {

			View imageLayout = null;
			imageLayout = inflater.inflate(R.layout.team_grid_view, null);
			GridView gridView = (GridView) imageLayout.findViewById(R.id.grid_view);
			/*
			 * final ArrayList <TeamItems> items = new ArrayList <TeamItems>(); items.add(teamItems.get(NO_OF_PAGES * position)); items.add(teamItems.get((NO_OF_PAGES * position) + 1)); items.add(teamItems.get((NO_OF_PAGES * position) + 2)); items.add(teamItems.get((NO_OF_PAGES * position) + 3));
			 */int[] teams = new int[4];
			if (position == 0) {
				teams[0] =R.drawable.mumbai_heroes; 
				teams[1] =R.drawable.ch_team_logo;
				teams[2] = R.drawable.tw_team_logo;
				teams[3] = R.drawable.kb_team_logo;
			}
			else {
				teams[0] = R.drawable.ks_team_logo;
				teams[1] = R.drawable.bengal_tigers_td_logo;
				teams[2] = R.drawable.veer_marathi;
				teams[3] =  R.drawable.bp_team_logo;
			}
			gridView.setAdapter(new TeamGridAdapter(TeamActivity.this, teams));
			gridView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick (AdapterView <?> arg0, View arg1, int pos, long arg3) {
					switch (pos) {
						case 0:
							indicatorOneImage.setVisibility(View.VISIBLE);
						  indicatorTwoImage.setVisibility(View.INVISIBLE);
							indicatorThreeImage.setVisibility(View.INVISIBLE);
							indicatorFourImage.setVisibility(View.INVISIBLE);

							break;
						case 1:
							indicatorOneImage.setVisibility(View.INVISIBLE);
							indicatorTwoImage.setVisibility(View.VISIBLE);
							indicatorThreeImage.setVisibility(View.INVISIBLE);
							indicatorFourImage.setVisibility(View.INVISIBLE);

							break;
						case 2:
							indicatorOneImage.setVisibility(View.INVISIBLE);
							indicatorTwoImage.setVisibility(View.INVISIBLE);
							indicatorThreeImage.setVisibility(View.VISIBLE);
							indicatorFourImage.setVisibility(View.INVISIBLE);

							break;
						case 3:
							indicatorOneImage.setVisibility(View.INVISIBLE);
							indicatorTwoImage.setVisibility(View.INVISIBLE);
							indicatorThreeImage.setVisibility(View.INVISIBLE);
							indicatorFourImage.setVisibility(View.VISIBLE);

							break;

						default:
							break;
					}

					int index = (3 * position) + (position + pos);
					teamLayout.setBackgroundResource(items.get(index).getTeamDetailsResource());
					ImageView teamPlayers = (ImageView) teamLayout.findViewById(R.id.team_player);
					teamPlayers.setBackgroundResource(items.get(index).getTeamPlayersResource());
				}
			});
			((ViewPager) views).addView(imageLayout, new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));

			return imageLayout;
		}

		@Override
		public boolean isViewFromObject (View view, Object object) {
			return view.equals(object);
		}

	}

}
