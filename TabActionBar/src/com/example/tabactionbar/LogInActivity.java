package com.example.tabactionbar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class LogInActivity extends Activity {

	public final static String USERNAME = "com.example.myfirstapp.USERNAME";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_log_in);
		
		Parse.initialize(this, "AGzf1jUA64JLDe3Kr1etAOuIvTpQAfLZvUUmSl3x", "1bccOOc7hcRKx28QSPqPxXyvFoRywqJPS98H2egq");
		
		ParseUser testUser = new ParseUser();
		testUser.setUsername("Amadu Durham");
		testUser.setPassword("testing");
		testUser.setEmail("parse@gmai.com");
		
		testUser.signUpInBackground(new SignUpCallback() {
			  public void done(ParseException e) {
			    if (e == null) {
			      // Hooray! Let them use the app now.
			    } else {
			      // Sign up didn't succeed. Look at the ParseException
			      // to figure out what went wrong
			    }
			  }
			});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.log_in, menu);
		return true;
	}
	
	public void signUp(View v) {
		
	}
	
	public void loginUser(View v) {
		final Intent mapIntent = new Intent(this, MapActivity.class);
		
		String username = ((EditText) findViewById(R.id.username)).getText().toString();
		String password = ((EditText) findViewById(R.id.username)).getText().toString();
		
		mapIntent.putExtra(USERNAME, username);
		
		ParseUser.logInInBackground(username, password, new LogInCallback() {
			  public void done(ParseUser user, ParseException e) {
			    if (user != null) {
			      // Hooray! The user is logged in.
			    	startActivity(mapIntent);
			    	
			    } else {
			      // Signup failed. Look at the ParseException to see what happened.
			    	((EditText) findViewById(R.id.username)).setText("");
			    	((EditText) findViewById(R.id.username)).setText("");
			    	System.out.println(e);
			    }
			  }
			});
		
	}

}
