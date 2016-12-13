package com.systemteam.smstool.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.systemteam.smstool.R;
import com.systemteam.smstool.activity.SMSSendActivity;
import com.systemteam.smstool.activity.UserAddActivity;
import com.systemteam.smstool.bean.Customer;
import com.systemteam.smstool.provider.db.CustomerHelper;
import com.systemteam.smstool.provider.db.DbUtil;
import com.systemteam.smstool.util.Utils;

import java.util.List;

/**
 * Created by Administrator on 2016/12/11.
 */

public class UserAdapter extends BaseAdapter {
    private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局
    private List<Customer> mData;
    private Context mContext;
    private boolean mIsAction = true;

    public void setmIsAction(boolean mIsAction) {
        this.mIsAction = mIsAction;
    }

    /**
     * 构造函数
     */
    public UserAdapter(Context context, List<Customer> list) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = list;
    }

    public void setmData(List<Customer> mData) {
        this.mData = mData;
    }

    @Override
    public int getCount() {
        return mData.size();//返回数组的长度
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    /**
     * 书中详细解释该方法
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_customer_list, null);
            holder = new ViewHolder();
            /**得到各个控件的对象*/
            holder.title = (TextView) convertView.findViewById(R.id.tv_title);
            holder.tvSend = (TextView) convertView.findViewById(R.id.tv_send);
            holder.tvRevice = (TextView) convertView.findViewById(R.id.tv_recive);
            holder.info = (TextView) convertView.findViewById(R.id.tv_info);
            holder.popupMenu = (ImageView) convertView.findViewById(R.id.popup_menu);
            convertView.setTag(holder);//绑定ViewHolder对象
        } else {
            holder = (ViewHolder) convertView.getTag();//取出ViewHolder对象
        }
        /**设置TextView显示的内容，即我们存放在动态数组中的数据*/
        holder.title.setText(mData.get(position).getName());
        holder.info.setText(mData.get(position).getPhoneNum());
        holder.tvSend.setText(mData.get(position).getSend());
        holder.tvRevice.setText(mData.get(position).getRecive());
        if(mIsAction){
            holder.popupMenu.setVisibility(View.VISIBLE);
        }else {
            holder.popupMenu.setVisibility(View.GONE);
        }
        setOnPopupMenuListener(holder, position);
        return convertView;
    }

    private void setOnPopupMenuListener(ViewHolder itemHolder, final int position) {

        itemHolder.popupMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final PopupMenu menu = new PopupMenu(mContext, v);
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Customer customer = mData.get(position);
                        switch (item.getItemId()) {
                            case R.id.popup_detail:
                                Utils.showDetail(mContext, customer);
                                break;
                            case R.id.popup_update:
                                Intent intent = new Intent(mContext, UserAddActivity.class);
                                intent.putExtra(UserAddActivity.ID, customer.getId());
                                mContext.startActivity(intent);
                                break;
                            case R.id.popup_del:
                                CustomerHelper mHelper = DbUtil.getCustomerHelper();
                                mHelper.delete(customer);
                                mData.remove(customer);
                                notifyDataSetChanged();
                                break;
                            case R.id.popup_send:
                                Intent intentSend = new Intent(mContext, SMSSendActivity.class);
                                intentSend.putExtra(UserAddActivity.ID, customer.getId());
                                mContext.startActivity(intentSend);
                                break;

                        }
                        return false;
                    }
                });
                menu.inflate(R.menu.popup_customer);
                menu.show();
            }
        });
    }

}

/**
 * 存放控件
 */
class ViewHolder {
    public TextView title, tvSend, tvRevice;
    public TextView info;
    public ImageView popupMenu;
}
