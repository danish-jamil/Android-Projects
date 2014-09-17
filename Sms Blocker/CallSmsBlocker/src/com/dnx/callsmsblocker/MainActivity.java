package com.dnx.callsmsblocker;

import java.util.ArrayList;
import java.util.Locale;

import com.dnx.adapter.DatabaseHelper;
import com.dnx.adapter.NumbersListAdapter;
import com.dnx.model.BlockedMessage;
import com.dnx.model.Person;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.OpenableColumns;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class MainActivity extends FragmentActivity {

	SectionsPagerAdapter mSectionsPagerAdapter;

	ViewPager mViewPager;
	
	public static final String SENDER_ID = "sender_id";

	public static final String KEY_CALLS_BLOCKED = "call_block_enabled";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);
		
		ActionBar bar = getActionBar();
		bar.setDisplayShowHomeEnabled(false);
		bar.setDisplayShowCustomEnabled(true);
		bar.setDisplayShowTitleEnabled(false);
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.actionbar_layout, null);
		bar.setCustomView(v);
	
	}
	
	public static String toggleNumber(Context c, String number){
		if(number.length() > 10){
			if(number.substring(0, 1).equals("0")){
				number = number.substring(1, number.length());
				number = "+92" + number;
				return number;
			}
			if(number.substring(0, 3).equals("+92")){
				number = number.replace("+92", "0");
				return number;
			}
		}
		return number;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId()){
		case R.id.action_settings:
			Intent intent = new Intent(MainActivity.this, MainPreferences.class);
			startActivity(intent);
			return true;
		case R.id.action_about:
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			
			switch(position){
			case 0:
				return new MessagesFragment();
			case 1:
				return new CallsFragment();
			case 2:
				return new LogFragment();
			}
			return null;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return "Block Messages";
			case 1:
				return "Block Calls";
			case 2:
				return "Log";
			}
			return null;
		}
	}


	public static class MessagesFragment extends Fragment implements PopupMenu.OnMenuItemClickListener{
		
		NumbersListAdapter adapter;
		ArrayList<Person> persons;
		TextView tv;
		ListView lv;
		DatabaseHelper db;
		
		public MessagesFragment() {
			
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			final View rootView = inflater.inflate(R.layout.fragment_messages,
					container, false);
			
			db = new DatabaseHelper(getActivity());
			
			ImageButton btn = (ImageButton) rootView.findViewById(R.id.ibAdd);
			
			btn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					PopupMenu menu = new PopupMenu(rootView.getContext(), v);
					menu.inflate(R.menu.messages_add_actions);
					menu.setOnMenuItemClickListener(MessagesFragment.this);
					menu.show();
				}
			});
			
			tv = (TextView) rootView.findViewById(R.id.tvHintMessages);
			Typeface tf = Typeface.createFromAsset(rootView.getContext().getAssets(), "fonts/segoeuil.ttf");
			tv.setTypeface(tf);
			
			tv.setText("Click add + button to block messages from specific numbers.");
			
			lv = (ListView) rootView.findViewById(R.id.lvMessageContacts);
			
			persons = db.getAllMsgNumbers();
			
			adapter = new NumbersListAdapter(getActivity(), persons);
			
			lv.setAdapter(adapter);
			
			lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int pos, long id) {
					if(db.getMessagesCount(persons.get(pos).getId()) > 0){
						Intent intent = new Intent(getActivity(), MessagesActivity.class);
						intent.putExtra(SENDER_ID, String.valueOf(persons.get(pos).getId()));
						startActivity(intent);
					}else{
						Toast.makeText(getActivity(), "0 Messages.", Toast.LENGTH_SHORT).show();
					}
					
				}
			});
			
			registerForContextMenu(lv);
			
			hideInstruction();
			
			return rootView;
		}
		
		private void hideInstruction(){
			if(persons.size() > 0){
				tv.setVisibility(View.INVISIBLE);
			}else{
				tv.setVisibility(View.VISIBLE);
			}
		}
		
		private void showAddNumberDialog(Context context){
			LayoutInflater inflater = LayoutInflater.from(context);
			View view = inflater.inflate(R.layout.dialog_add_contact, null);
			
			AlertDialog.Builder builder = new Builder(context);
			builder.setView(view);
			builder.setTitle("Add Number to Block Messages Sent by this Number.");
			
			final EditText input = (EditText) view.findViewById(R.id.etAddNumber);
			
			builder.setNegativeButton("Cancel", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			})
			.setPositiveButton("Add", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int arg1) {
					Person p = new Person(input.getText().toString(), input.getText().toString());
					
					if(!db.isExists(p.getNumber())){
						persons.add(p);
						hideInstruction();
						db.addMsgNumber(p);
						adapter.notifyDataSetChanged();
						
						new AlertDialog.Builder(getActivity())
						.setTitle("Confirmation!")
						.setMessage("Do you want to move all the messages received by this number to blocked list?")
						.setPositiveButton("Yes", new OnClickListener() {
							
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								moveFromInbox(input.getText().toString());
							}
						})
						.setNegativeButton("No", new OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.cancel();
							}
						})
						.create().show();
					}else{
                    	Toast.makeText(getActivity(), "Number Already Exists.", Toast.LENGTH_SHORT).show();
                    }
					
					adapter.notifyDataSetChanged();

				}
			});
			
			AlertDialog alertDialog = builder.create();
			alertDialog.getWindow().setSoftInputMode(
				    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
			alertDialog.show();
		}

		@Override
		public boolean onMenuItemClick(MenuItem item) {
			switch(item.getItemId()){
			case R.id.addContact:
				Intent intent2 = new Intent(Intent.ACTION_GET_CONTENT);
				intent2.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
				startActivityForResult(intent2, 0);
				return true;
			case R.id.addManual:
				showAddNumberDialog(getActivity());
				return true;
			case R.id.addInbox:
				Intent intent = new Intent(getActivity(), InboxActivity.class);
				startActivityForResult(intent, 1);
				return true;
			case R.id.addDefault:
				addDefault();
				return true;
			}
			return true;
		}
		
		private void addDefault(){
			for(String number: getResources().getStringArray(R.array.ufone_spam_list)){
				if(!db.isExists(number)){
					Person p = new Person(number, number);
					db.addMsgNumber(p);
					persons.add(p);
				}
			}
			for(String number: getResources().getStringArray(R.array.warid_spam_list)){
				if(!db.isExists(number)){
					Person p = new Person(number, number);
					db.addMsgNumber(p);
					persons.add(p);
				}
			}
			adapter.notifyDataSetChanged();
		}
		
		@Override
		public void onActivityResult(int requestCode, int resultCode,
				Intent data) {
			// TODO Auto-generated method stub
			super.onActivityResult(requestCode, resultCode, data);
			if(requestCode == 0){
				if(data != null){
					Uri uri = data.getData();
					if (uri != null) {
			            Cursor c = null;
			            try {
			                c = getActivity().getContentResolver().query(uri, new String[]{ 
			                			ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
			                            ContactsContract.CommonDataKinds.Phone.NUMBER,  
			                            ContactsContract.CommonDataKinds.Phone.TYPE },
			                        null, null, null);
	
			                if (c != null && c.moveToFirst()) {
			                    String name = c.getString(0);
			                    String number = c.getString(1);
			                    if(!db.isExists(number)){
			                    	Person p = new Person(name, number);
				                    persons.add(p);
				                    db.addMsgNumber(p);
			                    }else{
			                    	Toast.makeText(getActivity(), "Number Already Exists.", Toast.LENGTH_SHORT).show();
			                    }
			                    adapter.notifyDataSetChanged();
			                }
			            } finally {
			                if (c != null) {
			                    c.close();
			                }
			            }
			        }
				}
			}
			if(requestCode == 1){
				
			}
		}
		
		@Override
		public void onResume() {
			// TODO Auto-generated method stub
			super.onResume();
			if(adapter != null){
				adapter.notifyDataSetChanged();
			}
		}
		
		@Override
		public void onCreateContextMenu(ContextMenu menu, View v,
				ContextMenuInfo menuInfo) {
			// TODO Auto-generated method stub
			super.onCreateContextMenu(menu, v, menuInfo);
			MenuInflater inflater = getActivity().getMenuInflater();
			inflater.inflate(R.menu.nmbr_context, menu);
			menu.setHeaderTitle("Edit");
		}
		
		@Override
		public boolean onContextItemSelected(MenuItem item) {
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
		            .getMenuInfo();
			switch(item.getItemId()){
			case R.id.moveFromInbox:
				moveFromInbox(persons.get(info.position).getNumber());
				adapter.notifyDataSetChanged();
				return true;
			case R.id.restoreAll:
				restoreAllMessages(persons.get(info.position).getId());
				return true;
			}
			return super.onContextItemSelected(item);
		}
		
		private void moveFromInbox(String sender){
			Uri uri = Uri.parse("content://sms/inbox");
			ContentResolver cr = getActivity().getContentResolver();
			Cursor cur = cr.query(
					uri, 
					new String[]{"_id", "thread_id", "address", "body", "date"}, 
					"address = ? or address = ?" , 
					new String[]{sender, toggleNumber(getActivity(), sender)}, 
					null
				);
			if(cur.moveToFirst()){
				do{
					db.addMessage(new BlockedMessage(db.getSenderId(sender), cur.getString(3), cur.getString(4)));
					adapter.notifyDataSetChanged();
					cr.delete(Uri.parse("content://sms/" + cur.getInt(0)), null, null);
				}while(cur.moveToNext());

			}
			cur.close();
		}
		
		private void restoreAllMessages(int senderId){
			ArrayList<BlockedMessage> msgs = db.getMessages(senderId);
			for(BlockedMessage msg : msgs){
				addMessageToReceived(msg);
				db.deleteMessage(msg.getId());
			}
			adapter.notifyDataSetChanged();
		}
		
		private void addMessageToReceived(BlockedMessage msg) {
	        ContentValues sentSms = new ContentValues();
	        sentSms.put("address", db.getPerson(msg.getSenderId()).getNumber());
	        sentSms.put("body", msg.getMessage());
	        sentSms.put("date", Long.parseLong(msg.getDataTime()));
	        ContentResolver contentResolver = getActivity().getContentResolver();
	        contentResolver.insert(Uri.parse("content://sms/inbox"), sentSms);
	    }
		
	}
	
	public static class CallsFragment extends Fragment {
		
		public CallsFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			final View rootView = inflater.inflate(R.layout.fragment_calls,
					container, false);
			
			ImageButton btn = (ImageButton) rootView.findViewById(R.id.ibCallsAdd);
			
			btn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					PopupMenu menu = new PopupMenu(getActivity(), v);
					menu.getMenu().add("Contacts");
					menu.getMenu().add("Manual");
					menu.show();
				}
			});
			
			TextView tv = (TextView) rootView.findViewById(R.id.tvCallsHint);
			Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/segoeuil.ttf");
			tv.setTypeface(tf);
			
			tv.setText("Click add + button to block calls from specific numbers.");
			
			ListView lv = (ListView) rootView.findViewById(R.id.lvCallsContacts);
			
			
			return rootView;
		}
	}
	
	public static class LogFragment extends Fragment {
		
		TextView tv;
		ArrayList<String> log;
		ArrayAdapter<String> adapter;
		DatabaseHelper db;
		
		public LogFragment() {
			
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			final View rootView = inflater.inflate(R.layout.fragment_log,
					container, false);
			
			db = new DatabaseHelper(getActivity());
			
			ImageButton btn = (ImageButton) rootView.findViewById(R.id.ibClearLog);
			
			btn.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					db.clearLog();
					log.clear();
					adapter.notifyDataSetChanged();
				}
			});
			
			log = db.getLog();
			adapter = new ArrayAdapter<String>(getActivity(), R.layout.log_list_layout, 
					R.id.tvLog, log);
			
			tv = (TextView) rootView.findViewById(R.id.tvLogHint);
			Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/segoeuil.ttf");
			tv.setTypeface(tf);
			
			tv.setText("No activity performed since the app is installed.");
			
			ListView lv = (ListView) rootView.findViewById(R.id.lvLog);
			
			lv.setAdapter(adapter);
			
			hideInstruction();
			
			return rootView;
		}
		
		@Override
		public void onResume() {
			// TODO Auto-generated method stub
			super.onResume();
			if(adapter != null){
				adapter.notifyDataSetChanged();
			}
			
			hideInstruction();
		}
		
		private void hideInstruction(){
			if(log.size() > 0){
				tv.setVisibility(View.INVISIBLE);
			}else{
				tv.setVisibility(View.VISIBLE);
			}
		}
	}

}
