package com.systemteam.smstool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.systemteam.smstool.R;
import com.systemteam.smstool.bean.SMSInfo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/12/11.
 */

public class SMSAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<SMSInfo> mData;
    private Context mContext;

    /**
     * 构造函数
     */
    public SMSAdapter(Context context, List<SMSInfo> list) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mData = list;
    }

    public void setmData(List<SMSInfo> mData) {
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
        holder.title.setText(mData.get(position).getPerson());
        holder.info.setText(mData.get(position).getAddress());
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date d = new Date(mData.get(position).getDate());
        String strDate = dateFormat.format(d);
        holder.tvRevice.setText(strDate);
        holder.popupMenu.setVisibility(View.GONE);

        /*String strType = "";
        if (intType == 1) {
            strType = "接收";
        } else if (intType == 2) {
            strType = "发送";
        } else {
            strType = "null";
        }*/

        return convertView;
    }

    /*private void setOnPopupMenuListener(ViewHolder itemHolder, final int position) {

        itemHolder.popupMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final PopupMenu menu = new PopupMenu(mContext, v);
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        final SMSInfo customer = mData.get(position);
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
                                new AlertDialog.Builder(mContext)
                                        .setTitle(mContext.getString(R.string.pop_del))
                                        .setTitle(mContext.getString(R.string.pop_del_msg))
                                        .setPositiveButton(mContext.getString(R.string.ok), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                CustomerHelper mHelper = DbUtil.getCustomerHelper();
                                                mHelper.delete(customer);
                                                mData.remove(customer);
                                                notifyDataSetChanged();
                                            }
                                        })
                                        .setNegativeButton(mContext.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                            }
                                        }).create().show();
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
    }*/

}


