package com.emirweb.civilian.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SMSSQLiteOpenHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "civilian";
	private static final int DATABASE_VERSION = 2;

	static final String TABLE_SMS = "sms";

	static final String COLUMN_ID = "_id";
	static final String COLUMN_ADDRESS = "address";
	static final String COLUMN_MESSAGE = "message";
	static final String COLUMN_TIMESTAMP = "timestamp";
	static final String COLUMN_SMS_STATE = "sms_state";
	static final String COLUMN_NETWORK_STATE = "network_state";
	static final String COLUMN_NETWORK_SMS_STATE = "network_sms_state";

	static final String SMS_STATE_SMS_SEND = "sms_send";
	static final String SMS_STATE_SMS_SENDING = "sms_sending";
	static final String SMS_STATE_SMS_DELIVERED = "sms_delivered";
	static final String SMS_STATE_SMS_CANCELED = "sms_canceled";
	static final String SMS_STATE_SMS_RESULT_OK = "sms_result_ok";
	static final String SMS_STATE_SMS_RESULT_ERROR_GENERIC_FAILURE = "sms_result_error_generic_failure";
	static final String SMS_STATE_SMS_RESULT_ERROR_NO_SERVICE = "sms_result_error_no_service";
	static final String SMS_STATE_SMS_RESULT_ERROR_NULL_PDU = "sms_result_error_null_pdu";
	static final String SMS_STATE_SMS_RESULT_ERROR_RADIO_OFF = "sms_result_error_radio_off";
	static final String SMS_STATE_SMS_RECEIVED = "sms_received";

	static final String NETWORK_STATE_NETWORK_SEND = "network_send";
	static final String NETWORK_STATE_NETWORK_SENDING = "network_sending";
	static final String NETWORK_STATE_NETWORK_SENT = "network_sent";
	static final String NETWORK_STATE_NETWORK_DELIVERED = "network_delivered";
	static final String NETWORK_STATE_NETWORK_RECEIVED = "network_received";

	static final String COLUMN_DIRECTION = "direction";
	static final String DIRECTION_TO = "to";
	static final String DIRECTION_FROM = "from";

	private static final String COLUMN_ID_DECLERATION = COLUMN_ID + " integer primary key autoincrement";
	private static final String COLUMN_ADDRESS_DECLERATION = COLUMN_ADDRESS + " text";
	private static final String COLUMN_MESSAGE_DECLERATION = COLUMN_MESSAGE + " text";
	private static final String COLUMN_TIMESTAMP_DECLERATION = COLUMN_TIMESTAMP + " integer not null";
	private static final String COLUMN_SMS_STATE_DECLERATION = COLUMN_SMS_STATE + " text";
	private static final String COLUMN_NETWORK_STATE_DECLERATION = COLUMN_NETWORK_STATE + " text";
	private static final String COLUMN_NETWORK_SMS_DECLERATION = COLUMN_NETWORK_SMS_STATE + " text";
	private static final String COLUMN_DIRECTION_SENT_DECLERATION = COLUMN_DIRECTION + " text not null";

	private static String DATABASE_CREATE = "create table " + TABLE_SMS + "(" + COLUMN_ID_DECLERATION + ", " + COLUMN_ADDRESS_DECLERATION + ", "
			+ COLUMN_MESSAGE_DECLERATION + ", " + COLUMN_TIMESTAMP_DECLERATION + ", " + COLUMN_SMS_STATE_DECLERATION + ", "
			+ COLUMN_NETWORK_STATE_DECLERATION + ", " + COLUMN_NETWORK_SMS_DECLERATION + ", " + COLUMN_DIRECTION_SENT_DECLERATION + ");";

	public SMSSQLiteOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		database.execSQL("DROP TABLE IF EXISTS " + TABLE_SMS);
		onCreate(database);
	}

	@Override
	public void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}
}
