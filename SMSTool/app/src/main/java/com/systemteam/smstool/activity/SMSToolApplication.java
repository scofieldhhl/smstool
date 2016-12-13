package com.systemteam.smstool.activity;

import android.app.Application;

import com.systemteam.smstool.provider.db.DbCore;

import org.greenrobot.greendao.query.QueryBuilder;

/**
 * 类描述：
 * 创建人：Administrator
 * 创建时间：2016/12/13 13:45
 */

public class SMSToolApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DbCore.init(this);
        QueryBuilder.LOG_SQL = true;
        QueryBuilder.LOG_VALUES =true;
    }
}
