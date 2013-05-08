package com.emirweb.civilian;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SmsBroadcastReceiver extends BroadcastReceiver {

	private static final String SMS_EXTRA_NAME = "pdus";

	@Override
	public void onReceive(Context context, Intent intent) {
		// ---get the SMS message passed in---
		Bundle bundle = intent.getExtras();
		SmsMessage[] msgs = null;
		String str = "";
		if (bundle != null) {
			// ---retrieve the SMS message received---
			Object[] pdus = (Object[]) bundle.get("pdus");
			msgs = new SmsMessage[pdus.length];
			for (int i = 0; i < msgs.length; i++) {
				msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
				SmsMessage smsMessage = msgs[i];

				Log.d("civilian", "smsMessage.getDisplayMessageBody(): " + smsMessage.getDisplayMessageBody());
				Log.d("civilian", "smsMessage.getDisplayOriginatingAddress(): " + smsMessage.getDisplayOriginatingAddress());
				Log.d("civilian", "smsMessage.getEmailBody(): " + smsMessage.getEmailBody());
				Log.d("civilian", "smsMessage.getEmailFrom(): " + smsMessage.getEmailFrom());
				Log.d("civilian", "smsMessage.getIndexOnIcc(): " + smsMessage.getIndexOnIcc());
				Log.d("civilian", "smsMessage.getIndexOnSim(): " + smsMessage.getIndexOnSim());
				Log.d("civilian", "smsMessage.getMessageBody(): " + smsMessage.getMessageBody());
				Log.d("civilian", "smsMessage.getOriginatingAddress(): " + smsMessage.getOriginatingAddress());
				Log.d("civilian", "smsMessage.getProtocolIdentifier(): " + smsMessage.getProtocolIdentifier());
				Log.d("civilian", "smsMessage.getPseudoSubject(): " + smsMessage.getPseudoSubject());
				Log.d("civilian", "smsMessage.getServiceCenterAddress(): " + smsMessage.getServiceCenterAddress());
				Log.d("civilian", "smsMessage.getStatus(): " + smsMessage.getStatus());
				Log.d("civilian", "smsMessage.getStatusOnIcc(): " + smsMessage.getStatusOnIcc());
				Log.d("civilian", "smsMessage.getStatusOnSim(): " + smsMessage.getStatusOnSim());
				Log.d("civilian", "smsMessage.getTimestampMillis(): " + smsMessage.getTimestampMillis());
				Log.d("civilian", "smsMessage.isCphsMwiMessage(): " + smsMessage.isCphsMwiMessage());
				Log.d("civilian", "smsMessage.isEmail(): " + smsMessage.isEmail());
				Log.d("civilian", "smsMessage.isMWIClearMessage(): " + smsMessage.isMWIClearMessage());
				Log.d("civilian", "smsMessage.isReplyPathPresent(): " + smsMessage.isReplyPathPresent());

				str += "SMS from " + msgs[i].getOriginatingAddress();
				str += " :";
				str += msgs[i].getMessageBody().toString();
				str += "\n";

				String destinationAddress = smsMessage.getOriginatingAddress();
				CivilianContentManager.receiveSMS(smsMessage, context);

			}
			// ---display the new SMS message---
			Log.d("civilian", str);
			Toast.makeText(context, str, Toast.LENGTH_SHORT).show();

		}
	}


}
