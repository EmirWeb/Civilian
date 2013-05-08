package com.emirweb.civilian;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.emirweb.amqp.AMQPConsumerListener;
import com.emirweb.civilian.database.CivilianContentProvider;

public class CivilianService extends Service {
	
	private String SENT = "SMS_SENT";
	private String DELIVERED = "SMS_DELIVERED";

	private static final String NULL_RUNNABLE_ERROR = "Runnable is null.";

	private static final int NUM_THREADS = 10;
	private static final ScheduledExecutorService mScheduledExecutorService = Executors.newScheduledThreadPool(NUM_THREADS);
	private static final String EXCHANGE_NAME = "4168228694";
	private static final String QUEUE_NAME = "provider";
	private static final String BINDING = "provider";
	private static final String LOCAL_HOST = "localhost";
	private static final String HOST = "192.168.0.15";
	private static final String USERNAME = "guest";
	private static final String PASSWORD = "guest";
	private static Handler mHandler;
	
	
	private final AMQPConsumerListener amqpConsumerListener = new AMQPConsumerListener() {

		@Override
		public boolean onMessage(String message) {
			try {
				final JSONObject jsonObject = new JSONObject(message);
			} catch (JSONException e) {
				e.printStackTrace();
				return false;
			}
			return true;
		}

		@Override
		public void onClose(Exception e) {
			e.printStackTrace();
			stopSelf();
		}
	};
	
	
	@Override
	public void onCreate() {
		mHandler = new Handler();
		Log.d("civilian", "onCreate");
		
		getContentResolver().registerContentObserver(CivilianContentProvider.SMS_URI, true, new ContentObserver(null) {
			@Override
			public void onChange(boolean selfChange) {
				sendPendingSMS();
				super.onChange(selfChange);
			}
		});

//		runOnBackgroundThread(new Runnable() {
//
//			@Override
//			public void run() {
//				synchronized (CivilianService.this) {
//					try {
//						final AMQPManager amqpManager = new AMQPManager(HOST, USERNAME, PASSWORD, new ShutdownListener() {
//
//							@Override
//							public void shutdownCompleted(ShutdownSignalException arg0) {
//								stopSelf();
//							}
//						});
//						amqpManager.startConsume(EXCHANGE_NAME, QUEUE_NAME, BINDING, amqpConsumerListener );
//					} catch (IOException e) {
//						e.printStackTrace();
//						stopSelf();
//					}
//				}
//			}
//		});

		super.onCreate();
	}

	private void sendPendingSMS() {
		Cursor cursor = CivilianContentManager.getPendingSMS(getApplicationContext());
		
	}

	@Override
	public synchronized int onStartCommand(Intent intent, int flags, int startId) {
		Log.d("civilian", "onStartCommand");

		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
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
	
	int x;

	private void sendSMS(String phoneNumber, String message) {
		SENT += x;
		DELIVERED += x++;
		Intent sent = new Intent(SENT);
		sent.putExtra("EMIR", "WAS HERE");
		Intent delivered = new Intent(DELIVERED);
		delivered.putExtra("EMIR", "WAS HERE");

		PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, sent, PendingIntent.FLAG_UPDATE_CURRENT);

		PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0, delivered, PendingIntent.FLAG_UPDATE_CURRENT);

		final String key = Integer.toString(x);
		// ---when the SMS has been sent---
		registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent sent) {
				Log.d("civilian", "key: " + key);
				Log.d("civilian", "sent: " + sent);
				switch (getResultCode()) {
					case Activity.RESULT_OK:
						Log.d("civilian", "SMS sent");
						Toast.makeText(getBaseContext(), "SMS sent", Toast.LENGTH_SHORT).show();
						break;
					case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
						Log.d("civilian", "Generic failure");
						Toast.makeText(getBaseContext(), "Generic failure", Toast.LENGTH_SHORT).show();
						break;
					case SmsManager.RESULT_ERROR_NO_SERVICE:
						Log.d("civilian", "No service");
						Toast.makeText(getBaseContext(), "No service", Toast.LENGTH_SHORT).show();
						break;
					case SmsManager.RESULT_ERROR_NULL_PDU:
						Log.d("civilian", "Null PDU");
						Toast.makeText(getBaseContext(), "Null PDU", Toast.LENGTH_SHORT).show();
						break;
					case SmsManager.RESULT_ERROR_RADIO_OFF:
						Log.d("civilian", "Radio off");
						Toast.makeText(getBaseContext(), "Radio off", Toast.LENGTH_SHORT).show();
						break;
				}
				unregisterReceiver(this);
			}
		}, new IntentFilter(SENT));

		// ---when the SMS has been delivered---
		registerReceiver(new BroadcastReceiver() {
			@Override
			public void onReceive(Context arg0, Intent delivered) {
				Log.d("civilian", "key: " + key);
				Log.d("civilian", "delivered: " + delivered);
				switch (getResultCode()) {
					case Activity.RESULT_OK:
						Log.d("civilian", "SMS delivered");
						Toast.makeText(getBaseContext(), "SMS delivered", Toast.LENGTH_SHORT).show();
						break;
					case Activity.RESULT_CANCELED:
						Log.d("civilian", "SMS not delivered");
						Toast.makeText(getBaseContext(), "SMS not delivered", Toast.LENGTH_SHORT).show();
						break;
				}
				unregisterReceiver(this);
			}
		}, new IntentFilter(DELIVERED));

		Log.d("civilian", "sentPI: " + sentPI);
		Log.d("civilian", "sent: " + sent);
		Log.d("civilian", "deliveredPI: " + deliveredPI);
		Log.d("civilian", "delivered: " + delivered);

		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
	}

}
