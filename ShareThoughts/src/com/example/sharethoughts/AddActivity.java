package com.example.sharethoughts;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


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
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddActivity extends ActionBarActivity {
	
	protected void aboutST() {
		Intent intent = new Intent(this, AboutActivity.class);
		startActivity(intent);
	}
	
	protected boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		} else {
			return false;
		}
	}
	
	protected void showWarning(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add);
		Button button = (Button) findViewById(R.id.button1);
		button.setOnClickListener(new OnClickListener(){
	        @Override
	        //On click function
	        public void onClick(View view) {
	        	if (isOnline()) {
	        		EditText title_txt = (EditText) findViewById(R.id.title_edttxt);
	        		String title = title_txt.getText().toString();
	        		EditText content_txt = (EditText) findViewById(R.id.editText2);
	        		String content = content_txt.getText().toString();
	        		submitThought(title, content);
//	        		finish();
	        	}
	        	else {
	        		showWarning("No network connection");
	        	}
	        }
	    });
	}
	
	public void submitThought(String title, String content) {
		
		final ProgressDialog progress = new ProgressDialog(AddActivity.this, R.style.Theme_MyDialog);
		try {
			final String final_title = URLEncoder.encode(title, "UTF-8");
			final String final_content = URLEncoder.encode(content, "UTF-8");
		
		
		class RequestTask extends AsyncTask<String, String, String>{

		    		public String web_response;
		    		@Override
		    		protected void onPreExecute() {
		    			super.onPreExecute();
						progress.setTitle("Loading");
		    	        progress.setMessage("Wait while loading...");
		    			progress.show();
		    		}
		    	    @Override
		    	    protected String doInBackground(String... arg0) {
		    	        HttpClient httpclient = new DefaultHttpClient();
		    	        String URL = "http://share-thoughts.com/addthought?title="+final_title+"&content="+final_content;
		    	        HttpGet request = new HttpGet(URL);
		    	        HttpResponse response;
		    	        String responseString = null;
						try {
							response = httpclient.execute(request);
							BufferedReader  in = new BufferedReader(new InputStreamReader(
			    	                   response.getEntity().getContent()));
			    	        responseString = in.readLine();
						} catch (ClientProtocolException e) {
							e.printStackTrace();
						} catch (IOException e) {
							e.printStackTrace();
						}
		    	        
		    	        return responseString;
		    	    }

		    	    @Override
		    	    protected void onPostExecute(String result) {
		    	        super.onPostExecute(result);
						progress.dismiss();
		    	        web_response = (String) result;
		    	        
		    	        String validity = null;
		    	        if (web_response != null) {
		    	        	try {
								JSONObject jsonObj = new JSONObject(web_response);
								JSONArray status = jsonObj.getJSONArray("status");
								JSONObject success = status.getJSONObject(0);
								validity = (String) success.getString("success");
								if (validity.equalsIgnoreCase("1")) {
									submitSuccess();
								}
								else {
									submitFailure();
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
		    	        	
		    	        }
		    	    }
		    	}
		RequestTask rt_obj = new RequestTask();
    	rt_obj.execute();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void submitFailure() {
		showWarning("Sorry Error In Submission");
		
	}

	public void submitSuccess() {
		finish();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.add, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_about) {
        	aboutST();
        }
		return super.onOptionsItemSelected(item);
	}
}
