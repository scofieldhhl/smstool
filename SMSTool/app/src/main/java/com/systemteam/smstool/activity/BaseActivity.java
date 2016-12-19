package com.systemteam.smstool.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;

import com.systemteam.smstool.R;
import com.systemteam.smstool.provider.db.CustomerHelper;
import com.systemteam.smstool.view.ProgressDialogHelper;

import java.text.SimpleDateFormat;

/**
 * Created by chenjiang on 2016/5/23.
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected String DATE_FORMAT = "%d/%d/%d";
    protected Toolbar mToolbar;
    protected Context mContext;
    protected ProgressDialogHelper mProgressHelper;
    CustomerHelper mHelper;
    SimpleDateFormat sdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        mContext = BaseActivity.this;
        mProgressHelper = new ProgressDialogHelper(this);
    }

    protected void initToolBar(Activity act, int titleId) {
        mToolbar = (Toolbar) act.findViewById(R.id.toolbar);
        mToolbar.getVisibility();
        if (titleId == 0) {
            mToolbar.setTitle("");
        } else {
            int titleColor = Color.WHITE;
            mToolbar.setTitleTextColor(titleColor);
            mToolbar.setTitle(titleId);
        }


        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            mToolbar.setNavigationIcon(R.drawable.ic_back_normal);
        }
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    protected void initToolBar(Activity act, String title) {
        mToolbar = (Toolbar) act.findViewById(R.id.toolbar);
        mToolbar.getVisibility();
        int titleColor = Color.WHITE;
        mToolbar.setTitleTextColor(titleColor);
        mToolbar.setTitle(title);
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            mToolbar.setNavigationIcon(R.drawable.ic_back_normal);
        }
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


	/**
	 * 初始化
	 */
	protected abstract void initView();

	/**
	 * 加载布局文件
	 */
	protected abstract void initData();


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    protected void openActivity(Class<?> pClass) {
        openActivity(pClass, null);
    }

    protected void openActivity(Class<?> pClass, Bundle pBundle) {
        Intent intent = new Intent(this, pClass);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in,
                android.R.anim.fade_out);
    }

    protected void openActivity(String pAction) {
        openActivity(pAction, null);
    }

    protected void openActivity(String pAction, Bundle pBundle) {
        Intent intent = new Intent(pAction);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in,
                android.R.anim.fade_out);
    }

    /**
     * Activity关闭和启动动画
     */
    public void finish() {
        super.finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    /**
     * 提示框
     *
     */
    /*public void showDialog(String title, String Content) {
        if (mProgressHelper != null) {
            mProgressHelper.dismissProgressDialog();
        }
        if (isFinishing()) return;
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
//        builder.setIcon(R.drawable.ic_launcher);
        builder.setTitle(title);
        builder.setMessage(Content);
        builder.setPositiveButton(getString(R.string.lbOK), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }*/

    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
        return res;
    }


}

