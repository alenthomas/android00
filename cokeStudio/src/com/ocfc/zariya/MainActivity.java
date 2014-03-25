package com.ocfc.zariya;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends Activity implements OnMapLongClickListener, LocationListener, ClusterItem{
	
	private ProgressDialog pDialog;
	private static final String READ_LATLONG = "http://192.168.1.3/freefood/new/jsonmsg.php";
	
	private static final String TAG_SUCCESS = "success";
    private static final String TAG_POSTS = "posts";
    private static final String TAG_LOC_ID = "loc_id";
    private static final String TAG_EMAIL_ID = "email_id";
    private static final String TAG_GEO = "geo";
    private static final String TAG_GEO_LAT = "geolat";
    private static final String TAG_GEO_LONG = "geolong";
    private static final String TAG_OCCASION = "occasion";
    private static final String TAG_LOCATION = "location";
    private static final String TAG_DESCRIPTION = "description";
    private static final String TAG_TIME_LIMIT = "timelimit";
    private static final String TAG_QUANTITY = "quantity";
    private static final String TAG_VEG = "veg";
    private static final String TAG_NON_VEG = "nonveg";
    private static final String TAG_CONTACT_NO = "contactno";
    
    public JSONArray mDetails;
    public ArrayList<HashMap<String, String>> mDetailsList;
	
	public GoogleMap mapMap;
	LatLng latlong, myLocation;
	Location location;
	float MAP_ZOOM = 16;
	 private LocationManager locationManager;
	 private static final long MIN_TIME = 400;
	 private static final float MIN_DISTANCE = 1000;
	 
	 @Override
		protected void onCreate(Bundle savedInstanceState) {
		  super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mapMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map2)).getMap();
		//mapMap.setClustering(new ClusteringSettings().enabled(false).addMarkersDynamically(true));
		mapMap.addMarkersDynamically(true);
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE, this);
	    
	    /*
	     * Marker tagore = map.addMarker(new MarkerOptions().position(TAGORE).title("Tagore Hall"));
	     */
	    
	    mapMap.setMyLocationEnabled(true);
		//mapMap.setOnInfoWindowClickListener(this);
		mapMap.setOnMapLongClickListener(this);
		
		
	 }
	 
	 @Override
	    protected void onResume() {
	    	// TODO Auto-generated method stub
	    	super.onResume();
	    	//loading the comments via AsyncTask
	    	new FillMarker().execute();
	    }
	 
	 public void updateJSONdata() {
		 
		 mDetailsList = new ArrayList<HashMap<String, String>>();
		 
		 JSONParser jParser = new JSONParser();
		 
		 JSONObject json = jParser.getJSONFromUrl(READ_LATLONG);
		 
		 try {
			 
			 mDetails = json.getJSONArray(TAG_POSTS);
			 
			 for (int i = 0; i < mDetails.length(); i++) {
	                JSONObject c = mDetails.getJSONObject(i);
	                
	                String loc_id = c.getString(TAG_LOC_ID);
	                String email_id = c.getString(TAG_EMAIL_ID);
	                String geo = c.getString(TAG_GEO);
	                String geolat = c.getString(TAG_GEO_LAT);
	                String geolong = c.getString(TAG_GEO_LONG);
	                String occasion = c.getString(TAG_OCCASION);
	                String location = c.getString(TAG_LOCATION);
	                String description = c.getString(TAG_DESCRIPTION);
	                String timelimit = c.getString(TAG_TIME_LIMIT);
	                String quantity = c.getString(TAG_QUANTITY);
	                String veg = c.getString(TAG_VEG);
	                String nonveg = c.getString(TAG_NON_VEG);
	                String contactno = c.getString(TAG_CONTACT_NO);
	                
	                
	                HashMap<String, String> map = new HashMap<String, String>();
	                
	                map.put(TAG_GEO, geo);
	                map.put(TAG_GEO_LAT, geolat);
	                map.put(TAG_GEO_LONG, geolong);
	                map.put(TAG_OCCASION, occasion);
	                map.put(TAG_LOCATION, location);
	                map.put(TAG_EMAIL_ID, email_id);
	                
	                mDetailsList.add(map);
	                
	                //List<String> content = new ArrayList<String>();
	                /*mapMap.addMarker(new MarkerOptions()
	                .position(
	                        new LatLng(Double.valueOf(TAG_GEO_LAT),Double.valueOf(TAG_GEO_LONG)))
	                .title(TAG_LOCATION));*/

	                
	                
		 }
		 } catch (JSONException e) {
	            e.printStackTrace();
	        }
	 }
	/* {
		 for(HashMap<String, String> map : mDetailsList)
		 {
			 String l = map.get(TAG_GEO_LAT);
			 String ll = map.get(TAG_GEO_LONG);
			 String loc = map.get(TAG_LOCATION);
			 double l1 = Double.valueOf(l);
			 double l2 = Double.valueOf(ll);
			 //mapMap.addMarker(new MarkerOptions().position(new LatLng(l1,l2)).title(loc));
			 
		 }
	*/
/*	 for(int k=0; k<mDetailsList.size();k++)
	 {
		 
		 HashMap<String,String> ll1 = mDetailsList.get(4);
		 double l1 = Double.
		 double l2 = Double.valueOf(TAG_GEO_LONG);
		 String l = TAG_LOCATION;
		 mapMap.addMarker(new MarkerOptions()
         .position(
                 new LatLng(Double.valueOf(TAG_GEO_LAT),Double.valueOf(TAG_GEO_LONG)))
         .title(TAG_LOCATION));
	 }*/
	 
	 public class FillMarker extends AsyncTask< Double , Double, String> {

		 @Override
			protected void onPreExecute() {
				super.onPreExecute();
				pDialog = new ProgressDialog(MainActivity.this);
				pDialog.setMessage("Loading Markers.....");
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(true);
				pDialog.show();
		 }
		 
		 
		 @Override
		protected String doInBackground(Double... params) {
			// TODO Auto-generated method stub
			 
			 updateJSONdata();
			 for(HashMap<String, String> map : mDetailsList)
			 {
				 String l = map.get(TAG_GEO_LAT);
				 String ll = map.get(TAG_GEO_LONG);
				 String loc = map.get(TAG_LOCATION);
				 double l1 = Double.valueOf(l);
				 double l2 = Double.valueOf(ll);
				 mapMap.addMarker(new MarkerOptions().position(new LatLng(l1,l2)).title(loc));
				 //MarkerOptions marker = new MarkerOptions().position(new LatLng(l1, l2)).title(loc);
				    //marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.icon));
				    //marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
				    //mapMap.addMarker(marker);
				 
				 
			 }
			return null;
		}
		 
	 }
@Override
public void onLocationChanged(Location location) {
	// TODO Auto-generated method stub
	
	LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 10);
    mapMap.animateCamera(cameraUpdate);
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


@Override
public void onMapLongClick(LatLng point) {
	// TODO Auto-generated method stub
	
	mapMap.addMarker(new MarkerOptions().position(point).title(point.toString()));
	
}
}