package com.lw.lemonweather.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

import com.lw.lemonweather.plugin.TimerService;

/**
 * @author zhanghao
 */
public class WightProvider extends AppWidgetProvider {
    //每当组件从屏幕上移除
    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // TODO Auto-generated method stub
        super.onDeleted(context, appWidgetIds);
    }

    //当最后一个该Widget删除是调用该方法，注意是最后一个
    @Override
    public void onDisabled(Context context) {
        context.stopService(new Intent(context, TimerService.class));
        super.onDisabled(context);
    }


    // 当该Widget第一次添加到桌面是调用该方法，可添加多次但只第一次调用
    @Override
    public void onEnabled(Context context) {
        context.startService(new Intent(context,TimerService.class));
        super.onEnabled(context);
    }

    // 每接收一次广播消息就调用一次
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }

    //每次更新都调用一次该方法
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }
}
