//AIzaSyC78f-04_kr1ROL22pTRFnusYFwIsjLBfQ
//shared preferences
package com.ocfc.zariya;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class LoginPage extends Activity {
	
	Button bSignIn,bGuest,bSignUp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_page);
		
		bGuest = (Button) findViewById(R.id.bguest);
		bGuest.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent openMapView;
				openMapView = new Intent ("com.ocfc.zariya.MAPVIEW");
				startActivity(openMapView);
				
			}
		});
		bSignIn = (Button) findViewById(R.id.bsignin);
		bSignIn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent openSignInAct;
				openSignInAct = new Intent ("com.ocfc.zariya.SIGNINACT");
				startActivity(openSignInAct);
				
			}
		});
		bSignUp = (Button) findViewById(R.id.bsignup);
		bSignUp.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent openRegAct;
				openRegAct = new Intent ("com.ocfc.zariya.REGISTER");
				startActivity(openRegAct);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login_page, menu);
		return true;
	}

}
