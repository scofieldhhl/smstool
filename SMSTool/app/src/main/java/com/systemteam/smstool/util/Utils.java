package com.systemteam.smstool.util;

import android.app.AlertDialog;
import android.content.Context;

import com.systemteam.smstool.R;
import com.systemteam.smstool.bean.Customer;

import java.text.SimpleDateFormat;

/**
 * Created by Administrator on 2016/12/11.
 */

public class Utils {
    
    public static void showDetail(Context context, Customer customer){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        StringBuilder sbInfo = new StringBuilder();
        sbInfo.append(context.getString(R.string.name))
                .append(customer.getName())
                .append("\n")
                .append(context.getString(R.string.sex))
                .append(customer.getSex() == true ? context.getString(R.string.sex_man) : context.getString(R.string.sex_woman))
                .append("\n")
                .append(context.getString(R.string.phoneNum))
                .append(customer.getPhoneNum())
                .append("\n")
                .append(context.getString(R.string.homeAddress))
                .append(customer.getHomeAddress())
                .append("\n")
                .append(context.getString(R.string.companyAddress))
                .append(customer.getCompanyAddress())
                .append("\n")
                .append(context.getString(R.string.register))
                .append(customer.getRegisterTime() == null ? "" : sdf.format(customer.getRegisterTime()))
                .append("\n")
                .append(context.getString(R.string.date_add))
                .append(customer.getTime() == null ? "" : sdf.format(customer.getTime()))
                .append("\n")
                .append(context.getString(R.string.remarks))
                .append(customer.getRemarks());
        new AlertDialog.Builder(context)
                .setTitle(context.getString(R.string.customer))
                .setMessage(sbInfo.toString())
                .setPositiveButton(context.getString(R.string.ok), null)
                .show();
    }
}
