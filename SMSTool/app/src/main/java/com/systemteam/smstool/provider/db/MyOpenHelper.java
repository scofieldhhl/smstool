/*
******************* Copyright (c) ***********************\
**
**         (c) Copyright 2016, 蒋朋, china, sxkj. sd
**                  All Rights Reserved
**
**                 By(青岛世新科技有限公司)
**                    www.qdsxkj.com
**
**                       _oo0oo_
**                      o8888888o
**                      88" . "88
**                      (| -_- |)
**                      0\  =  /0
**                    ___/`---'\___
**                  .' \\|     |// '.
**                 / \\|||  :  |||// \
**                / _||||| -:- |||||- \
**               |   | \\\  -  /// |   |
**               | \_|  ''\---/''  |_/ |
**               \  .-\__  '-'  ___/-. /
**             ___'. .'  /--.--\  `. .'___
**          ."" '<  `.___\_<|>_/___.' >' "".
**         | | :  `- \`.;`\ _ /`;.`/ - ` : | |
**         \  \ `_.   \_ __\ /__ _/   .-` /  /
**     =====`-.____`.___ \_____/___.-`___.-'=====
**                       `=---='
**
**
**     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
**
**               佛祖保佑         永无BUG
**
**
**                   南无本师释迦牟尼佛
**

**----------------------版本信息------------------------
** 版    本: V0.1
**
******************* End of Head **********************\
*/

package com.systemteam.smstool.provider.db;

import android.content.Context;

import com.systemteam.smstool.dao.DaoMaster;
import com.systemteam.smstool.util.LogTool;

import org.greenrobot.greendao.database.Database;

/**
 * @Description 升级
 * @author scofield.hhl@gmail.com
 * @time 2016/12/2
 */
public class MyOpenHelper extends DaoMaster.OpenHelper {
    public MyOpenHelper(Context context, String name) {
        super(context, name);
    }


    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        LogTool.w("db version update from " + oldVersion + " to " + newVersion);

        switch (oldVersion) {
            case 1:
                //不能先删除表，否则数据都木了
//                StudentDao.dropTable(db, true);
//                TasksManagerModelDao.createTable(db, true);
                // 加入新字段
//                db.execSQL("ALTER TABLE 'TASKS_MANAGER_MODEL' ADD 'FORMAT' TEXT;");
                break;
        }

    }
}
