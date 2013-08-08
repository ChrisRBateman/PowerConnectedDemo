package com.example.deliciouselectrons;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.widget.Toast;

/**
 * Service class to convert text string to speech.
 */
public class MyTextToSpeechService extends Service implements OnInitListener {
	public static final String TAG = DeliciousElectronsActivity.TAG;
	public static final String POWER_CONNECTED_KEY = DeliciousElectronsActivity.POWER_CONNECTED_KEY;
	
	public static TextToSpeech mtts;
	public static boolean mttsInitialized = false;
	
	@Override
	public void onCreate() {
		Log.i(TAG, "MyTextToSpeechService.onCreate");
		mttsInitialized = false;
		mtts = new TextToSpeech(this, this);
	}
	
	@Override
	public void onInit(int status) {
		if (status == TextToSpeech.SUCCESS) {
			mttsInitialized = true;
		}
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		String msg = intent.getStringExtra(POWER_CONNECTED_KEY);
		if (msg != null) {
			if (mttsInitialized) {
				mtts.speak(msg, TextToSpeech.QUEUE_ADD, null);
			} else {
				Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
			}
		}
		
		return START_STICKY;
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onDestroy() {
		if (mtts != null) {
			mtts.stop();
			mtts.shutdown();
			mttsInitialized = false;
		}
	}
}
