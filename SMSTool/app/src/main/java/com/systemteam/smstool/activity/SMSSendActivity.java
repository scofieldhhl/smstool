package com.systemteam.smstool.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.systemteam.smstool.R;
import com.systemteam.smstool.bean.Customer;
import com.systemteam.smstool.bean.Order;
import com.systemteam.smstool.provider.db.CustomerHelper;
import com.systemteam.smstool.provider.db.DbUtil;
import com.systemteam.smstool.view.IconEditTextView;

public class SMSSendActivity extends BaseActivity implements CompoundButton.OnCheckedChangeListener,
        View.OnFocusChangeListener, View.OnClickListener{
    IconEditTextView mIetPhone, mIetWayTem, mIetWayVol;
    TextView mTvSendTo;
    Button mBtnMake, mBtnSend;
    Customer mCustomer;
    CustomerHelper mHelper;
    InputMethodManager mImm;
    CheckBox[] mCbSwitch;
    LinearLayout[] mLlInput;
    TextView[] mTvSwitchName;
    Order mTemOrder, mVolOrder;
    LinearLayout mLlContentOrder;

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
                }else {
                    mBtnSend.setVisibility(View.VISIBLE);
                    mLlContentOrder.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.btn_send:
                if(mCustomer != null){
                    if(mCustomer.getPhoneNum() == null || TextUtils.isEmpty(mCustomer.getPhoneNum())){
                        Toast.makeText(mContext, getString(R.string.error_phone), Toast.LENGTH_SHORT).show();
                        return;
                    }
                }else {
                    String strPhone = mIetPhone.getInputText();
                    if(strPhone == null || TextUtils.isEmpty(strPhone)){
                        Toast.makeText(mContext, getString(R.string.error_phone), Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                //TODO 判断是否为新的联系人，yes 插入数据库
                //TODO 发送短信
                finish();
                break;
        }
    }
}
