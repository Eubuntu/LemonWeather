package com.lw.lemonweather.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * 消息提示工具类
 */
public class ToastUtils {
    public static void showLongToast(Context context, CharSequence lw) {
        Toast.makeText(context.getApplicationContext(), lw, Toast.LENGTH_LONG).show();
    }

    public static void showShortToast(Context context, CharSequence lw) {
        Toast.makeText(context.getApplicationContext(), lw, Toast.LENGTH_SHORT).show();
    }
}
