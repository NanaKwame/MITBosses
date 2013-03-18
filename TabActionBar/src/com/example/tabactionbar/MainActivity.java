package com.example.tabactionbar;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MainActivity extends Activity {
	
	static ArrayList<String> testObjects = new ArrayList<String>();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent fromLoginIntent = getIntent();
		String email = fromLoginIntent.getStringExtra(LogInActivity.USERNAME);
		
		ActionBar actionBar = getActionBar();

		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		String todoLabel = getResources().getString(R.string.todo);
		Tab tab = actionBar.newTab();
		tab.setText(todoLabel);
		TabListener<ToDoFragment> tl = new TabListener<ToDoFragment>(this,
				todoLabel, ToDoFragment.class);
		tab.setTabListener(tl);
		actionBar.addTab(tab);

		String meetLabel = getResources().getString(R.string.meet);
		tab = actionBar.newTab();
		tab.setText(meetLabel);
		TabListener<MeetFragment> tl2 = new TabListener<MeetFragment>(this,
				meetLabel, MeetFragment.class);
		tab.setTabListener(tl2);
		actionBar.addTab(tab);
		
		String mapLabel = getResources().getString(R.string.map);
		tab = actionBar.newTab();
		tab.setText(mapLabel);
		TabListener<MapFragment> tl3 = new TabListener<MapFragment>(this,
				mapLabel, MapFragment.class);
		tab.setTabListener(tl3);
		actionBar.addTab(tab);
		
		String scheduleLabel = getResources().getString(R.string.schedule);
		tab = actionBar.newTab();
		tab.setText(scheduleLabel);
		TabListener<ScheduleFragment> tl4 = new TabListener<ScheduleFragment>(this,
				scheduleLabel, ScheduleFragment.class);
		tab.setTabListener(tl4);
		actionBar.addTab(tab);

	}
	
	public static void removeObject(View v) {
		ParseQuery query = new ParseQuery("TestObject");
		query.getInBackground(testObjects.get(0), new GetCallback() {
		  public void done(ParseObject object, ParseException e) {
		    if (e == null) {
		      object.deleteInBackground();
		    } else {
		      // something went wrong
		    	System.out.println("Something went wrong");
		    }
		  }
		});
		testObjects.remove(0);
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
