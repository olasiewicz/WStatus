package com.wojtek.wstatus;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class WStatusActivity extends Activity {

	private Button bTweet;
	private EditText etStatus;
	private TextView tvCounter;
	private int defaultTextColor;
	private String status = "";
	private static final String TAG = WStatusActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 //setContentView(R.layout.new_activity);

		if (savedInstanceState == null) {
			WStatusFragment fragment = new WStatusFragment();
			getFragmentManager()
					.beginTransaction()
					.add(android.R.id.content, fragment,
							WStatusFragment.class.getSimpleName()).commit();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.wstatus, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
