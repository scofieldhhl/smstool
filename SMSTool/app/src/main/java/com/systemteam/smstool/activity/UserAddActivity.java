package com.systemteam.smstool.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.systemteam.smstool.R;
import com.systemteam.smstool.bean.Customer;
import com.systemteam.smstool.dao.CustomerDao;
import com.systemteam.smstool.provider.db.CustomerHelper;
import com.systemteam.smstool.provider.db.DbUtil;
import com.systemteam.smstool.util.LogTool;
import com.systemteam.smstool.view.IconEditTextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class UserAddActivity extends BaseActivity implements View.OnClickListener{
    public static final String ID = "ID";
    IconEditTextView mIetName, mIetPhone, mIetHome, mIetCompany, mIetRemark;
    RadioButton mRbSexMan, mRbSexWo;
    Button mBtnSubmit;
    Customer mCustomer;
    CustomerHelper mHelper;
    InputMethodManager mImm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_add);
        initView();
        initData();
    }

    @Override
    protected void initView() {
        initToolBar(this, R.string.customer);
        mIetName = (IconEditTextView) findViewById(R.id.iet_name);
        mIetPhone = (IconEditTextView) findViewById(R.id.iet_phonenum);
        mIetHome = (IconEditTextView) findViewById(R.id.iet_homeAddress);
        mIetCompany = (IconEditTextView) findViewById(R.id.iet_companyAddress);
        mIetRemark = (IconEditTextView) findViewById(R.id.iet_remarks);
        mRbSexMan = (RadioButton) findViewById(R.id.radioButton);
        mRbSexWo = (RadioButton) findViewById(R.id.radioButtonw2);
        mBtnSubmit = (Button) findViewById(R.id.btn_submit);
        mBtnSubmit.setOnClickListener(this);
        mIetPhone.getmInput().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                LogTool.d("onFocusChange:" + b);
                if(!b){
                    Customer customer = mHelper.queryBuilder()
                            .where(CustomerDao.Properties.PhoneNum.eq(mIetPhone.getInputText()))
                            .unique();
                    if(customer != null){
                        new AlertDialog.Builder(mContext)
                                .setTitle(mContext.getString(R.string.tip))
                                .setMessage(mContext.getString(R.string.user_exist))
                                .setPositiveButton(mContext.getString(R.string.ok), null)
                                .show();
                        initContent(customer);
                    }
                }
            }
        });
    }

    @Override
    protected void initData() {
        mHelper = DbUtil.getCustomerHelper();
        mImm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        Intent intent = getIntent();
        if(intent != null){
            Long id = intent.getLongExtra(ID, -1);
            if (id != -1){
                mCustomer = mHelper.query(id);
                initContent(mCustomer);
            }
        }
    }
    private void initContent(Customer customer){
        if(customer != null){
            mIetName.setText(customer.getName());
            mIetPhone.setText(customer.getPhoneNum());
            if(customer.getSex()){
                mRbSexMan.setChecked(true);
            }else {
                mRbSexWo.setChecked(true);
            }
            mIetHome.setText(customer.getHomeAddress());
            mIetCompany.setText(customer.getCompanyAddress());
            mIetRemark.setText(customer.getRemarks());
        }else {
            mIetName.setText("");
            mIetPhone.setText("");
            mIetHome.setText("");
            mIetCompany.setText("");
            mIetRemark.setText("");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_submit:
                if(mIetName.getInputText() == null || TextUtils.isEmpty(mIetName.getInputText())){
                    Toast.makeText(UserAddActivity.this, getString(R.string.name_null), Toast.LENGTH_SHORT).show();
                    return;
                }
                if(mIetPhone.getInputText() == null || TextUtils.isEmpty(mIetPhone.getInputText())){
                    Toast.makeText(UserAddActivity.this, getString(R.string.name_null), Toast.LENGTH_SHORT).show();
                    return;
                }
                if(mCustomer == null){
                    mCustomer = new Customer();
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
                    String dateString = formatter.format(new Date());
                    mCustomer.setId(Long.parseLong(dateString));
                }
                mCustomer.setName(mIetName.getInputText());
                if(mRbSexMan.isChecked()){
                    mCustomer.setSex(true);
                }else {
                    mCustomer.setSex(false);
                }
                mCustomer.setPhoneNum(mIetPhone.getInputText());
                mCustomer.setHomeAddress(mIetHome.getInputText());
                mCustomer.setCompanyAddress(mIetCompany.getInputText());
                mCustomer.setRemarks(mIetRemark.getInputText());

                mHelper.saveOrUpdate(mCustomer);
                Toast.makeText(UserAddActivity.this, getString(R.string.save_success), Toast.LENGTH_SHORT).show();
                mCustomer = null;
                initContent(mCustomer);
                mImm.hideSoftInputFromWindow(v.getWindowToken(), 0); //强制隐藏键盘
                break;
        }
    }
}
