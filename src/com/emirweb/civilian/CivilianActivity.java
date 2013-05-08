package com.emirweb.civilian;

import android.app.Activity;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.emirweb.civilian.database.CivilianContentProvider;

public class CivilianActivity extends Activity {

	private static final String ADDRESS = "+14168228694";

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		setContentView(R.layout.civilian_acctivity);

		final ListView listView = (ListView) findViewById(R.id.messages);

		final CursorAdapter cursorAdapter = new CursorAdapter(getApplicationContext(), null, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER) {

			@Override
			public View newView(final Context context, final Cursor cursor, final ViewGroup parent) {
				final LayoutInflater inflater = LayoutInflater.from(context);
				final View view = inflater.inflate(R.layout.message, parent, false);
				bindView(view, context, cursor);
				return view;
			}

			@Override
			public void bindView(final View view, final Context context, final Cursor cursor) {
				final TextView message = (TextView) view;
				message.setText(cursor.getString(cursor.getColumnIndex(CivilianContentProvider.COLUMN_MESSAGE)));
			}
		};
		listView.setAdapter(cursorAdapter);

		final LoaderCallbacks<Cursor> loaderCallbacks = new LoaderCallbacks<Cursor>() {

			@Override
			public void onLoaderReset(final Loader<Cursor> cursorLoader) {
				cursorAdapter.swapCursor(null);
			}

			@Override
			public void onLoadFinished(final Loader<Cursor> cursorLoader, final Cursor cursor) {
				cursorAdapter.swapCursor(cursor);
			}

			@Override
			public Loader<Cursor> onCreateLoader(final int id, final Bundle args) {
				return new CursorLoader(getApplicationContext(), CivilianContentProvider.SMS_URI, new String[] { CivilianContentProvider.COLUMN_ADDRESS,
						CivilianContentProvider.COLUMN_ID, CivilianContentProvider.COLUMN_MESSAGE}, new String(), new String[] {}, null);
			}
		};

		final LoaderManager loaderManager = getLoaderManager();
		loaderManager.initLoader(0, null, loaderCallbacks);

		final EditText messageView = (EditText) findViewById(R.id.message);
		messageView.setOnEditorActionListener(new EditText.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(final TextView v, final int actionId, final KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEND) {
					sendMessage(messageView, ADDRESS);
					return true;
				}
				return false;
			}
		});

		final Button button = (Button) findViewById(R.id.send);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(final View view) {
				sendMessage(messageView, ADDRESS);
			}
		});

		startCivilianService();

		super.onCreate(savedInstanceState);
	}

	private void sendMessage(final EditText messageView, final String address) {
		final String message = messageView.getText().toString();
		messageView.setText(null);

		if (message == null || message.isEmpty() || address == null || address.isEmpty())
			return;

		sendSMS(address, message);

	}

	private void sendSMS(final String address, final String message) {
		CivilianContentManager.sendSMS(getApplicationContext(), address, message);
	}

	private void startCivilianService() {
		final Intent intent = new Intent(getApplicationContext(), CivilianService.class);
		startService(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		getMenuInflater().inflate(R.menu.civilian_acctivity, menu);
		return true;
	}

}
