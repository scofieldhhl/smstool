package com.systemteam.smstool.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.Toast;

import com.systemteam.smstool.R;
import com.systemteam.smstool.bean.Customer;
import com.systemteam.smstool.dao.CustomerDao;
import com.systemteam.smstool.provider.db.CustomerHelper;
import com.systemteam.smstool.provider.db.DbUtil;
import com.systemteam.smstool.view.IconEditTextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class UserAddActivity extends BaseActivity implements View.OnClickListener{
    public static final String ID = "ID";
    public static final int ID_DIALOG_REGISTER = 999;
    IconEditTextView mIetName, mIetPhone, mIetHome, mIetCompany, mIetRemark;
    RadioButton mRbSexMan, mRbSexWo;
    Button mBtnSubmit, mIetRegister;
    Customer mCustomer;
    CustomerHelper mHelper;
    InputMethodManager mImm;
    SimpleDateFormat sdf;
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
                if(!b){
                    mIetPhone.setText(mIetPhone.getInputText().replace(" ", ""));
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
                    }else {
                        customer = mHelper.queryBuilder()
                                .where(CustomerDao.Properties.PhoneNum.eq("+86"+mIetPhone.getInputText()))
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
            }
        });
        mIetRegister = (Button) findViewById(R.id.btn_register);
    }

    int year, yearf, yeart, month, monthf, montht, day, dayf, dayt;
    @Override
    protected void initData() {
        mHelper = DbUtil.getCustomerHelper();
        mImm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        sdf = new SimpleDateFormat("yyyy/MM/dd");
        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        yearf = yeart = year;
        monthf = montht = month;
        dayf = dayt = day;
        frm_lstnr.onDateSet(new DatePicker(this), year, month, day);//初始化时间

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
            if(mCustomer.getRegisterTime() != null){
                mIetRegister.setText(sdf.format(mCustomer.getRegisterTime()));
            }

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
                mCustomer.setPhoneNum(mIetPhone.getInputText().replace(" ", ""));
                mCustomer.setHomeAddress(mIetHome.getInputText());
                mCustomer.setCompanyAddress(mIetCompany.getInputText());
                mCustomer.setRemarks(mIetRemark.getInputText());
                mCustomer.setTime(new Date());
                try{
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                    mCustomer.setRegisterTime(sdf.parse(mIetRegister.getText().toString()));
                }catch (ParseException e) {
                    e.printStackTrace();
                }

                mHelper.saveOrUpdate(mCustomer);
                Toast.makeText(UserAddActivity.this, getString(R.string.save_success), Toast.LENGTH_SHORT).show();
                mCustomer = null;
                initContent(mCustomer);
                mImm.hideSoftInputFromWindow(v.getWindowToken(), 0); //强制隐藏键盘
                break;
            case R.id.btn_register:
                showDialog(ID_DIALOG_REGISTER);
                break;
        }
    }

    public void ButtonOnClick(View v) {

        switch (v.getId()) {
            case R.id.btn_register:
                showDialog(ID_DIALOG_REGISTER);
                break;
        }
    }
    String DATE_FORMAT = "%d/%d/%d";
    public DatePickerDialog.OnDateSetListener frm_lstnr = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int yeard, int monthd, int dayd) {
                yearf = yeard;
                monthf = monthd;
                dayf = dayd;
                mIetRegister.setText(String.format(Locale.US, DATE_FORMAT, yearf, (monthf + 1), dayf));
        }
    };

    @Override
    public Dialog onCreateDialog(int id) {
        //Log.d("sms8e", "oncrtdlg_enx");
        switch (id) {
            case ID_DIALOG_REGISTER:
                //Log.d("sms8e", "oncrtdlg_frm");
                return new DatePickerDialog(this, 0, frm_lstnr, yearf,
                        monthf, dayf) {
                    public void onDateChanged(DatePicker view, int yearf, int monthf, int dayf) {
                        if (yearf > year) {
                            Toast.makeText(mContext, getString(R.string.frm_to_err), Toast.LENGTH_SHORT).show();
                            view.updateDate(year, monthf, dayf);
                        } else if (yearf == year) {
                            if (monthf > month) {
                                Toast.makeText(mContext, getString(R.string.frm_to_err), Toast.LENGTH_SHORT).show();
                                view.updateDate(yearf, month, dayf);
                            } else if (monthf == month) {
                                if (dayf > day) {
                                    Toast.makeText(mContext, getString(R.string.frm_to_err), Toast.LENGTH_SHORT).show();
                                    view.updateDate(yearf, monthf, day);
                                }
                            }
                        }
                    }
                };


        }
        return null;
    }
}
