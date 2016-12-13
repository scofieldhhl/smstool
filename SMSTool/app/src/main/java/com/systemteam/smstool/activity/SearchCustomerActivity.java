package com.systemteam.smstool.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import android.widget.Toast;

import com.systemteam.smstool.R;
import com.systemteam.smstool.adapter.UserAdapter;
import com.systemteam.smstool.bean.Customer;
import com.systemteam.smstool.dao.CustomerDao;
import com.systemteam.smstool.provider.db.CustomerHelper;
import com.systemteam.smstool.provider.db.DbUtil;
import com.systemteam.smstool.util.LogTool;

import java.util.ArrayList;
import java.util.List;

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
    private int mIndexSearch = 0;
    CustomerHelper mHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_customer);
        initView();
        initData();
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
        mHelper = DbUtil.getCustomerHelper();
        mCustomers = new ArrayList<>();
        mCustomers = mHelper.queryAll();
        LogTool.d("size:" + mCustomers.size());
        mAdpater = new UserAdapter(mContext, mCustomers);
        mLvInfo.setAdapter(mAdpater);
        mAdpater.notifyDataSetChanged();
    }
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {

        getMenuInflater().inflate(R.menu.menu_search, menu);

        mSearchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.menu_search));

        mSearchView.setOnQueryTextListener(this);
        mSearchView.setQueryHint(getString(R.string.search_hint_name));

        mSearchView.setIconifiedByDefault(false);
        mSearchView.setIconified(false);
        mSearchView.clearFocus();
        hideInputManager();

        MenuItemCompat.setOnActionExpandListener(menu.findItem(R.id.menu_search),
                new MenuItemCompat.OnActionExpandListener() {
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
        LogTool.d("query:" + query);
//        onQueryTextChange(query);
        search(query);
        hideInputManager();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        return true;
    }

    private void search(String newText){
        LogTool.d("newText:" + newText);
        if(mCustomers == null){
            mCustomers = new ArrayList<>();
        }
        if (newText.equals(queryString)) {
            return;
        }
        queryString = newText;
        if (!queryString.trim().equals("")) {

            switch (mIndexSearch) {
                case 0:
                    mCustomers = mHelper.queryBuilder()
                            .where(CustomerDao.Properties.Name.like("%" + queryString + "%"))
                            .list();
                    break;
                case 1:
                    mCustomers = mHelper.queryBuilder()
                            .where(CustomerDao.Properties.PhoneNum.like("%" + queryString + "%"))
                            .list();
                    break;
                case 2:
                    mCustomers = mHelper.queryBuilder()
                            .where(CustomerDao.Properties.CompanyAddress.like("%" + queryString + "%"))
                            .list();
                    break;
            }
            if(mCustomers != null && mCustomers.size() > 0){
                mAdpater.setmData(mCustomers);
                mAdpater.notifyDataSetChanged();
            }else {
                mCustomers.clear();
                mAdpater.setmData(mCustomers);
                mAdpater.notifyDataSetChanged();
                Toast.makeText(mContext, getString(R.string.search_null), Toast.LENGTH_SHORT).show();
            }
        } else {
//            mCustomers.clear();
//            mAdpater.notifyDataSetChanged();
        }
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

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_name:
                mIndexSearch = 0;
                mSearchView.setQueryHint(getString(R.string.search_hint_name));
                return true;
            case R.id.menu_phone:
                mIndexSearch = 1;
                mSearchView.setQueryHint(getString(R.string.search_hint_phone));
                return true;
            case R.id.menu_compay:
                mIndexSearch = 2;
                mSearchView.setQueryHint(getString(R.string.search_hint_compay));
                return true;
            default:
                mIndexSearch = 0;
                mSearchView.setQueryHint(getString(R.string.search_hint_name));
                return false;
        }
    }
}
