package com.systemteam.smstool.dao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.systemteam.smstool.bean.SMSRecord;
import com.systemteam.smstool.bean.User;
import com.systemteam.smstool.bean.Customer;

import com.systemteam.smstool.dao.SMSRecordDao;
import com.systemteam.smstool.dao.UserDao;
import com.systemteam.smstool.dao.CustomerDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig sMSRecordDaoConfig;
    private final DaoConfig userDaoConfig;
    private final DaoConfig customerDaoConfig;

    private final SMSRecordDao sMSRecordDao;
    private final UserDao userDao;
    private final CustomerDao customerDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        sMSRecordDaoConfig = daoConfigMap.get(SMSRecordDao.class).clone();
        sMSRecordDaoConfig.initIdentityScope(type);

        userDaoConfig = daoConfigMap.get(UserDao.class).clone();
        userDaoConfig.initIdentityScope(type);

        customerDaoConfig = daoConfigMap.get(CustomerDao.class).clone();
        customerDaoConfig.initIdentityScope(type);

        sMSRecordDao = new SMSRecordDao(sMSRecordDaoConfig, this);
        userDao = new UserDao(userDaoConfig, this);
        customerDao = new CustomerDao(customerDaoConfig, this);

        registerDao(SMSRecord.class, sMSRecordDao);
        registerDao(User.class, userDao);
        registerDao(Customer.class, customerDao);
    }
    
    public void clear() {
        sMSRecordDaoConfig.clearIdentityScope();
        userDaoConfig.clearIdentityScope();
        customerDaoConfig.clearIdentityScope();
    }

    public SMSRecordDao getSMSRecordDao() {
        return sMSRecordDao;
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public CustomerDao getCustomerDao() {
        return customerDao;
    }

}
