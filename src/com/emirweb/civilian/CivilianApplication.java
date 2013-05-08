package com.emirweb.civilian;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Handler;


public class CivilianApplication extends Application{
	private static final int NUM_THREADS = 10;
	private static final ScheduledExecutorService mScheduledExecutorService = Executors.newScheduledThreadPool(NUM_THREADS);
	private static final String NULL_RUNNABLE_ERROR = "Runnable is null.";

	private static Handler mHandler;
	private boolean mIsDebuggable;
	private final String CIVILIAN_APPLICATION = "CivilianApplication";

	@Override
	public void onCreate() {
		final PackageManager pm = getPackageManager();
		try {
			final ApplicationInfo applicationInfo = pm.getApplicationInfo(getPackageName(), 0);
			mIsDebuggable = ( applicationInfo.flags & ApplicationInfo.FLAG_DEBUGGABLE ) != 0;
		} catch (NameNotFoundException e) {
			mIsDebuggable = false;
		} catch (NullPointerException e) {
			mIsDebuggable = false;
		}
		mHandler = new Handler();
		super.onCreate();
	}

	public static void runOnUiThread(final Runnable runnable) {
		if (runnable == null)
			throw new IllegalArgumentException(NULL_RUNNABLE_ERROR);
		mHandler.post(runnable);
	}

	public static void delayRunOnUiThread(final Runnable runnable, final long delay) {
		if (runnable == null)
			throw new IllegalArgumentException(NULL_RUNNABLE_ERROR);
		mHandler.postDelayed(runnable, delay);
	}

	public static void runOnBackgroundThread(final Runnable runnable) {
		if (runnable == null)
			throw new IllegalArgumentException(NULL_RUNNABLE_ERROR);
		mScheduledExecutorService.execute(runnable);
	}

	public static void delayRunOnBackgroundThread(final Runnable runnable, final long delay) {
		if (runnable == null)
			throw new IllegalArgumentException(NULL_RUNNABLE_ERROR);
		mScheduledExecutorService.schedule(runnable, delay, TimeUnit.MILLISECONDS);
	}
}
