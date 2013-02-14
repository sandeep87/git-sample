package in.ccl.helper;

import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.LinearLayout;

public class PageChangeListener implements OnPageChangeListener {

	private LinearLayout IndicatorLayout;

	private ViewPager viewPager;

	private int previousState;

	private int currentState;
	
	private int size;

	public PageChangeListener (LinearLayout pageIndicatorLayout, ViewPager pager,int size) {
		IndicatorLayout = pageIndicatorLayout;
		viewPager = pager;
		this.size = size;
		if (IndicatorLayout != null) {
			Util.setPageIndicator(0, IndicatorLayout);
		}

	}

	@Override
	public void onPageScrollStateChanged (int state) {
		int currentPage = viewPager.getCurrentItem();
		if (currentPage == size-1 || currentPage == 0) {
			previousState = currentState;
			currentState = state;
			if (previousState == 1 && currentState == 0) {
				viewPager.setCurrentItem(currentPage == 0 ? size : 0);
			}
		}
	}
  
	@Override
	public void onPageScrolled (int position, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected (int position) {
		if (IndicatorLayout != null) {
			Util.setPageIndicator(position, IndicatorLayout);
		}
	}
}
