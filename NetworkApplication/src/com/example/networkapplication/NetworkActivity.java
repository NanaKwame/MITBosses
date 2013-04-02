package com.example.networkapplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.Time;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class NetworkActivity extends Activity {

	Timer timer;
	ArrayList<Integer> throughput;
	Cursor cursor;
	TextView latency;
	TextView averageThroughput;
	int prevThrough;
	long latencyVal;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_network);
		latency = (TextView) findViewById(R.id.textView1);
		averageThroughput = (TextView) findViewById(R.id.textView2);
		
		throughput = new ArrayList<Integer>();
		timer = new Timer();
		Time now = new Time();
		now.setToNow();
		latencyVal = 0L;
		prevThrough = 0;
		Log.e("Network", "just entered");
		
		// Initial easy method of downloading a file for
		// for android version greater than or equal to Gingerbread
		if (isDownloadManagerAvailable(this)) {
			Log.e("Network", "just entered");
			String url = "http://web.mit.edu/21w.789/www/papers/griswold2004.pdf";
			DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
			request.setDescription("Network Assignment");
			request.setTitle("Griswold 2004");
			// in order for this if to run, you must use the android 3.2 to compile your app
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			    request.allowScanningByMediaScanner();
			    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
			}
			request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "networkTest.pdf");

			// get download service and enqueue file
			DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
			request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
			long id = manager.enqueue(request);
			long nowTime = now.toMillis(true);
			DownloadManager.Query query = new DownloadManager.Query();
			query.setFilterById(id);
			cursor = manager.query(query);
			cursor.moveToFirst();
			Log.e("Network", "about to begin loop");
			
			while (cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR) <= 0) {
				//keep looping
				Log.e("Network", "still no start time");
			}
			now.setToNow();
			timer.schedule(new TrackTask(), 0, 5000);
			latencyVal = now.toMillis(true) - nowTime;
		}
		
		
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.network, menu);
		return true;
	}
	
	public static boolean isDownloadManagerAvailable(Context context) {
	    try {
	        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
	            return false;
	        }
	        Intent intent = new Intent(Intent.ACTION_MAIN);
	        intent.addCategory(Intent.CATEGORY_LAUNCHER);
	        intent.setClassName("com.android.providers.downloads.ui", "com.android.providers.downloads.ui.DownloadList");
	        List<ResolveInfo> list = context.getPackageManager().queryIntentActivities(intent,
	                PackageManager.MATCH_DEFAULT_ONLY);
	        return list.size() > 0;
	    } catch (Exception e) {
	        return false;
	    }
	}
	
	private void updateViewAndParse() {
		latency.setText("Latency: " + latencyVal);
		int sum = 0;
		for (Integer i: throughput) {
			sum += i;
		}
		averageThroughput.setText("Average Througput: " + sum/throughput.size());
		
	}
	
	class TrackTask extends TimerTask {
        public void run() {
        	int currThrough = cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
            throughput.add(currThrough - prevThrough);
            prevThrough = currThrough;
            
            if (cursor.getColumnIndex(DownloadManager.COLUMN_STATUS) == DownloadManager.STATUS_SUCCESSFUL) {
            	updateViewAndParse();
            }
        }
	}

}
