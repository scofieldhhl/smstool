package com.systemteam.smstool.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.systemteam.smstool.R;
import com.systemteam.smstool.bean.Customer;
import com.systemteam.smstool.bean.Order;
import com.systemteam.smstool.provider.db.CustomerHelper;
import com.systemteam.smstool.provider.db.DbUtil;
import com.systemteam.smstool.view.IconEditTextView;

import java.util.List;
import java.util.Locale;

public class SMSSendActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener,
        View.OnFocusChangeListener, View.OnClickListener{
    IconEditTextView mIetPhone, mIetWayTem, mIetWayVol, mIetUpTem, mIetDownTem, mIetUpVol, mIetDownVol;
    TextView mTvSendTo, mTvSendContent;
    Button mBtnMake, mBtnSend;
    Customer mCustomer;
    CustomerHelper mHelper;
    InputMethodManager mImm;
    CheckBox[] mCbSwitch;
    LinearLayout[] mLlInput;
    TextView[] mTvSwitchName;
    Order mTemOrder, mVolOrder;
    LinearLayout mLlContentOrder;

    private String FORMAT_ORDER_TEM = "L%d: %s, %s";
    private String FORMAT_ORDER_VOL = "U%d: %s, %s";
    private String mOrderContent;
    private String OFF = "OFF";
    private String DEFAULT_UP_TEM = "O80";
    private String DEFAULT_DOWN_TEM = "-10";
    private String DEFAULT_UP_VOL = "412";
    private String DEFAULT_DOWN_VOL = "000";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_send);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    @Override
    protected void initView() {
        mContext = this;
        initToolBar(this, R.string.send_sms);
        mTvSendTo = (TextView) findViewById(R.id.tv_send_to);
        mIetPhone = (IconEditTextView) findViewById(R.id.iet_phonenum);

        mCbSwitch = new CheckBox[8];
        mLlInput = new LinearLayout[4];
        mTvSwitchName = new TextView[8];
        mLlInput[0] = (LinearLayout) findViewById(R.id.ll_up_tem);
        mLlInput[1] = (LinearLayout) findViewById(R.id.ll_down_tem);
        mLlInput[2] = (LinearLayout) findViewById(R.id.ll_up_vol);
        mLlInput[3] = (LinearLayout) findViewById(R.id.ll_down_vol);
        /*mLlInput[4] = (LinearLayout) findViewById(R.id.ll_up_relay_tem);
        mLlInput[5] = (LinearLayout) findViewById(R.id.ll_down_relay_tem);
        mLlInput[6] = (LinearLayout) findViewById(R.id.ll_up_relay_vol);
        mLlInput[7] = (LinearLayout) findViewById(R.id.ll_down_relay_vol);*/

        mTvSwitchName[0] = (TextView) findViewById(R.id.tv_up_tem);
        mTvSwitchName[1] = (TextView) findViewById(R.id.tv_down_tem);
        mTvSwitchName[2] = (TextView) findViewById(R.id.tv_up_vol);
        mTvSwitchName[3] = (TextView) findViewById(R.id.tv_down_vol);
        mTvSwitchName[4] = (TextView) findViewById(R.id.tv_up_relay_tem);
        mTvSwitchName[5] = (TextView) findViewById(R.id.tv_down_relay_tem);
        mTvSwitchName[6] = (TextView) findViewById(R.id.tv_up_relay_vol);
        mTvSwitchName[7] = (TextView) findViewById(R.id.tv_down_relay_vol);

        mCbSwitch[0] = (CheckBox) findViewById(R.id.cb_up_tem);
        mCbSwitch[1] = (CheckBox) findViewById(R.id.cb_down_tem);
        mCbSwitch[2] = (CheckBox) findViewById(R.id.cb_up_vol);
        mCbSwitch[3] = (CheckBox) findViewById(R.id.cb_down_vol);
        mCbSwitch[4] = (CheckBox) findViewById(R.id.cb_up_relay_tem);
        mCbSwitch[5] = (CheckBox) findViewById(R.id.cb_down_relay_tem);
        mCbSwitch[6] = (CheckBox) findViewById(R.id.cb_up_relay_vol);
        mCbSwitch[7] = (CheckBox) findViewById(R.id.cb_down_relay_vol);

        for(CheckBox box : mCbSwitch){
            box.setOnCheckedChangeListener(this);
        }

        mIetWayTem = (IconEditTextView) findViewById(R.id.iet_way_tem);
        mIetWayVol = (IconEditTextView) findViewById(R.id.iet_way_vol);
        mIetWayTem.getmInput().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b){
                    int way;
                    try {
                        way = Integer.parseInt(mIetWayTem.getInputText());
                    } catch (NumberFormatException e) {
                        way = -1;
                    }
                    if(way > 0 && way < 6){
                        mTemOrder.way = way;
                    }else {
                        Toast.makeText(mContext, getString(R.string.error_input), Toast.LENGTH_SHORT).show();
                        mIetWayTem.setText("");
                    }
                }
            }
        });

        mIetWayVol.getmInput().setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b){
                    int way;
                    try {
                        way = Integer.parseInt(mIetWayVol.getInputText());
                    } catch (NumberFormatException e) {
                        way = -1;
                    }
                    if(way > 0 && way < 9){
                        mVolOrder.way = way;
                    }else {
                        Toast.makeText(mContext, getString(R.string.error_input), Toast.LENGTH_SHORT).show();
                        mIetWayVol.setText("");
                    }
                }
            }
        });
        mBtnMake = (Button) findViewById(R.id.btn_make);
        mBtnSend = (Button) findViewById(R.id.btn_send);
        mBtnMake.setOnClickListener(this);
        mBtnSend.setOnClickListener(this);
        mLlContentOrder = (LinearLayout) findViewById(R.id.ll_order_content);

        mIetUpTem  = (IconEditTextView) findViewById(R.id.iet_up_value_tem);
        mIetDownTem  = (IconEditTextView) findViewById(R.id.iet_down_value_tem);
        mIetUpVol  = (IconEditTextView) findViewById(R.id.iet_up_value_vol);
        mIetDownVol  = (IconEditTextView) findViewById(R.id.iet_down_value_vol);
        mTvSendContent = (TextView) findViewById(R.id.tv_sms_content);
    }

    @Override
    protected void initData() {
        mTemOrder = new Order();
        mVolOrder = new Order();
        mHelper = DbUtil.getCustomerHelper();
        mImm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        Intent intent = getIntent();
        if(intent != null){
            Long id = intent.getLongExtra(UserAddActivity.ID, -1);
            if (id != -1){
                mCustomer = mHelper.query(id);
                if(mCustomer != null){
                    mIetPhone.setVisibility(View.GONE);
                    mTvSendTo.setText(getString(R.string.send_tip1, mCustomer.getName(), mCustomer.getPhoneNum()));
                }
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()){
            case R.id.cb_up_tem:
                mTemOrder.isUp = b;
                if(b){
                    mTvSwitchName[0].setText(getString(R.string.temperature_up_open));
                    mLlInput[0].setVisibility(View.VISIBLE);
                }else {
                    mTvSwitchName[0].setText(getString(R.string.temperature_up_close));
                    mLlInput[0].setVisibility(View.GONE);
                }
                break;
            case R.id.cb_down_tem:
                mTemOrder.isDown = b;
                if(b){
                    mTvSwitchName[1].setText(getString(R.string.temperature_down_open));
                    mLlInput[1].setVisibility(View.VISIBLE);
                }else {
                    mTvSwitchName[1].setText(getString(R.string.temperature_down_close));
                    mLlInput[1].setVisibility(View.GONE);
                }
                break;
            case R.id.cb_up_vol:
                mVolOrder.isUp = b;
                if(b){
                    mTvSwitchName[2].setText(getString(R.string.temperature_up_open));
                    mLlInput[2].setVisibility(View.VISIBLE);
                }else {
                    mTvSwitchName[2].setText(getString(R.string.temperature_up_close));
                    mLlInput[2].setVisibility(View.GONE);
                }
                break;
            case R.id.cb_down_vol:
                mVolOrder.isDown = b;
                if(b){
                    mTvSwitchName[3].setText(getString(R.string.temperature_down_open));
                    mLlInput[3].setVisibility(View.VISIBLE);
                }else {
                    mTvSwitchName[3].setText(getString(R.string.temperature_down_close));
                    mLlInput[3].setVisibility(View.GONE);
                }
                break;
            case R.id.cb_up_relay_tem:
                if(b){
                    mTvSwitchName[4].setText(getString(R.string.temperature_up_open));
                }else {
                    mTvSwitchName[4].setText(getString(R.string.temperature_up_close));
                }
                break;
            case R.id.cb_down_relay_tem:
                if(b){
                    mTvSwitchName[5].setText(getString(R.string.temperature_down_open));
                }else {
                    mTvSwitchName[5].setText(getString(R.string.temperature_down_close));
                }
                break;
            case R.id.cb_up_relay_vol:
                if(b){
                    mTvSwitchName[6].setText(getString(R.string.temperature_up_open));
                }else {
                    mTvSwitchName[6].setText(getString(R.string.temperature_up_close));
                }
                break;
            case R.id.cb_down_relay_vol:
                if(b){
                    mTvSwitchName[7].setText(getString(R.string.temperature_down_open));
                }else {
                    mTvSwitchName[7].setText(getString(R.string.temperature_down_close));
                }
                break;
        }
    }

    @Override
    public void onFocusChange(View view, boolean b) {

    }

    @Override
    public void onClick(View view) {
        mImm.hideSoftInputFromWindow(view.getWindowToken(), 0); //强制隐藏键盘
        switch (view.getId()){
            case R.id.btn_make:
                int way;
                try {
                    way = Integer.parseInt(mIetWayTem.getInputText());
                } catch (NumberFormatException e) {
                    way = -1;
                }
                if(way > 0 && way < 6){
                    mTemOrder.way = way;
                }
                try {
                    way = Integer.parseInt(mIetWayVol.getInputText());
                } catch (NumberFormatException e) {
                    way = -1;
                }
                if(way > 0 && way < 9){
                    mVolOrder.way = way;
                }
                if(mTemOrder.way < 0 && mVolOrder.way < 0){
                    Toast.makeText(mContext, getString(R.string.error_order), Toast.LENGTH_SHORT).show();
                    return;
                }
                if(mTemOrder.way != -1){
                    if(mTemOrder.isUp){
                        String strUp = mIetUpTem.getInputText();
                        if(strUp == null || TextUtils.isEmpty(strUp)){
                            mTemOrder.upValue = DEFAULT_UP_TEM;
                        }else {
                            try {
                                int up = Integer.parseInt(strUp);
                                if(up == 0){
                                    mTemOrder.upValue = "000";
                                }else if(up > 0 && up < 10){
                                    mTemOrder.upValue = "00"+ String.valueOf(up);
                                }else if(up > 9 && up < 100){
                                    mTemOrder.upValue = "0"+ String.valueOf(up);
                                }else{
                                    mTemOrder.upValue = String.valueOf(up);
                                }
                            } catch (NumberFormatException e) {
                                Toast.makeText(mContext, getString(R.string.error_input), Toast.LENGTH_SHORT).show();
                            }
                        }

                    }else {
                        mTemOrder.upValue = OFF;
                    }

                    if(mTemOrder.isDown){
                        String str = mIetDownTem.getInputText();
                        if(str == null || TextUtils.isEmpty(str)){
                            mTemOrder.downValue = DEFAULT_DOWN_TEM;
                        }else {
                            try {
                                int value = Integer.parseInt(str);
                                if(value == -10){
                                    mTemOrder.downValue = DEFAULT_DOWN_TEM;
                                }else if(value > -10 && value < 0){
                                    mTemOrder.downValue = "0"+ String.valueOf(value);
                                }else if(value == 0){
                                    mTemOrder.downValue = "000";
                                }else if(value > 0 && value < 10){
                                    mTemOrder.downValue = "00"+ String.valueOf(value);
                                }else if(value > 9 && value < 100){
                                    mTemOrder.downValue = "0"+ String.valueOf(value);
                                }else{
                                    mTemOrder.downValue = String.valueOf(value);
                                }
                            } catch (NumberFormatException e) {
                                Toast.makeText(mContext, getString(R.string.error_input), Toast.LENGTH_SHORT).show();
                            }
                        }

                    }else {
                        mTemOrder.downValue = OFF;
                    }
                }

                if(mVolOrder.way != -1){
                    if(mVolOrder.isUp){
                        String str = mIetUpVol.getInputText();
                        if(str == null || TextUtils.isEmpty(str)){
                            mVolOrder.upValue = DEFAULT_UP_VOL;
                        }else {
                            try {
                                int value = Integer.parseInt(str);
                                if(value == 0){
                                    mVolOrder.upValue = "000";
                                }else{
                                    mVolOrder.upValue = String.valueOf(value * 100);
                                }
                            } catch (NumberFormatException e) {
                                Toast.makeText(mContext, getString(R.string.error_input), Toast.LENGTH_SHORT).show();
                            }
                        }

                    }else {
                        mVolOrder.upValue = OFF;
                    }

                    if(mVolOrder.isDown){
                        String str = mIetDownVol.getInputText();
                        if(str == null || TextUtils.isEmpty(str)){
                            mVolOrder.downValue = DEFAULT_DOWN_VOL;
                        }else {
                            try {
                                int value = Integer.parseInt(str);
                                if(value == 0){
                                    mVolOrder.downValue = "000";
                                }else{
                                    mVolOrder.downValue = String.valueOf(value*100);
                                }
                            } catch (NumberFormatException e) {
                                Toast.makeText(mContext, getString(R.string.error_input), Toast.LENGTH_SHORT).show();
                            }
                        }

                    }else {
                        mVolOrder.downValue = OFF;
                    }
                }

                if(mTemOrder.way != -1){
                    mOrderContent = String.format(Locale.US, FORMAT_ORDER_TEM, mTemOrder.way,
                            mTemOrder.upValue, mTemOrder.downValue);
                }else {
                    mOrderContent = "";
                }
                if(mVolOrder.way != -1){
                    if(mOrderContent != null && !TextUtils.isEmpty(mOrderContent)){
                        mOrderContent += "\n";
                        mOrderContent += String.format(Locale.US, FORMAT_ORDER_VOL, mVolOrder.way,
                                mVolOrder.upValue, mVolOrder.downValue);
                    }else {
                        mOrderContent = String.format(Locale.US, FORMAT_ORDER_VOL, mVolOrder.way,
                                mVolOrder.upValue, mVolOrder.downValue);
                    }
                }else {
                    mOrderContent += "";
                }

                if(mOrderContent != null && !TextUtils.isEmpty(mOrderContent)){
                    mBtnSend.setVisibility(View.VISIBLE);
                    mLlContentOrder.setVisibility(View.VISIBLE);
                    mTvSendContent.setText(mOrderContent);
                }else {
                    Toast.makeText(mContext, getString(R.string.error_input), Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.btn_send:
                String phoneNum;
                if(mCustomer != null){
                    if(mCustomer.getPhoneNum() == null || TextUtils.isEmpty(mCustomer.getPhoneNum())){
                        Toast.makeText(mContext, getString(R.string.error_phone), Toast.LENGTH_SHORT).show();
                        return;
                    }else {
                        phoneNum = mCustomer.getPhoneNum();
                    }
                }else {
                    mCustomer = new Customer();
                    phoneNum = mIetPhone.getInputText();
                    if(phoneNum == null || TextUtils.isEmpty(phoneNum)){
                        Toast.makeText(mContext, getString(R.string.error_phone), Toast.LENGTH_SHORT).show();
                        return;
                    }else {
                        mCustomer = new Customer();
                        mCustomer.setName(getString(R.string.new_customer));
                        mCustomer.setPhoneNum(phoneNum);
                    }
                }
                mHelper.saveOrUpdate(mCustomer);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    doSendSMSTo(phoneNum, mOrderContent);
                }else {
                    sendSMS(phoneNum, mOrderContent);
                }
                finish();
                break;
        }
    }

    /**
     * 直接调用短信接口发短信，不含发送报告和接受报告
     *
     * @param phoneNumber
     * @param message
     */
    public void sendSMS(String phoneNumber, String message) {
        // 获取短信管理器
        android.telephony.SmsManager smsManager = android.telephony.SmsManager.getDefault();
        // 拆分短信内容（手机短信长度限制）
        List<String> divideContents = smsManager.divideMessage(message);
        for (String text : divideContents) {
            smsManager.sendTextMessage(phoneNumber, null, text, null, null);
        }
    }

    /**
     * 调起系统发短信功能
     * @param phoneNumber
     * @param message
     */
    public void doSendSMSTo(String phoneNumber,String message){
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:"+phoneNumber));
        intent.putExtra("sms_body", message);
        startActivity(intent);
    }
}
