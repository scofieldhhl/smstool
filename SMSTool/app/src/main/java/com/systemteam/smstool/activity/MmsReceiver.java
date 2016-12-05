package com.systemteam.smstool.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2016/12/5 20:23
 */

public class MmsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("cky","MmsReceiver: "+intent);
    }

}