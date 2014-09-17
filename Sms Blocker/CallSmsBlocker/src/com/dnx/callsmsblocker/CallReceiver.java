package com.dnx.callsmsblocker;

import java.lang.reflect.Method;

import com.android.internal.telephony.ITelephony;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class CallReceiver extends BroadcastReceiver {

	Context context = null;
	 private static final String TAG = "Phone call";
	 private ITelephony telephonyService;

	 @Override
	 public void onReceive(Context context, Intent intent) {
		 Log.v(TAG, "Receving....");
		 SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		 if(prefs.getBoolean(MainActivity.KEY_CALLS_BLOCKED, false)){
//			 TelephonyManager telephony = (TelephonyManager) 
//					 context.getSystemService(Context.TELEPHONY_SERVICE);
//			 try {
//				 Class c = Class.forName(telephony.getClass().getName());
//				 Method m = c.getDeclaredMethod("getITelephony");
//				 m.setAccessible(true);
//				 telephonyService = (ITelephony) m.invoke(telephony);
//				 //telephonyService.silenceRinger();
//				 telephonyService.endCall();
//			 } catch (Exception e) {
//				  e.printStackTrace();
//			 }
//			 String number = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
//			 setResultData(null);
//			 Toast.makeText(context, "Call Terminated.. " + number, Toast.LENGTH_LONG).show();
		 }
	
		 
	 }
}
