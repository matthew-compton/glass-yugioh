package com.ambergleam.glassyugioh;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/**
 * Activity showing the options menu.
 */
public class MenuActivity extends Activity {

	// For logging purposes
	private final String TAG = this.getClass().getName();

	protected static final int SPEECH_REQUEST = 0;
	private static ArrayList<String> list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		list = new ArrayList<String>();
	}

	@Override
	public void onResume() {
		super.onResume();
		openOptionsMenu();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_duel, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection.
		switch (item.getItemId()) {
		case R.id.loseLifeUser:
			DuelService.userLife -= getSpeechInput();
			return true;
		case R.id.loseLifeEnemy:
			DuelService.enemyLife -= getSpeechInput();
			return true;
		case R.id.addLifeUser:
			DuelService.userLife += getSpeechInput();
			return true;
		case R.id.addLifeEnemy:
			DuelService.enemyLife += getSpeechInput();
			return true;
		case R.id.restart:
			// Resets the duel
			DuelService.userLife = DuelService.startingLife;
			DuelService.enemyLife = DuelService.startingLife;
			return true;
		case R.id.stop:
			// Ends the duel and the service
			stopService(new Intent(this, DuelService.class));
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onOptionsMenuClosed(Menu menu) {
		// Nothing else to do, closing the Activity.
		finish();
	}

	/**
	 * Get speech input from user
	 */
	private int getSpeechInput() {
		Log.v(TAG, "About to get speech input");
		displaySpeechRecognizer();
		return convertStringsAndCombine();
	}

	/**
	 * Simply displays the speech recognizer
	 */
	private void displaySpeechRecognizer() {
		try {
			Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
			intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say some numbers!");
			intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "en-US");
			startActivityForResult(intent, SPEECH_REQUEST);
        } catch (ActivityNotFoundException a) {
        	Log.e(TAG, "ActivityNotFoundException");
        }
		
	}

	/**
	 * Convert the array list of strings to a total int value
	 */
	private int convertStringsAndCombine() {
		Log.v(TAG, "About to convert and combine strings");
		int sum = 0;
		Log.v(TAG, "list size: " + list.size());
		for (int i = 0; i < list.size(); i++) {
			String voiceResultsString = list.get(i);
			Log.v(TAG, "string: " + voiceResultsString);
			int lifeDifference;
			try {
				NumberFormat nf = NumberFormat.getIntegerInstance();
				nf.setParseIntegerOnly(true);
				lifeDifference = nf.parse(voiceResultsString).intValue();
			} catch (ParseException e) {
				Log.e(TAG, "ParseException");
				lifeDifference = 0;
			}
			sum += lifeDifference;
		}
		Log.v(TAG, "sum: " + ((Integer) sum).toString());
		return sum;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.v(TAG, "on activity result");
		// Check which request we're responding to
		if (requestCode == SPEECH_REQUEST) {
			Log.v(TAG, "request is SPEECH_REQUEST");
			// Make sure the request was successful
			if (resultCode == RESULT_OK) {
				Log.v(TAG, "result okay");
				list = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			} else {
				Log.v(TAG, "result not okay");
				list = new ArrayList<String>();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}
