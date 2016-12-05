package com.systemteam.smstool.dao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.systemteam.smstool.bean.Customer;
import com.systemteam.smstool.bean.SMSRecord;

import com.systemteam.smstool.dao.CustomerDao;
import com.systemteam.smstool.dao.SMSRecordDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig customerDaoConfig;
    private final DaoConfig sMSRecordDaoConfig;

    private final CustomerDao customerDao;
    private final SMSRecordDao sMSRecordDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        customerDaoConfig = daoConfigMap.get(CustomerDao.class).clone();
        customerDaoConfig.initIdentityScope(type);

        sMSRecordDaoConfig = daoConfigMap.get(SMSRecordDao.class).clone();
        sMSRecordDaoConfig.initIdentityScope(type);

        customerDao = new CustomerDao(customerDaoConfig, this);
        sMSRecordDao = new SMSRecordDao(sMSRecordDaoConfig, this);

        registerDao(Customer.class, customerDao);
        registerDao(SMSRecord.class, sMSRecordDao);
    }
    
    public void clear() {
        customerDaoConfig.clearIdentityScope();
        sMSRecordDaoConfig.clearIdentityScope();
    }

    public CustomerDao getCustomerDao() {
        return customerDao;
    }

    public SMSRecordDao getSMSRecordDao() {
        return sMSRecordDao;
    }

}