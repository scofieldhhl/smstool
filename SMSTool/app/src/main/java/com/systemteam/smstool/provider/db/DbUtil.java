/*
******************************* Copyright (c)*********************************\
**
**                 (c) Copyright 2015, 蒋朋, china, qd. sd
**                          All Rights Reserved
**
**                           By()
**                         
**-----------------------------------版本信息------------------------------------
** 版    本: V0.1
**
**------------------------------------------------------------------------------
********************************End of Head************************************\
*/

package com.systemteam.smstool.provider.db;


import com.systemteam.smstool.dao.CustomerDao;

/**
 * @Description 获取表 Helper 的工具类
 * @author scofield.hhl@gmail.com
 * @time 2016/12/2
 */
public class DbUtil {
    private static CustomerHelper mCustomerHelper;
    private static RecordHelper mRecordHelper;

    private static CustomerDao getTaskModelDao() {
        return DbCore.getDaoSession().getCustomerDao();
    }

    public static CustomerHelper getCustomerHelper() {
        if (mCustomerHelper == null) {
            mCustomerHelper = new CustomerHelper(getTaskModelDao());
        }
        return mCustomerHelper;
    }

    public static RecordHelper getRecordHelper() {
        if (mRecordHelper == null) {
            mRecordHelper = new RecordHelper(getTaskModelDao());
        }
        return mRecordHelper;
    }


}
