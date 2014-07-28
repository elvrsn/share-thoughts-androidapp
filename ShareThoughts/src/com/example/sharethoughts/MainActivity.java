package com.example.sharethoughts;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.sharethoughts.ContentActivity;

import android.support.v7.app.ActionBarActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;


public class MainActivity extends ActionBarActivity {
	
	private ListView mainListView;  
	private ArrayAdapter<String> listAdapter;
	public String content;
	
	protected boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		} else {
			return false;
		}
	}
	
	public void populateView(ArrayList<String> title_list, ArrayList<String> content_list) {
		
		mainListView = (ListView) findViewById( R.id.mainListView );  
        
		final ArrayList<String> content = content_list;
        // Create ArrayAdapter using the array_list.  
        listAdapter = new ArrayAdapter<String>(this, R.layout.simple_row, title_list);  
          
        // Set the ArrayAdapter as the ListView's adapter.  
        mainListView.setAdapter( listAdapter );
        
        final Intent intent = new Intent(this, ContentActivity.class);
        
        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parentAdapter, View view, int position,
					long id) {
				intent.putExtra("content", content.get(position));
				startActivity(intent);
			}
       });

	}
	
	public void displayView() {
		
		final ArrayList<String> title_list = new ArrayList<String>();
        final ArrayList<String> content_list = new ArrayList<String>();
        
        final ProgressDialog progress = new ProgressDialog(MainActivity.this, R.style.Theme_MyDialog);
        
        
        
		class RequestTask extends AsyncTask<String, String, String>{
			
    		public String web_response;
    		
    		@Override
    		protected void onPreExecute() {
    			// TODO Auto-generated method stub
    			super.onPreExecute();
    			progress.setTitle("Loading");
    	        progress.setMessage("Wait while loading...");
    			progress.show();
    			
    		}
    	    @Override
    	    protected String doInBackground(String... uri) {
    	        HttpClient httpclient = new DefaultHttpClient();
    	        HttpResponse response;
    	        String responseString = null;
    	        try {
    	            response = httpclient.execute(new HttpGet(uri[0]));
    	            StatusLine statusLine = response.getStatusLine();
    	            if(statusLine.getStatusCode() == HttpStatus.SC_OK){
    	                ByteArrayOutputStream out = new ByteArrayOutputStream();
    	                response.getEntity().writeTo(out);
    	                out.close();
    	                responseString = out.toString();
    	            } else{
    	                //Closes the connection.
    	                response.getEntity().getContent().close();
    	                throw new IOException(statusLine.getReasonPhrase());
    	            }
    	        } catch (ClientProtocolException e) {
    	            //TODO Handle problems..
    	        } catch (IOException e) {
    	            //TODO Handle problems..
    	        }
    	        return responseString;
    	    }

    	    @Override
    	    protected void onPostExecute(String result) {
    	    	progress.dismiss();
    	        super.onPostExecute(result);
    	        //Do anything with response..
    	        web_response = (String) result;
    	        
    	        String title = null;
    	        if (web_response != null) {
    	        	try {
						JSONObject jsonObj = new JSONObject(web_response);
						JSONArray jobs = jsonObj.getJSONArray("thought_list");
						for (int i = 0; i < jobs.length(); i++) {
							JSONObject job = jobs.getJSONObject(i);
							title = (String) job.getString("title");
							content = (String) job.getString("content");
							title_list.add(title);
							content_list.add(content);
						}
						populateView(title_list, content_list);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
    	        	
    	        }
    	    }
    	}
    	
    	RequestTask rt_obj = new RequestTask();
    	rt_obj.execute("http://share-thoughts.com/thoughts/");

	}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!isOnline()) {
        	Toast.makeText(this, "Network isn't available", Toast.LENGTH_LONG).show();
		} else {
			displayView();
		}
    
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
}
