package com.example.deliciouselectrons;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.espiandev.showcaseview.ShowcaseView;

/**
 * Main activity for Delicious Electrons application.
 */
public class DeliciousElectronsActivity extends Activity implements View.OnClickListener {
	public static final String TAG = "DeliciousElectronsTag";
	public static final String POWER_CONNECTED_KEY = "userPowerConnectedString";
	public static final String SHARED_PREFS_NAME = "myPrefs";
	
	private ImageView mAppImageView;
	private Button mSaveButton;
	private Button mTutorialButton;
    private EditText mMsgEditText;
    
    private ShowcaseView mSv;
    private int tutorialState = -1;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "DeliciousElectronsActivity.onCreate");
		setContentView(R.layout.activity_delicious_electrons);
		
		mAppImageView = (ImageView)findViewById(R.id.app_image);
		mSaveButton = (Button)findViewById(R.id.msg_save_button);
		mTutorialButton = (Button)findViewById(R.id.tutorial_button);
		mMsgEditText = (EditText)findViewById(R.id.power_connected_msg_edit);
		
		
		mSaveButton.setOnClickListener(this);
		mTutorialButton.setOnClickListener(this);
		
		SharedPreferences prefs = getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
		String s = prefs.getString(POWER_CONNECTED_KEY, "");
		if (s.length() > 0) {
			mMsgEditText.setText(s);
		}
		
		// perform check for speech engine
		Intent checkIntent = new Intent();
		checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
		startActivityForResult(checkIntent, 1);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == 1) {
	        if (resultCode != TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
	            // start up google play store so user can install speech engine
	            Intent installIntent = new Intent();
	            installIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
	            startActivity(installIntent);
	        } else {
	        	Intent i = new Intent(this, MyTextToSpeechService.class);
	        	startService(i);
	        }
	    }
	}
	
	@Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
        	case R.id.msg_save_button:
        		saveUserMessage();
        		break;
        		
        	case R.id.tutorial_button:
        		startTutorial();
        		break;
        		
        	case R.id.showcase_button:
        		updateShowcaseView();
        		break;
        }
    }
	
	/**
	 * Save the users message. Report error to user if field is blank.
	 */
	private void saveUserMessage() {
		try {
			String s = mMsgEditText.getText().toString().trim();
            if (s.length() > 0) {
            	SharedPreferences prefs = getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        		SharedPreferences.Editor editor = prefs.edit();
        		editor.putString(POWER_CONNECTED_KEY, s);
        		editor.commit();
        		Toast.makeText(this, getResources().getString(R.string.msg_saved), Toast.LENGTH_SHORT).show();
            }
            else {
            	AlertDialog alertDialog = new AlertDialog.Builder(this).create();
                String message = getResources().getString(R.string.msg_blank);
                alertDialog.setMessage(message);
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.ok_text), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                alertDialog.show();
            }
		} catch (Exception e) {
			
		}
	}
	
	/**
	 * Start the on screen tutorial.
	 */
	private void startTutorial() {
		if (tutorialState != -1) {
			return;
		}
		
		if (mSv == null) {
			ShowcaseView.ConfigOptions co = new ShowcaseView.ConfigOptions();
			mSv = ShowcaseView.insertShowcaseView(mMsgEditText, this, 
					R.string.tutorial_enter_msg_title, R.string.tutorial_enter_msg_desc, co);
			mSv.overrideButtonClick(this);
			mSv.setShowcaseIndicatorScale(0.75f);
		} else {
			mSv.setShowcaseView(mMsgEditText);
			mSv.setText(R.string.tutorial_enter_msg_title, R.string.tutorial_enter_msg_desc);
			mSv.show();
		}
		tutorialState = 1;
	}
	
	/**
	 * Update the showcaseview. 
	 */
	private void updateShowcaseView() {
		if (mSv != null) {
			switch (tutorialState) {
				case 1:
					mSv.setShowcaseView(mSaveButton);
					mSv.setText(R.string.tutorial_save_msg_title, R.string.tutorial_save_msg_desc);
					tutorialState = 2;
					break;
				case 2:
					mSv.setShowcaseView(mAppImageView);
					mSv.setText(R.string.tutorial_hear_msg_title, R.string.tutorial_hear_msg_desc);
					tutorialState = 3;
					break;
				case 3:
					mSv.hide();
					mSv.setShowcaseView(mMsgEditText);
					mSv.setText(R.string.tutorial_enter_msg_title, R.string.tutorial_enter_msg_desc);
					tutorialState = -1;
					break;
			}
		}
	}
}
