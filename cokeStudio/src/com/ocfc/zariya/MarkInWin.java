package com.ocfc.zariya;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

public class MarkInWin extends Activity {
	
	Button nav;
	TextView display1,display2;
	LatLng latlong;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mark_in_win);
		
		Bundle bundle = getIntent().getParcelableExtra("bundle");
		latlong = bundle.getParcelable("position");
		nav = (Button) findViewById(R.id.bNav);
		display1 = (TextView) findViewById(R.id.tvLat);			//lats
		display2 = (TextView) findViewById(R.id.tvLong);	//lons
		display1.setText(String.valueOf(latlong.latitude));
		display2.setText(String.valueOf(latlong.longitude));
		nav.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				//String uri = "http://maps.google.com/maps?saddr="+"44.871709,-0.505704"+"&daddr="+"43.572665,3.871447";
				Intent i = new Intent (Intent.ACTION_VIEW, Uri.parse(String.format("google.navigation:ll=%s,%s%s",  latlong.latitude, latlong.longitude, "&mode=d")));
				MarkInWin.this.startActivity(i);
				/*
				  
				  
				 Intent(android.content.Intent.ACTION_VIEW,Uri.parse("google.navigation:q="+
 point1.latitude+","+point1.longitude+";"+ point2.latitude+","+point2.longitude));
 
 
				 String uri = "http://maps.google.com/maps?saddr="+"44.871709,-0.505704"+"&daddr="+"43.572665,3.871447";
Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
startActivity(intent);
				 */
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.mark_in_win, menu);
		return true;
	}

}
