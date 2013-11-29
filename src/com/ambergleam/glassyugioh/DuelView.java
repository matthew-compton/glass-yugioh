package com.ambergleam.glassyugioh;

import java.util.concurrent.TimeUnit;

import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DuelView extends RelativeLayout {

	/**
	 * Interface to listen for changes on the view layout.
	 */
	public interface ChangeListener {
		/** Notified of a change in the view. */
		public void onChange();
	}

	private TextView userLifeTextView, enemyLifeTextView;

	private boolean mStarted;
	private boolean mForceStart;
	private boolean mVisible;
	private boolean mRunning;

	// About 24 FPS.
	private static final long DELAY_MILLIS = 41;

	private ChangeListener mChangeListener;

	public DuelView(Context context) {
		this(context, null, 0);
	}

	public DuelView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public DuelView(Context context, AttributeSet attrs, int style) {
		super(context, attrs, style);
		LayoutInflater.from(context).inflate(R.layout.card_duel, this);
		userLifeTextView = (TextView) findViewById(R.id.userLife);
		enemyLifeTextView = (TextView) findViewById(R.id.enemyLife);
	}

	/**
	 * Set a {@link ChangeListener}.
	 */
	public void setListener(ChangeListener listener) {
		mChangeListener = listener;
	}

	/**
	 * Set whether or not to force the start of the chronometer when a window has not been attached
	 * to the view.
	 */
	public void setForceStart(boolean forceStart) {
		mForceStart = forceStart;
		updateRunning();
	}

	/**
	 * Start the chronometer.
	 */
	public void start() {
		mStarted = true;
		updateRunning();
	}

	/**
	 * Stop the chronometer.
	 */
	public void stop() {
		mStarted = false;
		updateRunning();
	}

	@Override
	protected void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		mVisible = false;
		updateRunning();
	}

	@Override
	protected void onWindowVisibilityChanged(int visibility) {
		super.onWindowVisibilityChanged(visibility);
		mVisible = (visibility == VISIBLE);
		updateRunning();
	}

	private final Handler mHandler = new Handler();

	private final Runnable mUpdateTextRunnable = new Runnable() {
		@Override
		public void run() {
			if (mRunning) {
				updateText();
				mHandler.postDelayed(mUpdateTextRunnable, DELAY_MILLIS);
			}
		}
	};

	/**
	 * Update the running state of the chronometer.
	 */
	private void updateRunning() {
		boolean running = (mVisible || mForceStart) && mStarted;
		if (running != mRunning) {
			if (running) {
				mHandler.post(mUpdateTextRunnable);
			} else {
				mHandler.removeCallbacks(mUpdateTextRunnable);
			}
			mRunning = running;
		}
	}

	/**
	 * Update the state of the duel.
	 */
	private void updateText() {
		userLifeTextView.setText("7500");
		enemyLifeTextView.setText("7800");
		if (mChangeListener != null) {
			mChangeListener.onChange();
		}
	}
}
