//11.255979,75.774356
package com.ocfc.zariya;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapView extends Activity implements OnMapLongClickListener, OnInfoWindowClickListener, LocationListener{
	
	static final LatLng TAGORE = new LatLng(11.255979, 75.774356);
	private GoogleMap map;
	LatLng latlong, myLocation;
	Location location;
	float MAP_ZOOM = 16;
	
	private LocationManager locationManager;
	private static final long MIN_TIME = 400;
	private static final float MIN_DISTANCE = 1000;
	
	 @Override
	protected void onCreate(Bundle savedInstanceState) {
	  super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_map_view);
	map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
	    .getMap();
	
	locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, this); //You can also use LocationManager.GPS_PROVIDER and LocationManager.PASSIVE_PROVIDER        

	@SuppressWarnings("unused")
	Marker tagore = map.addMarker(new MarkerOptions().position(TAGORE)
	    .title("Tagore Hall"));
	
	
	map.setMyLocationEnabled(true);
	map.setOnInfoWindowClickListener(this);
	map.setOnMapLongClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	getMenuInflater().inflate(R.menu.map_view, menu);
	return true;
	 }

	@Override
	public void onMapLongClick(LatLng point) {
		// TODO Auto-generated method stub
		map.addMarker(new MarkerOptions().position(point).title(point.toString()));
		
	}

	@Override
	public void onInfoWindowClick(Marker arg0) {
		// TODO Auto-generated method stub
		//Intent openMarkerInfoWindow ;
		Intent openFoodDetail ;
		latlong = arg0.getPosition();
		
		Bundle args = new Bundle();
		args.putParcelable("position", latlong);
		//openMarkerInfoWindow = new Intent(MapView.this, MarkInWin.class);
		openFoodDetail = new Intent(MapView.this, FoodDetails.class);
		openFoodDetail.putExtra("bundle", args);
		startActivity(openFoodDetail);
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
		 LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
		    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 10);
		    map.animateCamera(cameraUpdate);
		    locationManager.removeUpdates(this);
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

}