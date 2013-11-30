package com.ambergleam.glassyugioh;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/**
 * Activity showing the options menu.
 */
public class MenuActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
				// TODO - add voice recognition
				DuelService.userLife -= 800;
				return true;
			case R.id.loseLifeEnemy:
				// TODO - add voice recognition
				DuelService.enemyLife -= 800;
				return true;
			case R.id.addLifeUser:
				// TODO - add voice recognition
				DuelService.userLife += 800;
				return true;
			case R.id.addLifeEnemy:
				// TODO - add voice recognition
				DuelService.enemyLife += 800;
				return true;
			case R.id.reset:
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
}
