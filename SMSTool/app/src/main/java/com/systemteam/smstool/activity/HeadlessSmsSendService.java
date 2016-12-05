package com.systemteam.smstool.activity;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2016/12/5 20:24
 */

public class HeadlessSmsSendService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        Log.i("cky","HeadlessSmsSendService: "+intent);
        return null;
    }

}
