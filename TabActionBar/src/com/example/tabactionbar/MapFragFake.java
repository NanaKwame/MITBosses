package com.example.tabactionbar;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseObject;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MapFragFake extends Fragment {
	private GoogleMap mMap;
	private static HashMap<String, ArrayList<Double>> scheduleMarkersOnMap = new HashMap<String, ArrayList<Double>>();
	private static HashMap<String, ArrayList<Double>> todoMarkersOnMap = new HashMap<String, ArrayList<Double>>();
	private static HashMap<String, ArrayList<Double>> meetMarkersOnMap= new HashMap<String, ArrayList<Double>>();
	private static ArrayList<String> scheduleMarkerNames = new ArrayList<String>();
	private static ArrayList<String> todoMarkerNames = new ArrayList<String>();
	private static ArrayList<String> meetMarkerNames= new ArrayList<String>();
	private ArrayList<Marker> scheduleMarkers= new ArrayList<Marker>();
	private ArrayList<Marker> todoMarkers= new ArrayList<Marker>();
	private static ArrayList<Marker> meetMarkers = new ArrayList<Marker>();
	private CheckBox scheduleBox ;
	private CheckBox todoBox ;
	private CheckBox meetBox ;
	private CheckBox meBox  ;
	private View view;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//onClick setup for the check boxes
		View view1 = inflater.inflate(R.layout.mapfragfake, container, false);
		this.view=view1;
		scheduleBox = (CheckBox) view.findViewById(R.id.scheduleBox);
		scheduleBox.setBackgroundColor(Color.GREEN);
		todoBox = (CheckBox) view.findViewById(R.id.toDoBox);
		todoBox.setBackgroundColor(Color.RED);
		meetBox= (CheckBox) view.findViewById(R.id.meetBox);
		meetBox.setBackgroundColor(Color.YELLOW);
		meBox = (CheckBox) view.findViewById(R.id.meBox);
		meBox.setBackgroundColor(Color.BLUE);
		scheduleBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(arg1==true){
					scheduleMarkers.clear();
					for (int i = 0; i < scheduleMarkerNames.size(); i++) {
						String name = scheduleMarkerNames.get(i);
						scheduleMarkers.add(mMap.addMarker(new MarkerOptions().position(new LatLng(scheduleMarkersOnMap.get(name).get(0), scheduleMarkersOnMap.get(name).get(1))).title(name).icon(BitmapDescriptorFactory.defaultMarker(120))));
					}
					setUpMapIfNeeded();}
				else{
					for (int i = 0; i < scheduleMarkers.size(); i++) {
						Marker mark=scheduleMarkers.get(i);
						mark.remove();
					}
				}
			} 
        });
		todoBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(arg1==true){
					todoMarkers.clear();
					for (int i = 0; i < todoMarkerNames.size(); i++) {
						String name = todoMarkerNames.get(i);
						todoMarkers.add(mMap.addMarker(new MarkerOptions().position(new LatLng(todoMarkersOnMap.get(name).get(0), todoMarkersOnMap.get(name).get(1))).title(name).icon(BitmapDescriptorFactory.defaultMarker(0))));
					}
					setUpMapIfNeeded();}
				else{
					for (int i = 0; i < todoMarkers.size(); i++) {
						Marker mark=todoMarkers.get(i);
						mark.remove();
					}
				}
			} 
        });
		meetBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(arg1==true){
					meetMarkers.clear();
					for (int i = 0; i < meetMarkerNames.size(); i++) {
						String name = meetMarkerNames.get(i);
						meetMarkers.add(mMap.addMarker(new MarkerOptions().position(new LatLng(meetMarkersOnMap.get(name).get(0), meetMarkersOnMap.get(name).get(1))).title(name).icon(BitmapDescriptorFactory.defaultMarker(60))));
					}
					setUpMapIfNeeded();}
				else{
					for (int i = 0; i < meetMarkers.size(); i++) {
						Marker mark=meetMarkers.get(i);
						mark.remove();
					}
				}
			} 
        });
		meBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(arg1==true)
					mMap.setMyLocationEnabled(true);
				else
					mMap.setMyLocationEnabled(false);
			} 
        });
		
		setUpMapIfNeeded();
		
		// logging
		ParseObject log = new ParseObject("MapFragment");
		log.put("action", "just arrived");
		log.saveInBackground();
				
		return view;
	}
	
	
	
	private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
        	mMap = ((com.google.android.gms.maps.MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        	// Check if we were successful in obtaining the map.
        	if (mMap != null) {
        		makeMarkers();
        	}
        }

    }
	
	private void makeMarkers(){
		addMarkerMeet("6.01 Pset Group", 42.34909, -71.08953, "test");
		addMarkerSchedule("Buy Milk", 42.34947, -71.08695, "test");
		addMarkerTodo("Do 6.02 Pset", 42.34573, -71.08850, "test");
	}
	
	public static void addMarkerSchedule(String name, Double lat, Double longi, String type){
		ArrayList<Double> pseudoPos = new ArrayList<Double>();
		pseudoPos.add(lat);
		pseudoPos.add(longi);
		scheduleMarkersOnMap.put(name,pseudoPos);
		scheduleMarkerNames.add(name);
		
		// logging
		ParseObject log = new ParseObject("MapFragment");
		log.put("action", "add marker schedule");
		log.saveInBackground();
	}
	
	public static void deleteAllMarkerSchedule(){
		scheduleMarkersOnMap= new HashMap<String, ArrayList<Double>>();
		scheduleMarkerNames= new ArrayList<String>();
	}
	
	public static void deleteMarkerSchedule(String name){
		scheduleMarkersOnMap.remove(name);
		scheduleMarkerNames.remove(name);
		
		// logging
		ParseObject log = new ParseObject("MapFragment");
		log.put("action", "delete marker schedule");
		log.saveInBackground();
	}
	
	public static void addMarkerTodo(String name, Double lat, Double longi, String type){
		ArrayList<Double> pseudoPos = new ArrayList<Double>();
		pseudoPos.add(lat);
		pseudoPos.add(longi);
		todoMarkersOnMap.put(name,pseudoPos);
		todoMarkerNames.add(name);
		
		// logging
		ParseObject log = new ParseObject("MapFragment");
		log.put("action", "add marker todo");
		log.saveInBackground();
	}
	
	public static void deleteAllMarkerTodo(){
		todoMarkersOnMap= new HashMap<String, ArrayList<Double>>();
		todoMarkerNames= new ArrayList<String>();
	}
	
	public static void deleteMarkerTodo(String name){
		todoMarkersOnMap.remove(name);
		todoMarkerNames.remove(name);
		
		// logging
		ParseObject log = new ParseObject("MapFragment");
		log.put("action", "delete marker todo");
		log.saveInBackground();
	}
	
	public static void addMarkerMeet(String name, Double lat, Double longi, String type){
		ArrayList<Double> pseudoPos = new ArrayList<Double>();
		pseudoPos.add(lat);
		pseudoPos.add(longi);
		meetMarkersOnMap.put(name,pseudoPos);
		meetMarkerNames.add(name);
		
		// logging
		ParseObject log = new ParseObject("MapFragment");
		log.put("action", "add marker meet");
		log.saveInBackground();
	}
	
	public static void deleteAllMarkerMeet(){
		meetMarkersOnMap= new HashMap<String, ArrayList<Double>>();
		meetMarkerNames= new ArrayList<String>();
	}
	
	public static void deleteMarkerMeet(String name){
		meetMarkersOnMap.remove(name);
		meetMarkerNames.remove(name);
		
		// logging
		ParseObject log = new ParseObject("MapFragment");
		log.put("action", "delete marker meet");
		log.saveInBackground();
	}
	
	public static void clearMap(){
		deleteAllMarkerMeet();
		deleteAllMarkerSchedule();
		deleteAllMarkerTodo();
	}
	
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		scheduleBox = (CheckBox) view.findViewById(R.id.scheduleBox);
		scheduleBox.setBackgroundColor(Color.GREEN);
		todoBox = (CheckBox) view.findViewById(R.id.toDoBox);
		todoBox.setBackgroundColor(Color.RED);
		meetBox= (CheckBox) view.findViewById(R.id.meetBox);
		meetBox.setBackgroundColor(Color.YELLOW);
		meBox = (CheckBox) view.findViewById(R.id.meBox);
		meBox.setBackgroundColor(Color.BLUE);
		scheduleBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(arg1==true){
					scheduleMarkers.clear();
					for (int i = 0; i < scheduleMarkerNames.size(); i++) {
						String name = scheduleMarkerNames.get(i);
						scheduleMarkers.add(mMap.addMarker(new MarkerOptions().position(new LatLng(scheduleMarkersOnMap.get(name).get(0), scheduleMarkersOnMap.get(name).get(1))).title(name).icon(BitmapDescriptorFactory.defaultMarker(120))));
					}
					setUpMapIfNeeded();}
				else{
					for (int i = 0; i < scheduleMarkers.size(); i++) {
						Marker mark=scheduleMarkers.get(i);
						mark.remove();
					}
				}
			} 
        });
		todoBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(arg1==true){
					todoMarkers.clear();
					for (int i = 0; i < todoMarkerNames.size(); i++) {
						String name = todoMarkerNames.get(i);
						todoMarkers.add(mMap.addMarker(new MarkerOptions().position(new LatLng(todoMarkersOnMap.get(name).get(0), todoMarkersOnMap.get(name).get(1))).title(name).icon(BitmapDescriptorFactory.defaultMarker(0))));
					}
					setUpMapIfNeeded();}
				else{
					for (int i = 0; i < todoMarkers.size(); i++) {
						Marker mark=todoMarkers.get(i);
						mark.remove();
					}
				}
			} 
        });
		meetBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(arg1==true){
					meetMarkers.clear();
					for (int i = 0; i < meetMarkerNames.size(); i++) {
						String name = meetMarkerNames.get(i);
						meetMarkers.add(mMap.addMarker(new MarkerOptions().position(new LatLng(meetMarkersOnMap.get(name).get(0), meetMarkersOnMap.get(name).get(1))).title(name).icon(BitmapDescriptorFactory.defaultMarker(60))));
					}
					setUpMapIfNeeded();}
				else{
					for (int i = 0; i < meetMarkers.size(); i++) {
						Marker mark=meetMarkers.get(i);
						mark.remove();
					}
				}
			} 
        });
		meBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(arg1==true)
					mMap.setMyLocationEnabled(true);
				else
					mMap.setMyLocationEnabled(false);
			} 
        });
		
		setUpMapIfNeeded();
	}

	@Override
    public void onDestroyView() {
        super.onDestroyView();
        MapFragment f = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        if (f != null) 
            getFragmentManager().beginTransaction().remove(f).commit();
    }
}

