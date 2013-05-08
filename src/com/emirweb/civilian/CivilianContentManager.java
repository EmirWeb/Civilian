package com.emirweb.civilian;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.telephony.SmsMessage;

import com.emirweb.civilian.database.CivilianContentProvider;

public class CivilianContentManager {

	public static void sendSMS(final Context context, final String address, final String message) {
		final ContentValues contentValues = new ContentValues();
		contentValues.put(CivilianContentProvider.COLUMN_MESSAGE, message);
		contentValues.put(CivilianContentProvider.COLUMN_ADDRESS, address);
		contentValues.put(CivilianContentProvider.COLUMN_TIMESTAMP, System.nanoTime());
		contentValues.put(CivilianContentProvider.COLUMN_SMS_STATE, CivilianContentProvider.SMS_STATE_SMS_SEND);
		contentValues.put(CivilianContentProvider.COLUMN_DIRECTION, CivilianContentProvider.DIRECTION_TO);
		insert(context, contentValues);
	}

	private static void insert(final Context context, final ContentValues contentValues) {
		CivilianApplication.runOnBackgroundThread(new Runnable() {

			@Override
			public void run() {
				final Uri uri = context.getContentResolver().insert(CivilianContentProvider.SMS_URI, contentValues);
				startCivilianService(context);
			}
		});
	}

	private static void startCivilianService(final Context context) {
		final Intent intent = new Intent(context, CivilianService.class);
		context.startService(intent);
	}

	public static Cursor getPendingSMS(Context context) {
		return context.getContentResolver().query(CivilianContentProvider.SMS_URI,
				new String[] { CivilianContentProvider.COLUMN_ADDRESS, CivilianContentProvider.COLUMN_ID, CivilianContentProvider.COLUMN_MESSAGE },
				CivilianContentProvider.COLUMN_SMS_STATE + "=?", new String[] { CivilianContentProvider.SMS_STATE_SMS_SEND }, CivilianContentProvider.COLUMN_TIMESTAMP);
	}

	public static void receiveSMS(SmsMessage smsMessage, Context context) {
		
		final String message = smsMessage.getMessageBody();
		final String address = smsMessage.getOriginatingAddress();
		final ContentValues contentValues = new ContentValues();
		contentValues.put(CivilianContentProvider.COLUMN_MESSAGE, message);
		contentValues.put(CivilianContentProvider.COLUMN_ADDRESS, address);
		contentValues.put(CivilianContentProvider.COLUMN_TIMESTAMP, System.nanoTime());
		contentValues.put(CivilianContentProvider.COLUMN_SMS_STATE, CivilianContentProvider.SMS_STATE_SMS_SEND);
		contentValues.put(CivilianContentProvider.COLUMN_DIRECTION, CivilianContentProvider.DIRECTION_TO);
		insert(context, contentValues);
		
	}

}
