package com.systemteam.smstool.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.systemteam.smstool.R;
import com.systemteam.smstool.bean.Customer;
import com.systemteam.smstool.provider.db.CustomerHelper;
import com.systemteam.smstool.provider.db.DbUtil;
import com.systemteam.smstool.view.IconEditTextView;

public class UserAddActivity extends BaseActivity implements View.OnClickListener{
    IconEditTextView mIetName, mIetPhone, mIetHome, mIetCompany, mIetRemark;
    RadioButton mRbSex;
    Button mBtnSubmit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_add);
        initView();
    }

    @Override
    protected void initView() {
        mIetName = (IconEditTextView) findViewById(R.id.iet_name);
        mIetPhone = (IconEditTextView) findViewById(R.id.iet_phonenum);
        mIetHome = (IconEditTextView) findViewById(R.id.iet_homeAddress);
        mIetCompany = (IconEditTextView) findViewById(R.id.iet_companyAddress);
        mIetRemark = (IconEditTextView) findViewById(R.id.iet_remarks);
        mRbSex = (RadioButton) findViewById(R.id.radioButton);
        mBtnSubmit = (Button) findViewById(R.id.btn_submit);
        mBtnSubmit.setOnClickListener(this);
    }

    @Override
    protected void initData() {

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
                Customer customer = new Customer();
                customer.setName(mIetName.getInputText());
                if(mRbSex.isChecked()){
                    customer.setSex(true);
                }else {
                    customer.setSex(false);
                }
                customer.setPhoneNum("   " + mIetPhone.getInputText());
                customer.setHomeAddress(mIetHome.getInputText());
                customer.setCompanyAddress(mIetCompany.getInputText());
                customer.setRemarks(mIetRemark.getInputText());

                CustomerHelper mHelper = DbUtil.getCustomerHelper();
                mHelper.save(customer);
                Toast.makeText(UserAddActivity.this, getString(R.string.save_success), Toast.LENGTH_SHORT).show();
                clearInput();
                break;
        }
    }

    private void clearInput(){
        mIetName.setText("");
        mIetPhone.setText("");
        mIetHome.setText("");
        mIetCompany.setText("");
        mIetRemark.setText("");
    }
}
