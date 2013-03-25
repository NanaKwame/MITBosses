package com.example.tabactionbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class InitListTodoFragment extends ListFragment {

	Button mButton;
	private static ArrayAdapter<String> adapter;
	private static ArrayList<String> taskList;
	private static final String className = "Tasks";

	/**
	 * Returns the custom layout view of the fragment in order
	 * to show both a ListView and Buttons
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.list_of_tasks, null);
		mButton = (Button) view.findViewById(R.id.createTask);
		mButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Log.e("InitListFragment", "The createTask method was called in this class");
//				createTask();
			}
		});
		return inflater.inflate(R.layout.list_of_tasks, null);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
	  super.onActivityCreated(savedInstanceState);
	  taskList = new ArrayList<String>();
	  adapter = new ArrayAdapter<String>(getActivity(),
		        android.R.layout.simple_list_item_1, taskList);
	  adapter.setNotifyOnChange(true);
	  setListAdapter(adapter);
	  setTaskList();
	}

	@Override
	public void onListItemClick(final ListView l, View v, final int position, long id) {
	    // Delete/remove the item from the list
		//maybe first popup an alert just to confirm
		new AlertDialog.Builder(getActivity())
	    .setTitle("Delete entry")
	    .setMessage("Are you sure you want to delete this entry?")
	    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            // continue with delete
	        	Log.e("InitList", "Pressed yes and now trying to delete");
	        	deleteTask(l.getItemAtPosition(position).toString());
	        	
	        }
	     })
	    .setNegativeButton("No", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            // simply close the alert dialog
	        	dialog.dismiss();
	        }
	     })
	     .show();
	}


	public static void updateView(Activity activity, String tableName, HashMap<String, String> map) {
		final String objectName = map.get("name");
		
		if (!taskList.contains(objectName)) {
				
			final ParseObject tableObject = new ParseObject(tableName);
			Iterator<String> keys = map.keySet().iterator();
			
			while (keys.hasNext()) {
				String key = (String) keys.next();
				String value = map.get(key);
				tableObject.put(key, value);
			}

			tableObject.saveInBackground(new SaveCallback() {

				@Override
				public void done(ParseException e) {
					// TODO Auto-generated method stub
					if (e == null) {
						//the save was successful so pass on the name
						taskList.add(objectName);
						adapter.notifyDataSetChanged();
					}
					else {
						//the save went wrong and we may need to try again
						//maybe setting a recursive loop will help or saving later
						//due to poor wifi coverage
					}
				}
				
			});				
		}
		else {
			// display an alert dialog
			Log.e("InitList", "about to show dialog");
			alertTaskDuplicate(activity);
		}
	}
	
//	private static void updateUsersTasks(String objectId) {
//		// TODO Auto-generated method stub
//		
//	}

	private static void alertTaskDuplicate(Activity activity) {

		new AlertDialog.Builder(activity)
	    .setTitle("Duplicate Entry")
	    .setMessage("Can Not Create Duplicate Entry")
	    .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int which) { 
	            // simply close the alert dialog
	        	dialog.dismiss();
	        }
	     })
	    .show();
	}

	private static void setTaskList() {
		taskList.removeAll(taskList);
		ParseQuery query = new ParseQuery(className);
		query.whereEqualTo("creator", MainActivity.getCurrentUser());
		query.findInBackground(new FindCallback() {

			@Override
			public void done(List<ParseObject> tList, ParseException e) {
				if (e == null) {
					for (ParseObject object: tList) {
						taskList.add(object.getString("name"));
					}
					adapter.notifyDataSetChanged();
				}
				else {
					// do something if the query is not successful
				}
			}
			
		});
	}

	private void deleteTask(final String objectName) {
		ParseQuery query = new ParseQuery(className);
		query.whereEqualTo("name", objectName);
		query.findInBackground(new FindCallback() {
		  public void done(List<ParseObject> objects, ParseException e) {
		    if (e == null) {
		    	try {
					objects.get(0).delete();
					taskList.remove(objectName);
					adapter.notifyDataSetChanged();
					Log.e("InitList", "deleted task");
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

		    } else {
		      // something went wrong
				Log.e("InitList", "deleted task failed");

		    }
		  }
		});
	}
}
