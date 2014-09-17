package com.dnx.callsmsblocker;

import java.sql.Date;
import java.util.ArrayList;

import com.dnx.adapter.DatabaseHelper;
import com.dnx.adapter.MessagesAdapter;
import com.dnx.model.BlockedMessage;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;

public class MessagesActivity extends Activity {

	DatabaseHelper db;
	ArrayList<BlockedMessage> msgs;
	MessagesAdapter adapter;
	ListView lv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_messages);
	
		db 				= new DatabaseHelper(this);
		String senderId = getIntent().getExtras().getString(MainActivity.SENDER_ID);
		msgs 			= db.getMessages(Integer.parseInt(senderId));
		lv 				= (ListView) findViewById(R.id.lvMessages);
		adapter 		= new MessagesAdapter(this, msgs);
		lv.setAdapter(adapter);
		
		registerForContextMenu(lv);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.message, menu);
		return true;
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.msg_context, menu);
		menu.setHeaderTitle("Options");
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
	            .getMenuInfo();
		switch(item.getItemId()){
		case R.id.restore:
			addMessageToReceived(msgs.get(info.position));
			db.deleteMessage(msgs.get(info.position).getId());
			msgs.remove(info.position);
			adapter.notifyDataSetChanged();
			return true;
		case R.id.delete:
			db.deleteMessage(msgs.get(info.position).getId());
			msgs.remove(info.position);
			adapter.notifyDataSetChanged();
			return true;
		}
		return super.onContextItemSelected(item);
	}
	
	private void addMessageToReceived(BlockedMessage msg) {
        ContentValues sentSms = new ContentValues();
        sentSms.put("address", db.getPerson(msg.getSenderId()).getNumber());
        sentSms.put("body", msg.getMessage());
        sentSms.put("date", Long.parseLong(msg.getDataTime()));
        ContentResolver contentResolver = getContentResolver();
        contentResolver.insert(Uri.parse("content://sms/inbox"), sentSms);
    }

}
