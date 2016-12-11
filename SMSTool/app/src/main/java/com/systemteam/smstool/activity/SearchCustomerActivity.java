package com.systemteam.smstool.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.systemteam.smstool.R;
import com.systemteam.smstool.adapter.UserAdapter;
import com.systemteam.smstool.bean.Customer;
import com.systemteam.smstool.dao.CustomerDao;
import com.systemteam.smstool.provider.db.CustomerHelper;
import com.systemteam.smstool.provider.db.DbUtil;
import com.systemteam.smstool.util.LogTool;
import com.systemteam.smstool.util.Utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 2016/12/11.
 */

public class SearchCustomerActivity extends BaseActivity  implements SearchView.OnQueryTextListener{
    private SearchView mSearchView;
    private InputMethodManager mImm;
    private String queryString;
    private ListView mLvInfo;
    List<Customer> mCustomers;
    UserAdapter mAdpater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_customer);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void initView() {
        mContext = this;
        initToolBar(this, R.string.search_customer);
        mLvInfo = (ListView) findViewById(R.id.lv_list);
    }

    @Override
    protected void initData() {
        mImm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mCustomers = new ArrayList<>();
    }
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {

        getMenuInflater().inflate(R.menu.menu_search, menu);

        mSearchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.menu_search));

        mSearchView.setOnQueryTextListener(this);
        mSearchView.setQueryHint(getString(R.string.search_hint));

        mSearchView.setIconifiedByDefault(false);
        mSearchView.setIconified(false);

        MenuItemCompat.setOnActionExpandListener(menu.findItem(R.id.menu_search), new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                finish();
                return false;
            }
        });

        menu.findItem(R.id.menu_search).expandActionView();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        onQueryTextChange(query);
        hideInputManager();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if(mCustomers == null){
            mCustomers = new ArrayList<>();
        }
        mCustomers.clear();
        mAdpater = new UserAdapter(mContext, mCustomers);
        if (newText.equals(queryString)) {
            return true;
        }
        queryString = newText;
        if (!queryString.trim().equals("")) {
            CustomerHelper mHelper = DbUtil.getCustomerHelper();
            Long keyPhone;
            try {
                keyPhone = Long.parseLong(queryString);
            } catch (NumberFormatException e) {
                keyPhone = -1L;
            }
            if(keyPhone > 0){
                mCustomers = mHelper.queryBuilder()
                        .where(CustomerDao.Properties.PhoneNum.like("%"+queryString+"%"))
                        .list();
            }else {
                mCustomers = mHelper.queryBuilder()
                        .where(CustomerDao.Properties.Name.like("%"+queryString+"%"))
                        .list();
            }
            if(mCustomers != null && mCustomers.size() > 0){
                mAdpater = new UserAdapter(mContext, mCustomers);
                mLvInfo.setAdapter(mAdpater);
                mLvInfo.setDivider(getResources().getDrawable(R.drawable.xml_list_divider));
                mLvInfo.setDividerHeight(1);
                mAdpater.notifyDataSetChanged();
            }else {
                Toast.makeText(mContext, getString(R.string.search_null), Toast.LENGTH_SHORT).show();
            }
        } else {
            mCustomers.clear();
            mAdpater.notifyDataSetChanged();
        }
        return true;
    }

    public void hideInputManager() {
        if (mSearchView != null) {
            if (mImm != null) {
                mImm.hideSoftInputFromWindow(mSearchView.getWindowToken(), 0);
            }
            mSearchView.clearFocus();

//            SearchHistory.getInstance(this).addSearchString(queryString);
        }
    }
}
