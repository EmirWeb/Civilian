package com.emirweb.civilian.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class CivilianContentProvider extends ContentProvider {

	private static final String PROVIDER_NAME = "com.emirweb.civilian.sms.provider";
	private static final UriMatcher mUriMatcher;
	private static final int MATCH_SMS = 0;
	private static final String CONTENT = "content://";
	private static final String SMS = "sms";
	
	private SMSSQLiteOpenHelper mSmssqLiteOpenHelper;

	public static final String COLUMN_ID = SMSSQLiteOpenHelper.COLUMN_ID;
	public static final String COLUMN_ADDRESS = SMSSQLiteOpenHelper.COLUMN_ADDRESS;
	public static final String COLUMN_MESSAGE = SMSSQLiteOpenHelper.COLUMN_MESSAGE;
	public static final String COLUMN_TIMESTAMP = SMSSQLiteOpenHelper.COLUMN_TIMESTAMP;
	public static final String COLUMN_SMS_STATE = SMSSQLiteOpenHelper.COLUMN_SMS_STATE;
	public static final String COLUMN_NETWORK_STATE = SMSSQLiteOpenHelper.COLUMN_NETWORK_STATE;
	public static final String COLUMN_NETWORK_SMS_STATE = SMSSQLiteOpenHelper.COLUMN_NETWORK_SMS_STATE;
	
	
	public static final String COLUMN_DIRECTION = "direction";
	public static final String DIRECTION_TO =SMSSQLiteOpenHelper.DIRECTION_TO;
	public static final String DIRECTION_FROM = SMSSQLiteOpenHelper.DIRECTION_FROM;
	
	
	public static final String SMS_STATE_SMS_SEND = SMSSQLiteOpenHelper.SMS_STATE_SMS_SEND;
	public static final String SMS_STATE_SMS_SENDING = SMSSQLiteOpenHelper.SMS_STATE_SMS_SENDING;
	public static final String SMS_STATE_SMS_DELIVERED = SMSSQLiteOpenHelper.SMS_STATE_SMS_DELIVERED;
	public static final String SMS_STATE_SMS_CANCELED = SMSSQLiteOpenHelper.SMS_STATE_SMS_CANCELED;
	public static final String SMS_STATE_SMS_RESULT_OK = SMSSQLiteOpenHelper.SMS_STATE_SMS_RESULT_OK;
	public static final String SMS_STATE_SMS_RESULT_ERROR_GENERIC_FAILURE = SMSSQLiteOpenHelper.SMS_STATE_SMS_RESULT_ERROR_GENERIC_FAILURE;
	public static final String SMS_STATE_SMS_RESULT_ERROR_NO_SERVICE = SMSSQLiteOpenHelper.SMS_STATE_SMS_RESULT_ERROR_NO_SERVICE;
	public static final String SMS_STATE_SMS_RESULT_ERROR_NULL_PDU = SMSSQLiteOpenHelper.SMS_STATE_SMS_RESULT_ERROR_NULL_PDU;
	public static final String SMS_STATE_SMS_RESULT_ERROR_RADIO_OFF = SMSSQLiteOpenHelper.SMS_STATE_SMS_RESULT_ERROR_RADIO_OFF;
	public static final String SMS_STATE_SMS_RECEIVED = SMSSQLiteOpenHelper.SMS_STATE_SMS_RECEIVED;

	public static final String NETWORK_STATE_NETWORK_SEND = SMSSQLiteOpenHelper.NETWORK_STATE_NETWORK_SEND;
	public static final String NETWORK_STATE_NETWORK_SENDING = SMSSQLiteOpenHelper.NETWORK_STATE_NETWORK_SENDING;
	public static final String NETWORK_STATE_NETWORK_SENT = SMSSQLiteOpenHelper.NETWORK_STATE_NETWORK_SENT;
	public static final String NETWORK_STATE_NETWORK_DELIVERED = SMSSQLiteOpenHelper.NETWORK_STATE_NETWORK_DELIVERED;
	public static final String NETWORK_STATE_NETWORK_RECEIVED = SMSSQLiteOpenHelper.NETWORK_STATE_NETWORK_RECEIVED;
	

	public static final Uri CIVILAIN_URI = Uri.parse(CONTENT + PROVIDER_NAME);
	public static final Uri SMS_URI = Uri.parse(CONTENT + PROVIDER_NAME + "/" + SMS);
	
	static {
		mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		mUriMatcher.addURI(PROVIDER_NAME, SMS, MATCH_SMS);
		// uriMatcher.addURI(PROVIDER_NAME, "items/#", ITEM_ID);
	}

	@Override
	public synchronized void attachInfo(Context context, ProviderInfo info) {
		super.attachInfo(context, info);
	}


	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

	@Override
	public synchronized Uri insert(Uri uri, ContentValues values) {
		switch (mUriMatcher.match(uri)) {
			case 0:
				final SQLiteDatabase sqLiteDatabase = mSmssqLiteOpenHelper.getWritableDatabase();
				final long id = sqLiteDatabase.insert(SMSSQLiteOpenHelper.TABLE_SMS, null, values);
				getContext().getContentResolver().notifyChange(SMS_URI, null);
				return ContentUris.withAppendedId(SMS_URI, id);
			default:
				break;
		}

		return null;
	}

	@Override
	public synchronized boolean onCreate() {
		mSmssqLiteOpenHelper = new SMSSQLiteOpenHelper(getContext());
		return mSmssqLiteOpenHelper != null;
	}
	
	@Override
	public synchronized Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		switch (mUriMatcher.match(uri)) {
			case 0:
				final SQLiteDatabase sqLiteDatabase = mSmssqLiteOpenHelper.getReadableDatabase();
				final Cursor cursor =  sqLiteDatabase.query(SMSSQLiteOpenHelper.TABLE_SMS, projection, selection, selectionArgs, null, null, sortOrder);
				cursor.setNotificationUri(getContext().getContentResolver(), SMS_URI);
				return cursor;
			default:
				break;
		}

		return null;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		return 0;
	}

}
