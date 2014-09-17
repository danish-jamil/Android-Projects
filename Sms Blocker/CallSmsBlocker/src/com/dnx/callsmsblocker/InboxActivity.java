package com.dnx.callsmsblocker;

import java.util.ArrayList;

import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.PhoneLookup;
import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

public class InboxActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inbox);
		// Show the Up button in the action bar.
		setupActionBar();
		
		ListView lv = (ListView) findViewById(R.id.lvInbox);
		ArrayList<String> msgs = new ArrayList<String>();
		
//		ContentResolver cr = getContentResolver();
//		Uri uri = Uri.parse("content://sms");
//		Cursor cur = cr.query(uri, new String[]{"address", "person"}, null, null, null);
//		if(cur.moveToFirst()){
//			do{
//				String sender = null, number = cur.getString(0);
//				
//				Uri uri2 = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));  
//                Cursor cs= getContentResolver().query(uri2, new String[]{PhoneLookup.DISPLAY_NAME},PhoneLookup.NUMBER+"='"+number+"'",null,null);
//
//                if(cs.getCount()>0)
//                {
//                 cs.moveToFirst();
//                 sender=cs.getString(cs.getColumnIndex(PhoneLookup.DISPLAY_NAME));
//                } 
//                msgs.add(sender + "  " + number);
//			}while(cur.moveToNext());
//		}
//		
//		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, msgs);
//		lv.setAdapter(adapter);
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.inbox, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
