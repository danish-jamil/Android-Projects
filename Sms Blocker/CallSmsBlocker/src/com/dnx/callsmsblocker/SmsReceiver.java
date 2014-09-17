package com.dnx.callsmsblocker;

import java.util.Date;

import com.dnx.adapter.DatabaseHelper;
import com.dnx.model.BlockedMessage;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract.PhoneLookup;
import android.telephony.SmsMessage;
import android.widget.Toast;

public class SmsReceiver extends BroadcastReceiver {

	Context context;
	DatabaseHelper db;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		this.context = context;
		db = new DatabaseHelper(context);

		if (intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                Object[] pdus = (Object[])extras.get("pdus");

                if (pdus.length < 1) return; // Invalid SMS. Not sure that it's possible.
                
                String body = "";
                String sender = null;
                String dateTime = null;
                for (int i = 0; i < pdus.length; i++) {
                    SmsMessage message = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    sender = message.getOriginatingAddress();
                    body += message.getMessageBody();
                    dateTime = String.valueOf(message.getTimestampMillis());
                }
                
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
                if (sp.getBoolean("sms_block_enabled", false)) {

                	if(db.isExists(sender)){
                		abortBroadcast();
                		//addMessageToSent("Blocked", "Sender: " + sender + "\n" + body);
                		db.addMessage(new BlockedMessage(db.getSenderId(sender), body, dateTime));
                	}
                }
                else{
                	//Toast.makeText(context, dateTime, Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
	}
	
	public boolean contactExists(Context context, String number) {
		/// number is the phone number
		Uri lookupUri = Uri.withAppendedPath(
		PhoneLookup.CONTENT_FILTER_URI, 
		Uri.encode(number));
		String[] mPhoneNumberProjection = { PhoneLookup._ID, PhoneLookup.NUMBER, PhoneLookup.DISPLAY_NAME };
		Cursor cur = context.getContentResolver().query(lookupUri,mPhoneNumberProjection, null, null, null);
		try {
		   if (cur.moveToFirst()) {
		      return true;
		   }
		} finally {
		if (cur != null)
		   cur.close();
		}
		return false;
	}
	
    private static final String TELEPHON_NUMBER_FIELD_NAME = "address";
    private static final String MESSAGE_BODY_FIELD_NAME = "body";
    private static final Uri SENT_MSGS_CONTET_PROVIDER = Uri.parse("content://sms/sent");
    
    private void addMessageToSent(String telNumber, String messageBody) {
        ContentValues sentSms = new ContentValues();
        sentSms.put(TELEPHON_NUMBER_FIELD_NAME, telNumber);
        sentSms.put(MESSAGE_BODY_FIELD_NAME, messageBody);
        
        ContentResolver contentResolver = context.getContentResolver();
        contentResolver.insert(SENT_MSGS_CONTET_PROVIDER, sentSms);
    }

}
