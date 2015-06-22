package com.wojtek.wstatus;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class WSettingsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_settings);

//		if (savedInstanceState == null) {
//			SettingsFragment settingsFragment = new SettingsFragment();
//			getFragmentManager().beginTransaction()
//					.add(android.R.id.content, settingsFragment,
//							settingsFragment.getClass().getCanonicalName()).commit();
//		}
	}

}
