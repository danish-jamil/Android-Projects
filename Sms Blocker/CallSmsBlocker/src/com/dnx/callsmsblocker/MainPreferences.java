package com.dnx.callsmsblocker;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

public class MainPreferences extends PreferenceActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		getFragmentManager().beginTransaction()
		.replace(android.R.id.content, new PrefsFragment()).commit();
		
	}
	
	public static class PrefsFragment extends PreferenceFragment{
		@Override
		public void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			
			addPreferencesFromResource(R.xml.preferences);
			
		}
	}

}
