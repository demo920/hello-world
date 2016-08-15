package com.teemo.ww.ddpay.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by admin on 2016/8/8.
 */

public class ToastUtils {

    private static Toast toast;

    public static void show(Context context,String s){
        if(!checkParams(new Object[]{context,s})) {

            throw new NullPointerException("params can\'t be null ,please check the params");
        } else {

            if(toast==null) {
                toast = Toast.makeText(context, s, Toast.LENGTH_SHORT);
                toast.show();
            }else{
                toast.setText(s);
                toast.show();
            }
        }
    }

    private static boolean checkParams(Object... objects) {
        boolean check = true;
        Object[] var2 = objects;
        int var3 = objects.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            Object o = var2[var4];
            if(o == null) {
                check = false;
                return check;
            }
        }

        return check;
    }
}
