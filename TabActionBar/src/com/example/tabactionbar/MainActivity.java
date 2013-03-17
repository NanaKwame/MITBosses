package com.example.tabactionbar;

import android.os.Build;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Fragment;
import android.app.FragmentTransaction;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MainActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ActionBar actionBar = getActionBar();

actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		String label1 = getResources().getString(R.string.label1);
		Tab tab = actionBar.newTab();
		tab.setText(label1);
		TabListener<Tab1Fragment> tl = new TabListener<Tab1Fragment>(this,
				label1, Tab1Fragment.class);
		tab.setTabListener(tl);
		actionBar.addTab(tab);

		String label2 = getResources().getString(R.string.label2);
		tab = actionBar.newTab();
		tab.setText(label2);
		TabListener<Tab2Fragment> tl2 = new TabListener<Tab2Fragment>(this,
				label2, Tab2Fragment.class);
		tab.setTabListener(tl2);
		actionBar.addTab(tab);

	}

	private class TabListener<T extends Fragment> implements
			ActionBar.TabListener {
		private Fragment mFragment;
		private final Activity mActivity;
		private final String mTag;
		private final Class<T> mClass;

		/**
		 * Constructor used each time a new tab is created.
		 * 
		 * @param activity
		 *            The host Activity, used to instantiate the fragment
		 * @param tag
		 *            The identifier tag for the fragment
		 * @param clz
		 *            The fragment's Class, used to instantiate the fragment
		 */
		public TabListener(Activity activity, String tag, Class<T> clz) {
			mActivity = activity;
			mTag = tag;
			mClass = clz;
		}

		@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			// Check if the fragment is already initialized
			if (mFragment == null) {
				// If not, instantiate and add it to the activity
				mFragment = Fragment.instantiate(mActivity, mClass.getName());
				ft.add(android.R.id.content, mFragment, mTag);
			} else {
				// If it exists, simply attach it in order to show it
				ft.attach(mFragment);
			}
		}

		@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			if (mFragment != null) {
				// Detach the fragment, because another one is being attached
				ft.detach(mFragment);
			}
		}

		public void onTabReselected(Tab tab, FragmentTransaction ft) {
			// User selected the already selected tab. Usually do nothing.
		}
	}

}
