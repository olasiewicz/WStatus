package com.wojtek.wstatus;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class WMainActivity extends Activity {	
	

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.action_add:
			startActivity(new Intent(WMainActivity.this, WStatusActivity.class));
			break;
			
		case R.id.action_settings:
			startActivity(new Intent("com.wojtek.settings"));
			break;

		case R.id.action_refresh:
			startService(new Intent("com.wojtek.refresh"));
			break;
			
		case R.id.action_clearDB:
			
			AlertDialog.Builder alert = new AlertDialog.Builder(
					this);

			alert.setMessage(R.string.dialog_delAll);
			alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					getContentResolver().delete(WStatusContract.ContentURI,
							null, null);
				}
			});
	        alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					dialog.cancel();
					
				}
			});
			alert.show();
			
			break;
	
			
		default:
			break;
		}

		return false;
	}
}