package com.example.deliciouselectrons;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * BroadcastReceiver class to receive charger connect/disconnect messages.
 */
public class PowerConnectionReceiver extends BroadcastReceiver {
	public static final String TAG = DeliciousElectronsActivity.TAG;
	public static final String POWER_CONNECTED_KEY = DeliciousElectronsActivity.POWER_CONNECTED_KEY;
	public static final String SHARED_PREFS_NAME = DeliciousElectronsActivity.SHARED_PREFS_NAME;
	
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.i(TAG, "PowerConnectionReceiver.onReceive : action = " + action);
        
        String defaultMsg = context.getResources().getString(R.string.default_power_connected_msg);
        
        // Get the user selected power connected message
        SharedPreferences prefs = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        String userPowerConnectedString = prefs.getString(POWER_CONNECTED_KEY, defaultMsg);
        
        if (action.equals(Intent.ACTION_POWER_CONNECTED)) {
        	String msg = userPowerConnectedString;
        	
        	Intent i = new Intent(context, MyTextToSpeechService.class);
        	i.putExtra(POWER_CONNECTED_KEY, msg);
        	context.startService(i);
        }
        else if (action.equals(Intent.ACTION_POWER_DISCONNECTED)) {
        }
    }
}