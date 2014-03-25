package com.ocfc.zariya;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class LocationDetail extends ListActivity {

	Button pstNew;
	// Progress Dialog
	private ProgressDialog pDialog;
 
	//php read comments script
    
    //localhost :  
    //testing on your device
    //put your local ip instead,  on windows, run CMD > ipconfig
    //or in mac's terminal type ifconfig and look for the ip under en0 or en1
   // private static final String READ_COMMENTS_URL = "http://xxx.xxx.x.x:1234/webservice/comments.php";
    
    //testing on Emulator:
    private static final String READ_COMMENTS_URL = "http://192.168.1.3/freefood/jsonmsg.php";
    
  //testing from a real server:
    //private static final String READ_COMMENTS_URL = "http://www.mybringback.com/webservice/comments.php";
   
  //JSON IDS:
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_OCCASION = "occasion";
    private static final String TAG_POSTS = "posts";
    private static final String TAG_LOC_ID = "loc_id";
    private static final String TAG_EMAIL_ID = "email_id";
    private static final String TAG_LOCATION = "location";
    //it's important to note that the message is both in the parent branch of 
    //our JSON tree that displays a "Post Available" or a "No Post Available" message,
    //and there is also a message for each individual post, listed under the "posts"
    //category, that displays what the user typed as their message.
    
    
   //An array of all of our comments
    private JSONArray mComments = null;
    //manages all of our comments in a list.
    private ArrayList<HashMap<String, String>> mCommentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //note that use read_comments.xml instead of our single_post.xml
        setContentView(R.layout.view_location);   
        pstNew = (Button) findViewById(R.id.post_location);
        pstNew.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i;
				i = new Intent("com.ocfc.zariya.fooddetails");
				startActivity(i);
			}
		});
    }
    
    @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
    	super.onResume();
    	//loading the comments via AsyncTask
    	new LoadComments().execute();
    }

    /**
     * Retrieves json data of comments
     */
    public void updateJSONdata() {
  
    	/*
    	 * ...
 /**
     * Retrieves recent post data from the server.
     */
       // Instantiate the arraylist to contain all the JSON data.
    	// we are going to use a bunch of key-value pairs, referring
    	// to the json element name, and the content, for example,
    	// message it the tag, and "I'm awesome" as the content..
    	
        mCommentList = new ArrayList<HashMap<String, String>>();
        
        // Bro, it's time to power up the J parser 
        JSONParser jParser = new JSONParser();
        // Feed the beast our comments url, and it spits us
        //back a JSON object.  Boo-yeah Jerome.
        JSONObject json = jParser.getJSONFromUrl(READ_COMMENTS_URL);

        //when parsing JSON stuff, we should probably
        //try to catch any exceptions:
        try {
            
        	//I know I said we would check if "Posts were Avail." (success==1)
        	//before we tried to read the individual posts, but I lied...
        	//mComments will tell us how many "posts" or comments are
        	//available
            mComments = json.getJSONArray(TAG_POSTS);

            // looping through all posts according to the json object returned
            for (int i = 0; i < mComments.length(); i++) {
                JSONObject c = mComments.getJSONObject(i);

                //gets the content of each tag
                String occasion = c.getString(TAG_OCCASION);
                String location = c.getString(TAG_LOCATION);
                String email_id = c.getString(TAG_EMAIL_ID);
                

                // creating new HashMap
                HashMap<String, String> map = new HashMap<String, String>();
              
                map.put(TAG_OCCASION, occasion);
                map.put(TAG_LOCATION, location);
                map.put(TAG_EMAIL_ID, email_id);
             
                // adding HashList to ArrayList
                mCommentList.add(map);
                
                //annndddd, our JSON data is up to date same with our array list
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    /**
     * Inserts the parsed data into our listview
     */
    /**
    	 * Inserts the parsed data into the listview.
    	 */
    	private void updateList() {
    		// For a ListActivity we need to set the List Adapter, and in order to do
    		//that, we need to create a ListAdapter.  This SimpleAdapter,
    		//will utilize our updated Hashmapped ArrayList, 
    		//use our single_post xml template for each item in our list,
    		//and place the appropriate info from the list to the
    		//correct GUI id.  Order is important here.
    		ListAdapter adapter = new SimpleAdapter(this, mCommentList,
    				R.layout.activity_location_detail, new String[] { TAG_OCCASION, TAG_LOCATION,
    						TAG_EMAIL_ID }, new int[] { R.id.titleOccasion, R.id.messageLocation,
    						R.id.tvEmailIdLocasionView });

    		// I shouldn't have to comment on this one:
    		setListAdapter(adapter);
    		
    		// Optional: when the user clicks a list item we 
    		//could do something.  However, we will choose
    		//to do nothing...
    		ListView lv = getListView();	
    		lv.setOnItemClickListener(new OnItemClickListener() {

    			@Override
    			public void onItemClick(AdapterView<?> parent, View view,
    					int position, long id) {

    				// This method is triggered if an item is click within our
    				// list. For our example we won't be using this, but
    				// it is useful to know in real life applications.

    			}
    		});
    	}       

    public class LoadComments extends AsyncTask<Void, Void, Boolean> {

    	@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(LocationDetail.this);
			pDialog.setMessage("Loading Comments...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}
        @Override
        protected Boolean doInBackground(Void... arg0) {
        	//we will develop this method in version 2
            updateJSONdata();
            return null;

        }


        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            pDialog.dismiss();
          //we will develop this method in version 2
            updateList();
        }
    }
}