package com.systemteam.smstool.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.systemteam.smstool.R;
import com.systemteam.smstool.bean.Customer;

public class SMSDetailActivity extends BaseActivity {
    private Customer mCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_detail);
        initView();
        initData();
    }

    @Override
    protected void initView() {
        mContext = this;
    }

    @Override
    protected void initData() {
        Intent intent = getIntent();
        mCustomer = (Customer) intent.getSerializableExtra("customer");
        if(mCustomer == null){
            Toast.makeText(mContext, mContext.getString(R.string.error_input), Toast.LENGTH_SHORT).show();
        }else {
            initToolBar(this, mCustomer.getName()+"("+mCustomer.getPhoneNum()+")");
        }
    }
}
