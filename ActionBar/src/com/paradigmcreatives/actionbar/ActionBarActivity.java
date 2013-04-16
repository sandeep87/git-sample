package com.paradigmcreatives.actionbar;


import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint({ "ValidFragment", "NewApi" })
public class ActionBarActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_action_bar);
	}

	@SuppressLint("NewApi")
	public void onAddTab () {
		final ActionBar bar = getActionBar();
		final int tabCount = bar.getTabCount();
		final String text = "Tab" +tabCount;
		bar.addTab(bar.newTab()
                .setText(text)
                .setTabListener(new TabListener(new TabContentFragment(text))));
					
	}
	
	@SuppressLint("NewApi")
	public void onRemoveTab () {
		final ActionBar bar = getActionBar();
		bar.removeTabAt(bar.getTabCount() - 1);
	}
	
	@SuppressLint("NewApi")
	public void onToggleTab() {
		final ActionBar bar = getActionBar();
		
		if (bar.getNavigationMode() == ActionBar.NAVIGATION_MODE_TABS) {
			bar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
			bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE, ActionBar.DISPLAY_SHOW_TITLE);
			}
		else {
			bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
			bar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);
		}
	}
	
	@SuppressLint("NewApi")
	public void onRemoveallTab () {
		getActionBar().removeAllTabs();
	}
	
	private class TabListener implements ActionBar.TabListener {
		
		private TabContentFragment mFragment;
		
		public TabListener(TabContentFragment fragment) {
			mFragment = fragment;
		}

		@SuppressLint("NewApi")
		@Override
		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			 Toast.makeText(ActionBarActivity.this, "Reselected!", Toast.LENGTH_SHORT).show();
			
		}

		@SuppressLint("NewApi")
		@Override
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			ft.add(R.id.fragment_content, mFragment, mFragment.getText());
			
		}

		@SuppressLint("NewApi")
		@Override
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			ft.remove(mFragment);
			
		}
		
		
	}
	private class TabContentFragment extends Fragment {
        private String mText;

        @SuppressLint("ValidFragment")
		public TabContentFragment(String text) {
            mText = text;
        }

        public String getText() {
            return mText;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View fragView = inflater.inflate(R.layout.row, container, false);

            TextView text = (TextView) fragView.findViewById(R.id.text_view);
            text.setText(mText);

            return fragView;
        }

    }

}
