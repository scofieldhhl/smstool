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


import com.systemteam.smstool.bean.SMSRecord;

import org.greenrobot.greendao.AbstractDao;

/**
 * @Description 具体表的实现类
 * @author scofield.hhl@gmail.com
 * @time 2016/12 */
public class RecordHelper extends BaseDbHelper<SMSRecord, Long> {


    public RecordHelper(AbstractDao dao) {
        super(dao);
    }
}
