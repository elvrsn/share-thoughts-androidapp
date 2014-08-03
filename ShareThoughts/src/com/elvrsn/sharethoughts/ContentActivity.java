package com.elvrsn.sharethoughts;


import com.elvrsn.sharethoughts.R;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class ContentActivity extends ActionBarActivity {
	
	protected void addThought() {
		Intent intent = new Intent(this, AddActivity.class);
		startActivity(intent);
	}
	
	protected void aboutST() {
		Intent intent = new Intent(this, AboutActivity.class);
		startActivity(intent);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		Intent incomingintent = getIntent();
		String title = incomingintent.getStringExtra("title");
		String content = incomingintent.getStringExtra("content");
		TextView title_txt = (TextView) findViewById(R.id.textView1);
		TextView content_txt = (TextView) findViewById(R.id.textView2);
		title_txt.setText(title);
		content_txt.setText(content);
//		ArrayList<String> content_list = new ArrayList<String>();
//		content_list.add(content);
//		ListView contentListView = (ListView) findViewById( R.id.contentListView );
//		contentListView.setDivider(null);
//		ArrayAdapter<String> listAdapter = new ArrayAdapter<String>(this, R.layout.content_row, content_list);
//		contentListView.setAdapter(listAdapter);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		android.support.v7.app.ActionBar actionBar = getSupportActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);

		setContentView(R.layout.activity_content);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.content, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_addthought) {
        	addThought();
        }
        else if (id == R.id.action_about) {
        	aboutST();
        }
		return super.onOptionsItemSelected(item);
	}
}
