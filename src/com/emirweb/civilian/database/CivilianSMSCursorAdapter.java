package com.emirweb.civilian.database;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

public class CivilianSMSCursorAdapter extends CursorAdapter {

	public CivilianSMSCursorAdapter(Context context, Cursor c, int flags) {
		super(context, c, flags);
	}

	public CivilianSMSCursorAdapter(Context context, Cursor c) {
		super(context, c);
	}

	public CivilianSMSCursorAdapter(Context context, Cursor c, boolean autoRequery) {
		super(context, c, autoRequery);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
		return null;
	}

}
