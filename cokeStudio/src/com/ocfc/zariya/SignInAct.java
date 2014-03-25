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

public class SignInAct extends Activity implements OnClickListener{

	TextView txtReg, txtErrorMsg;
	Button SignInForLogin;
	EditText NameForLogin, EmailForLogin, PasswordForLogin;
	
	private ProgressDialog pDialog;
	
	JSONParser jsonParser = new JSONParser();
	private static final String LOGIN_URL = "http://192.168.1.3/freefood/login.php";
	private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_in);
		// Importing all assets like buttons, text fields
				NameForLogin = (EditText) findViewById(R.id.etName);
				EmailForLogin = (EditText) findViewById(R.id.etEmailId);
				PasswordForLogin = (EditText) findViewById(R.id.etPassword);
				SignInForLogin = (Button) findViewById(R.id.bEmailSignIn);
				txtReg = (TextView) findViewById(R.id.tvRegister);
				txtErrorMsg = (TextView) findViewById(R.id.tvLoginError);
				
				SignInForLogin.setOnClickListener(this);
				txtReg.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch(v.getId())
		{
		case R.id.bEmailSignIn:
					{	
							new AttemptLogin().execute();
					}
					break;
		case R.id.tvRegister:
					{
							Intent openReg = new Intent(this, com.ocfc.zariya.SignupRegister.class);
							startActivity(openReg);
					}
					break;
		default	:
					break;
		}
		}
	
	class AttemptLogin extends AsyncTask<String, String, String> {

		 /**
         * Before starting background thread Show Progress Dialog
         * */
		boolean failure = false;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(SignInAct.this);
            pDialog.setMessage("Attempting login...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

		@Override
		protected String doInBackground(String... args) {
			// TODO Auto-generated method stub
			 // Check for success tag
            int success;
            String name = NameForLogin.getText().toString();
            String email_id = EmailForLogin.getText().toString();
            String password = PasswordForLogin.getText().toString();
            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("name",name));
                params.add(new BasicNameValuePair("email_id", email_id));
                params.add(new BasicNameValuePair("password", password));

                Log.d("request!", "starting");
                // getting product details by making HTTP request
                JSONObject json = jsonParser.makeHttpRequest(LOGIN_URL, "POST", params);

                // check your log for json response
                Log.d("Login attempt", json.toString());

                // json success tag
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                	Log.d("Login Successful!", json.toString());
                	
                	// save user data
					SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(SignInAct.this);
					Editor edit = sp.edit();
					edit.putString("email_id", email_id);
					edit.commit();
                	
                	Intent i = new Intent(SignInAct.this, MapView.class);
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
            	Toast.makeText(SignInAct.this, file_url, Toast.LENGTH_LONG).show();
            }

        }

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sign_in, menu);
		return true;
	}

	
}
/*
...
//upon successful login, save username:
				// Async json success tag
				success = json.getInt(TAG_SUCCESS);
				if (success == 1) {
					Log.d("Login Successful!", json.toString());
					// save user data
					SharedPreferences sp = PreferenceManager
							.getDefaultSharedPreferences(Login.this);
					Editor edit = sp.edit();
					edit.putString("username", username);
					edit.commit();
					
					Intent i = new Intent(Login.this, ReadComments.class);
					finish();
					startActivity(i);
					return json.getString(TAG_MESSAGE);
				} else {
					Log.d("Login Failure!", json.getString(TAG_MESSAGE));
					return json.getString(TAG_MESSAGE);
				}

...

*/