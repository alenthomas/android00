package com.ocfc.zariya;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

public class FoodDetails extends Activity implements OnClickListener{
	
	Button submit;
	TextView tv_email_id, tv_geo;
	EditText et_location, et_occasion, et_description;
	LatLng latlong;
	ProgressDialog pDialog;
	JSONParser jsonParser = new JSONParser();
	static final String POST_DETAIL_URL = "http://192.168.1.3/freefood/location.php";
	static final String TAG_SUCCESS = "success";
	static final String TAG_MESSAGE = "message";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_food_details);
		Bundle bundle = getIntent().getParcelableExtra("bundle");
		latlong = bundle.getParcelable("position");
		submit = (Button) findViewById(R.id.bSubmit);
		tv_email_id =(TextView) findViewById(R.id.tvEmailId);
		tv_geo = (TextView) findViewById(R.id.tvLatLong);
		et_location = (EditText) findViewById(R.id.etLocation);
		et_occasion = (EditText) findViewById(R.id.etOccasion);
		et_description = (EditText) findViewById(R.id.etDescription);
		
		String geolat= String.valueOf(latlong.latitude);
		String geolon = String.valueOf(latlong.longitude);
		String comma = ",";
		String geolatcomm = geolat + comma;
		String goept = geolatcomm + geolon;
		tv_geo.setText(goept);
		submit.setOnClickListener(this);
/*
 * display1.setText(String.valueOf(latlong.latitude));
		display2.setText(String.valueOf(latlong.longitude));
 * 		
 */

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.food_details, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		new PostDetail().execute();
	}
	
	class PostDetail extends AsyncTask<String, String, String>
	{
		@Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(FoodDetails.this);
            pDialog.setMessage("Posting Detail...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
		
		@Override
		protected String doInBackground(String... args) {
			// TODO Auto-generated method stub
			 // Check for success tag
            int success;
           // String post_emailId = tv_email_id.getText().toString();
            String post_geo = tv_geo.getText().toString();
            String post_location = et_location.getText().toString();
            String post_occasion = et_occasion.getText().toString();
            String post_description = et_description.getText().toString();
            
            /*
             * ...
            String post_title = title.getText().toString();
            String post_message = message.getText().toString();
            
            //Retrieving Saved Username Data:
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(AddComment.this);
            String post_username = sp.getString("username", "anon");
            
            try {
                // Building Parameters
...

             */
            
            //We need to change this:
            //String post_emailId = "ligindd@gmail.com";
            
          //Retrieving Saved Username Data:
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(FoodDetails.this);
            String post_emailId = sp.getString("email_id", "anon");
            
            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("email_id", post_emailId));
                params.add(new BasicNameValuePair("geo", post_geo));
                params.add(new BasicNameValuePair("location", post_location));
                params.add(new BasicNameValuePair("occasion", post_occasion));
                params.add(new BasicNameValuePair("description", post_description));
 
                Log.d("request!", "starting");
                
                //Posting user data to script 
                JSONObject json = jsonParser.makeHttpRequest(
                		POST_DETAIL_URL, "POST", params);
 
                // full json response
                Log.d("Post Comment attempt", json.toString());
 
                // json success element
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                	Log.d("Comment Added!", json.toString());    
                	finish();
                	return json.getString(TAG_MESSAGE);
                }else{
                	Log.d("Comment Failure!", json.getString(TAG_MESSAGE));
                	return json.getString(TAG_MESSAGE);
                	
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
 
            return null;
			
		}
		
		 protected void onPostExecute(String file_url) {
	            // dismiss the dialog once product deleted
	            pDialog.dismiss();
	            if (file_url != null){
	            	Toast.makeText(FoodDetails.this, file_url, Toast.LENGTH_LONG).show();
	            }
	            Intent openLocDet;
	            openLocDet = new Intent(FoodDetails.this , LocationDetail.class);
	            startActivity(openLocDet);
	}

	}
}