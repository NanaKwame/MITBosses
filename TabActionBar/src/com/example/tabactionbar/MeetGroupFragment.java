package com.example.tabactionbar;

import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MeetGroupFragment extends ListFragment{
	
	public static FragmentManager fm;
	private static ArrayAdapter<String> adapter;
	private static ArrayList<String> groupList;
	private String cNum;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.course_fragment, null);
		
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
	  super.onActivityCreated(savedInstanceState);
	  groupList = new ArrayList<String>();
	  adapter = new ArrayAdapter<String>(getActivity(),
		        android.R.layout.simple_list_item_1, groupList);
	  adapter.setNotifyOnChange(true);
	  setListAdapter(adapter);
	  setTaskList();
	}

	/**
	 * refreshes the grouplist that is used by the adapter to show all
	 * of the groups
	 */
	private void setTaskList() {
		ParseQuery query = new ParseQuery(MeetFragment.COURSE_FIELD);
		query.whereEqualTo("number", cNum);
		query.findInBackground(new FindCallback() {

			@Override
			public void done(List<ParseObject> tList, ParseException e) {
				if (e == null) {
					if (!tList.isEmpty()) {
						ParseObject course = tList.get(0);
						for (Object obj:course.getList("groups")) {
							groupList.add((String) obj);
						}
					}
					adapter.notifyDataSetChanged();
				}
				else {
					// do something if the query is not successful
				}
			}
			
		});
		
	}
	
	/**
	 * updates the listview for the user to notice their new group
	 */
	public void updateGroupList() {
		groupList.removeAll(groupList);
		setTaskList();
	}
	
	/**
	 * Set the course number for this fragemnt
	 * @param courseNum
	 */
	public void setCourseNum(String courseNum) {
		cNum = courseNum;
	}
	
	/**
	 * get the course number for the current course the user
	 * is focused on
	 * @return
	 */
	public String getCourseNum() {
		return cNum;
	}
	
	public ArrayList<String> getGroupList() {
		return groupList;
	}
	
}
