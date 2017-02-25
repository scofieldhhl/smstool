package com.systemteam.smstool.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.systemteam.smstool.R;
import com.systemteam.smstool.adapter.SMSAdapter;
import com.systemteam.smstool.bean.Customer;
import com.systemteam.smstool.bean.SMSInfo;
import com.systemteam.smstool.provider.db.DbUtil;
import com.systemteam.smstool.util.LogTool;
import com.systemteam.smstool.view.ProgressDialogHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.systemteam.smstool.R.id.btn_date;
import static com.systemteam.smstool.R.id.ibtn_next;
import static com.systemteam.smstool.R.id.ibtn_pre;

public class SMSDetailActivity extends BaseActivity implements View.OnClickListener{
    public static final int MSG_QUERYDATA = 0x123;
    public static final int ID_DIALOG_DETAIL = 998;
    final String SMS_URI_ALL = "content://sms/";
    final String SMS_URI_INBOX = "content://sms/inbox";
    final String SMS_URI_SEND = "content://sms/sent";
    private Customer mCustomer;
    private Button mbtnDate;
    private ImageButton mIbtnPre, mIbtnNext;
    private ListView mLvInfo;
    SMSAdapter mAdpater;
    long ms_frm, ms_to;
    String columns[] = new String[]{"address", "type", "date", "body", "COUNT('body')"};

    List<SMSInfo> mData = new ArrayList<>();
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MSG_QUERYDATA:
                new QueryData().execute();
                break;
            }
        }
    };
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
        mProgressHelper = new ProgressDialogHelper(this);
        mbtnDate = (Button) findViewById(btn_date);
        mIbtnPre = (ImageButton) findViewById(ibtn_pre);
        mIbtnNext = (ImageButton) findViewById(ibtn_next);
        mbtnDate.setOnClickListener(this);
        mIbtnPre.setOnClickListener(this);
        mIbtnNext.setOnClickListener(this);
        mLvInfo = (ListView)findViewById(R.id.lv_list);
    }

    int year, yearf, yeart, month, monthf, montht, day, dayf, dayt;
    @Override
    protected void initData() {
        mHelper = DbUtil.getCustomerHelper();
        sdf = new SimpleDateFormat("yyyy/MM/dd");

        Intent intent = getIntent();
        mCustomer = (Customer) intent.getSerializableExtra("customer");
        if(mCustomer == null){
            Toast.makeText(mContext, mContext.getString(R.string.error_input), Toast.LENGTH_SHORT).show();
        }else {
            initToolBar(this, mCustomer.getName()+"("+mCustomer.getPhoneNum()+")");
        }
        mAdpater = new SMSAdapter(mContext, mData);
        mLvInfo.setAdapter(mAdpater);

        Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        yearf = yeart = year;
        monthf = montht = month;
        dayf = dayt = day;
        frm_lstnr.onDateSet(new DatePicker(this), year, month, day);//初始化时间
    }

    public DatePickerDialog.OnDateSetListener frm_lstnr = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int yeard, int monthd, int dayd) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");
            yearf = yeard;
            monthf = monthd;
            dayf = dayd;
            ms_frm = new Date(yearf - 1900, monthf, dayf, 00, 00).getTime();
            Date d = new Date(ms_frm);
            String strDate = dateFormat.format(d);
            LogTool.d("form:" + strDate);
            mbtnDate.setText(String.format(Locale.US, DATE_FORMAT, yearf, (monthf + 1), dayf));
            yeart = yeard;
            montht = monthd;
            dayt = dayd;
            ms_to = new Date(yeart - 1900, montht, dayt, 23, 59).getTime();
            d = new Date(ms_to);
            strDate = dateFormat.format(d);
            long now = new Date().getTime();
            if(now > ms_frm && now < ms_to){
                mIbtnNext.setVisibility(View.INVISIBLE);
            }else {
                mIbtnNext.setVisibility(View.VISIBLE);
            }
            LogTool.d("to:" + strDate);
            mHandler.sendEmptyMessage(MSG_QUERYDATA);
        }
    };

    @Override
    public Dialog onCreateDialog(int id) {
        switch (id) {
            case ID_DIALOG_DETAIL:
                return new DatePickerDialog(this, 0, frm_lstnr, yearf, monthf, dayf) {
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

    /**
     * 获取数据
     */
    public void generate(Customer customer) {
        Uri uri = Uri.parse(SMS_URI_ALL);
        String[] projection = new String[] { "_id", "address", "person", "body", "date", "type" };
        Cursor cur = null;
        try {
            cur = getContentResolver().query(uri, projection,
                    "address=? AND date BETWEEN " + ms_frm + " AND " + ms_to,
                    new String[]{customer.getPhoneNum()},
                    "address");
            /*Cursor cur = getContentResolver().query(Uri.parse("content://sms/inbox"), columns,
                    "address=? AND date BETWEEN " + ms_frm + " AND " + ms_to + ") GROUP BY (address),(type",
                    new String[]{customer.getPhoneNum()}, "address");*/
            if (cur.moveToFirst()) {
                int index_Body = cur.getColumnIndex("body");
                int index_Date = cur.getColumnIndex("date");
                int index_Type = cur.getColumnIndex("type");

                do {
                    SMSInfo info = new SMSInfo();
                    info.setAddress(customer.getPhoneNum());
                    info.setPerson(customer.getName());
                    info.setBody(cur.getString(index_Body));
                    info.setDate(cur.getLong(index_Date));
                    info.setType(cur.getInt(index_Type));
                    mData.add(info);
                } while (cur.moveToNext());
            } else {
                cur  = getContentResolver().query(uri, projection,
                        "address=? AND date BETWEEN " + ms_frm + " AND " + ms_to,
                        new String[]{ "+86" + customer.getPhoneNum()},
                        "address");
                if (cur.moveToFirst()) {
                    int index_Body = cur.getColumnIndex("body");
                    int index_Date = cur.getColumnIndex("date");
                    int index_Type = cur.getColumnIndex("type");

                    do {
                        SMSInfo info = new SMSInfo();
                        info.setAddress(customer.getPhoneNum());
                        info.setPerson(customer.getName());
                        info.setBody(cur.getString(index_Body));
                        info.setDate(cur.getLong(index_Date));
                        info.setType(cur.getInt(index_Type));
                        mData.add(info);

                    } while (cur.moveToNext());

                    if (!cur.isClosed()) {
                        cur.close();
                        cur = null;
                    }

                }
            } // end if

        } catch (SQLiteException ex) {
            LogTool.d(ex.getMessage());
        }finally {
            if (cur != null) {
                cur.close();
                cur = null;
            }
        }
    }

    @Override
    public void onClick(View view) {
        String strDate = String.valueOf(mbtnDate.getText());
        switch (view.getId()){
            case R.id.btn_date:
                showDialog(ID_DIALOG_DETAIL);
                break;
            case R.id.ibtn_pre:
                if(strDate != null && !TextUtils.isEmpty(strDate)){
                    setPreOrNextDate(strDate, -1);
                }else {
                    Toast.makeText(mContext, mContext.getString(R.string.frm_to_err), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ibtn_next:
                if(strDate != null && !TextUtils.isEmpty(strDate)){
                    setPreOrNextDate(strDate, 1);
                }else {
                    Toast.makeText(mContext, mContext.getString(R.string.frm_to_err), Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    class QueryData extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mData.clear();
            mAdpater.setmData(mData);
            mAdpater.notifyDataSetChanged();
            mProgressHelper.showProgressDialog(getString(R.string.init));
        }

        @Override
        protected String doInBackground(Void... integers) {
            generate(mCustomer);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            mProgressHelper.dismissProgressDialog();
            if(mData != null && mData.size() > 0){
                mAdpater.setmData(mData);
                mAdpater.notifyDataSetChanged();
            }else {
                Toast.makeText(mContext, mContext.getString(R.string.data_null), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setPreOrNextDate(String strDate, int space){
        Calendar c = Calendar.getInstance();//获得一个日历的实例
        Date date = null;
        try{
            date = sdf.parse(strDate);//初始日期
        }catch(Exception e){

        }
        c.setTime(date);//设置日历时间
        int daySet =c.get(Calendar.DATE);
        c.set(Calendar.DATE,daySet + space);
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        yearf = yeart = year;
        monthf = montht = month;
        dayf = dayt = day;
        frm_lstnr.onDateSet(new DatePicker(this), year, month, day);//初始化时间
    }

}
