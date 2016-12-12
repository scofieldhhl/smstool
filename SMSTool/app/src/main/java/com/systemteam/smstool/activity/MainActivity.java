package com.systemteam.smstool.activity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.systemteam.smstool.R;
import com.systemteam.smstool.adapter.UserAdapter;
import com.systemteam.smstool.bean.Customer;
import com.systemteam.smstool.provider.db.CustomerHelper;
import com.systemteam.smstool.provider.db.DbUtil;
import com.systemteam.smstool.util.LogTool;
import com.systemteam.smstool.util.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private SmsObserver smsObserver;
    private ListView mLvInfo;
    List<Customer> mCustomers;
    UserAdapter mAdpater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolBar(this, R.string.app_name);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        smsObserver = new SmsObserver(this, smsHandler);
        getContentResolver().registerContentObserver(SMS_INBOX, true,
                smsObserver);
        checkSDK();
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
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                startActivity(new Intent(MainActivity.this, UserAddActivity.class));
            }
        });
        mLvInfo = (ListView) findViewById(R.id.lv_list);
        mLvInfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if(mCustomers != null && mCustomers.size() > 0){
                    Customer customer = mCustomers.get(position);
                    Utils.showDetail(mContext, customer);
                }
            }
        });

        mLvInfo.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                Customer customer = mCustomers.get(position);
                Intent intent = new Intent(MainActivity.this, UserAddActivity.class);
                intent.putExtra(UserAddActivity.ID, customer.getId());
                startActivity(intent);
                return true;
            }
        });
    }

    @Override
    protected void initData() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String dateString = formatter.format(new Date());
        LogTool.d("dateString" + Long.parseLong(dateString));
        if(Long.parseLong(dateString) > 20161217154658L){
            LogTool.e("time out");
            finish();
        }
        CustomerHelper mHelper = DbUtil.getCustomerHelper();
        mCustomers = mHelper.queryAll();
        if(mCustomers != null && mCustomers.size() > 0){
            String ITEM = "%s ( %s )";
            String[] arrContent = new String[mCustomers.size()];
            for(int i = 0; i < mCustomers.size(); i++){
                Customer customer = mCustomers.get(i);
                arrContent[i] = String.format(Locale.US, ITEM, customer.getName(), customer.getPhoneNum());
            }
            mAdpater = new UserAdapter(mContext, mCustomers);
            mLvInfo.setAdapter(mAdpater);
            mLvInfo.setDivider(getResources().getDrawable(R.drawable.xml_list_divider));
            mLvInfo.setDividerHeight(1);
            mAdpater.notifyDataSetChanged();
        }else {
            LogTool.e("no records!");
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            startActivity(new Intent(MainActivity.this, UserAddActivity.class));
        } else if (id == R.id.nav_gallery) {
            startActivity(new Intent(MainActivity.this, SearchCustomerActivity.class));
        } else if (id == R.id.nav_slideshow) {
            startActivity(new Intent(MainActivity.this, SMS15Activity.class));
        } else if (id == R.id.nav_manage) {
            startActivity(new Intent(MainActivity.this, SMSSendActivity.class));
        } /*else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private Uri SMS_INBOX = Uri.parse("content://sms/");
    public void getSmsFromPhone() {
        ContentResolver cr = getContentResolver();
        String[] projection = new String[] { "body" };//"_id", "address", "person",, "date", "type
        String where = " address = '1066321332' AND date >  "
                + (System.currentTimeMillis() - 10 * 60 * 1000);
        Cursor cur = cr.query(SMS_INBOX, projection, where, null, "date desc");
        if (null == cur){
            LogTool.e("cur == null");
            return;
        }
        if (cur.moveToNext()) {
            String number = cur.getString(cur.getColumnIndex("address"));//手机号
            String name = cur.getString(cur.getColumnIndex("person"));//联系人姓名列表
            String body = cur.getString(cur.getColumnIndex("body"));

            LogTool.d("number:" + number);
            //这里我是要获取自己短信服务号码中的验证码~~
            Pattern pattern = Pattern.compile(" [a-zA-Z0-9]{10}");
            Matcher matcher = pattern.matcher(body);
            if (matcher.find()) {
                String res = matcher.group().substring(1, 11);
//                mobileText.setText(res);
            }
        }else {
            LogTool.e("cur.moveToNext() == false");
        }
    }

    class SmsObserver extends ContentObserver {

        public SmsObserver(Context context, Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            //每当有新短信到来时，使用我们获取短消息的方法
            getSmsFromPhone();
        }
    }

    public Handler smsHandler = new Handler() {
        //这里可以进行回调的操作
        //TODO

    };

    /*
     * 异步任务下载
     * */
    public class InitTask extends AsyncTask<String, Void, Boolean> {
        @Override
        //在界面上显示进度条
        protected void onPreExecute() {
        };
        protected Boolean doInBackground(String... params) {  //三个点，代表可变参数
            getSmsFromPhone();
            return false;
        }
        //主要是更新UI
        @Override
        protected void onPostExecute(Boolean isDownloaded) {
            super.onPostExecute(isDownloaded);
            //更新UI
            Toast.makeText(MainActivity.this, "success!", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkSDK() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            int Permission1 = checkSelfPermission(Manifest.permission.READ_SMS);
            int Permission2 = checkSelfPermission(Manifest.permission.SEND_SMS);
            int Permission3 = checkSelfPermission(Manifest.permission.RECEIVE_SMS);
            int Permission4 = checkSelfPermission(Manifest.permission.RECEIVE_MMS);
            int Permission5 = checkSelfPermission(Manifest.permission.READ_CONTACTS);


            List<String> permissions = new ArrayList<String>();
            if (Permission1 != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.READ_SMS);
            }
            if (Permission2 != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.SEND_SMS);
            }
            if (Permission3 != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.RECEIVE_SMS);
            }
            if (Permission4 != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.RECEIVE_MMS);
            }
            if (Permission5 != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.READ_CONTACTS);
            }
            if (!permissions.isEmpty()) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), REQUEST_CODE_SOME_FEATURES_PERMISSIONS);
            }

        }
    }

    private final int REQUEST_CODE_SOME_FEATURES_PERMISSIONS = 101;
}
