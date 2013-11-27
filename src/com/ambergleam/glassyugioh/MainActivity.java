package com.ambergleam.glassyugioh;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity {

	private int userLife = 8000, enemyLife = 8000;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	/*
	 * Updates the screen
	 */
	private void updateState() {
		((TextView) findViewById(R.id.userLife)).setText(Integer.toString(userLife));
		((TextView) findViewById(R.id.enemyLife)).setText(Integer.toString(enemyLife));
	}

	@Override
	protected void onResume() {
		super.onResume();
		updateState();
	}

}
