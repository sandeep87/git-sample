package in.ccl.adapters;

import in.ccl.helper.Category;
import in.ccl.logging.Logger;
import in.ccl.model.TeamMember;
import in.ccl.ui.R;

import java.util.ArrayList;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.RelativeLayout.LayoutParams;

public class TeamImagePagerAdapter extends PagerAdapter {

	private LayoutInflater inflater;

	private ArrayList <TeamMember> itemsList;

	private Activity activity;

	private Category mCategory;

	public static String AlbumTitle;

	public static int photoGalleryId;

	private static final int VIEW_PAGER_PAGE_COUNT = 3;

	protected static final String TAG = "TeamImagePagerAdapter";

	private enum RequestType {
		NO_REQUEST, TEAM_MEMBERS_REQUEST, TEAM_AMBASSADORS_REQUEST;
	}

	RequestType mRequestType = RequestType.NO_REQUEST;

	@Override
	public void destroyItem (ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView((View) object);
	}

	public TeamImagePagerAdapter (Activity ctx, ArrayList <TeamMember> list, Category category) {
		activity = ctx;
		itemsList = list;
		mCategory = category;
		inflater = activity.getLayoutInflater();

	}

	@Override
	public int getCount () {
		if (itemsList.size() > 3) {
			// return VIEW_PAGER_PAGE_COUNT;
			if (itemsList.size() % 3 == 0) {
				return itemsList.size() / 3;

			}
			else {
				return (itemsList.size() / 3) + 1;

			}
		}
		return 1;
	}

	@Override
	public View instantiateItem (View view, final int position) {
		View imageLayout = null;
		// ImageView imageView = null;
		/*
		 * PhotoView imageView = null; TextView errorTitleText = null;
		 * 
		 * ImageView loadingImage = null;
		 */

		switch (mCategory) {

			case TEAM_MEMBERS:
				mRequestType = RequestType.TEAM_MEMBERS_REQUEST;
				imageLayout = load("team_members", position);
				break;
			case TEAM_AMBASSADORS:
				mRequestType = RequestType.TEAM_AMBASSADORS_REQUEST;
				imageLayout = load("team_ambassadors", position);
				break;

			default:
				break;
		}
		((ViewPager) view).addView(imageLayout, new FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
		return imageLayout;
	}

	private View load (final String from, final int position) {
		View view = inflater.inflate(R.layout.grid_view, null);
		GridView gridView = (GridView) view.findViewById(R.id.grid_view);
		final ArrayList <TeamMember> teamMembers = new ArrayList <TeamMember>();
		try {
			teamMembers.add(itemsList.get(VIEW_PAGER_PAGE_COUNT * position));
			teamMembers.add(itemsList.get((VIEW_PAGER_PAGE_COUNT * position) + 1));
			teamMembers.add(itemsList.get((VIEW_PAGER_PAGE_COUNT * position) + 2));
			// options = new DisplayImageOptions.Builder().showStubImage(R.drawable.stub_image).showImageForEmptyUri(R.drawable.image_for_empty_url).cacheInMemory().cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565).build();
		}
		catch (IndexOutOfBoundsException e) {
			Logger.info(TAG, "Loading empty page ");
		}
		TeamMemberGridAdapter adapter = new TeamMemberGridAdapter(activity, teamMembers, from);
		gridView.setAdapter(adapter);
		return view;
	}

	@Override
	public boolean isViewFromObject (View view, Object object) {
		return view.equals(object);
	}

}
