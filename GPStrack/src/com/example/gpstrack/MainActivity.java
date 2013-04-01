package com.example.gpstrack;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.view.Menu;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

public class MainActivity extends Activity {
	CheckBox gps;
	CheckBox wifi;
	CheckBox both;
	TextView latView;
	TextView longView;
	TextView accuView;
	LocationManager locationManager; 
	MyListener locationListener;
	

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        locationManager= (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        latView = (TextView) findViewById(R.id.latText);
        longView = (TextView) findViewById(R.id.longText);
        accuView = (TextView) findViewById(R.id.accuText);
        locationListener =  new MyListener(latView, longView, accuView);
        gps = (CheckBox) findViewById(R.id.GPSbox);
        
        gps.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked==true){
					locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 
					(long)60*1000,
					(float)100,
					locationListener); 
				}
			}
		});
        wifi = (CheckBox) findViewById(R.id.WIFIbox);
        wifi.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked==true){
					locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 
					(long)60*1000,
					(float)100,
					locationListener); 
				}
			}
		});
        both = (CheckBox) findViewById(R.id.GPSWIFI);
        both.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked==true){
					locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 
					(long)60*1000,
					(float)100,
					locationListener); 
					locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 
							(long)60*1000,
							(float)100,
							locationListener); 
				}
				
			}
		});
        
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}

class MyListener implements LocationListener{
	private TextView lat;
	private TextView longi;
	private TextView accu;
	public MyListener(TextView lat, TextView longi, TextView Accu){
		this.lat = lat;
		this.longi=longi;
		this.accu=Accu;
	}

	@Override
	public void onLocationChanged(Location location) {
	    double latitude = location.getLatitude();
	    double longitude = location.getLongitude();
	    double accuracy = location.getAccuracy();

	    longi.setText(Double.toString(longitude));
	    lat.setText(Double.toString(latitude));
	    accu.setText(Double.toString(accuracy));
	    
	    
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	
}
