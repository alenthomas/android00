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
import android.content.SharedPreferences.Editor;
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

public class SignupRegister extends Activity implements OnClickListener{

	TextView loginReg,regError;
	EditText regName, regEmail,/* regPhoneNumber, */regPassword ;
	Button regRegister;
	
	private ProgressDialog pDialog;
	JSONParser jsonParser = new JSONParser();
	
	private static final String REG_URL = "http://192.168.1.3/freefood/register.php";
	
	private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup_register);
		
		loginReg = (TextView) findViewById(R.id.tvRegLogin);
		regError = (TextView) findViewById(R.id.tvRegError);
		regName = (EditText) findViewById(R.id.etRegName);
		regEmail = (EditText) findViewById(R.id.etRegEmail);
		//regPhoneNumber = (EditText) findViewById(R.id.etRegPhoneNumber);
		regPassword = (EditText) findViewById(R.id.etRegPassword);
		regRegister = (Button) findViewById(R.id.bReg);
		
		regRegister.setOnClickListener(this);
		loginReg.setOnClickListener(this);
	}

	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.bReg:
					{	
							new CreateUser().execute();
					}
					break;
		case R.id.tvRegLogin:
					{
							Intent openLog = new Intent(this, com.ocfc.zariya.SignInAct.class);
							startActivity(openLog);
					}
					break;
		default	:
					break;
		}
	}
	
	class CreateUser extends AsyncTask<String, String, String> {

		 /**
        * Before starting background thread Show Progress Dialog
        * */
		boolean failure = false;

       @Override
       protected void onPreExecute() {
           super.onPreExecute();
           pDialog = new ProgressDialog(SignupRegister.this);
           pDialog.setMessage("Creating User...");
           pDialog.setIndeterminate(false);
           pDialog.setCancelable(true);
           pDialog.show();
       }

		@Override
		protected String doInBackground(String... args) {
			// TODO Auto-generated method stub
			 // Check for success tag
           int success;
           String name = regName.getText().toString();
           String email_id = regEmail.getText().toString();
           String password = regPassword.getText().toString();
           try {
               // Building Parameters
               List<NameValuePair> params = new ArrayList<NameValuePair>();
               params.add(new BasicNameValuePair("name",name));
               params.add(new BasicNameValuePair("email_id", email_id));
               params.add(new BasicNameValuePair("password", password));

               Log.d("request!", "starting");

               //Posting user data to script
               JSONObject json = jsonParser.makeHttpRequest(
                      REG_URL, "POST", params);

               // full json response
               Log.d("Login attempt", json.toString());

               // json success element
               success = json.getInt(TAG_SUCCESS);
               if (success == 1) {
               	Log.d("User Created!", json.toString());
               	
             // save user data
				SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(SignupRegister.this);
				Editor edit = sp.edit();
				edit.putString("email_id", email_id);
				edit.commit();
               	
               	Intent i = new Intent(SignupRegister.this, MapView.class);
               	finish();
               	startActivity(i);
               	return json.getString(TAG_MESSAGE);
               }else{
               	Log.d("Login Failure!", json.getString(TAG_MESSAGE));
               	return json.getString(TAG_MESSAGE);

               }
           } catch (JSONException e) {
               e.printStackTrace();
           }

           return null;

		}
		/**
        * After completing background task Dismiss the progress dialog
        * **/
       protected void onPostExecute(String file_url) {
           // dismiss the dialog once product deleted
           pDialog.dismiss();
           if (file_url != null){
           	Toast.makeText(SignupRegister.this, file_url, Toast.LENGTH_LONG).show();
           }

       }

	}

	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.signup_register, menu);
		return true;
		}

	}