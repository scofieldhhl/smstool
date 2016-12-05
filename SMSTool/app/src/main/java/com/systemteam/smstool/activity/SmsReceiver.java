package com.systemteam.smstool.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

/**
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2016/12/5 20:25
 */

public class SmsReceiver extends BroadcastReceiver {
    public static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    public SmsReceiver() {
        Log.i("cky", "new SmsReceiver");
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        Log.i("cky", "jie shou dao");
        Cursor cursor = null;
        try {
            if (SMS_RECEIVED.equals(intent.getAction())) {
                Log.d("cky", "sms received!");
                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    final SmsMessage[] messages = new SmsMessage[pdus.length];
                    for (int i = 0; i < pdus.length; i++) {
                        messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    }
                    if (messages.length > 0) {
                        String msgBody = messages[0].getMessageBody();
                        String msgAddress = messages[0].getOriginatingAddress();
                        long msgDate = messages[0].getTimestampMillis();
                        String smsToast = "New SMS received from : "
                                + msgAddress + "\n'"
                                + msgBody + "'";
                        Toast.makeText(context, smsToast, Toast.LENGTH_LONG)
                                .show();
                        Log.d("cky", "message from: " + msgAddress + ", message body: " + msgBody
                                + ", message date: " + msgDate);
                    }
                }
                cursor = context.getContentResolver().query(Uri.parse("content://sms"), new String[] { "_id", "address", "read", "body", "date" }, "read = ? ", new String[] { "0" }, "date desc");
                if (null == cursor){
                    return;
                }

                Log.i("cky","m cursor count is "+cursor.getCount());
                Log.i("cky","m first is "+cursor.moveToFirst());


            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("cky", "Exception : " + e);
        } finally {
            if (cursor != null) {
                cursor.close();
                cursor = null;
            }
        }

    }
}

    /*Bundle bundle = intent.getExtras();
    SmsMessage msg = null;
if (null != bundle) {
        Object[] smsObj = (Object[]) bundle.get("pdus");
        for (Object object : smsObj) {
        msg = SmsMessage.createFromPdu((byte[]) object);
        Date date = new Date(msg.getTimestampMillis());//时间
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String receiveTime = format.format(date);
        System.out.println("number:" + msg.getOriginatingAddress()
        + "   body:" + msg.getDisplayMessageBody() + "  time:"
        + msg.getTimestampMillis());

        //在这里写自己的逻辑
        if (msg.getOriginatingAddress().equals("10086")) {
        //TODO

        }

        }
        }  */