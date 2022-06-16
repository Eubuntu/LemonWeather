package com.lw.lemonweather.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.ImageView;
import android.widget.RemoteViews;

import androidx.annotation.RequiresApi;

import com.lw.lemonweather.R;
import com.lw.lemonweather.utils.DateUtils;
import com.lw.lemonweather.utils.WeatherUtil;
import com.lw.mvplibrary.bean.WeatherInfo;

import org.litepal.LitePal;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author zhanghao
 */
@RequiresApi(api = Build.VERSION_CODES.M)
public class WightProvider extends AppWidgetProvider {
    private WeatherInfo weatherInfoLast;

    //每当组件从屏幕上移除
    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // TODO Auto-generated method stub
        super.onDeleted(context, appWidgetIds);
    }

    //当最后一个该Widget删除是调用该方法，注意是最后一个
    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
    }

    // 当该Widget第一次添加到桌面是调用该方法，可添加多次但只第一次调用
    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
    }

    // 每接收一次广播消息就调用一次
    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        updateWidgetView(context, appWidgetManager);
    }

    //每次更新都调用一次该方法
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        updateWidgetView(context,appWidgetManager);
    }

    private void updateWidgetView(Context context, AppWidgetManager appWidgetManager) {
        weatherInfoLast = LitePal.findLast(WeatherInfo.class);
        //定义RemoteViews实例
        RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.lemonplugin_view);
        rv.setTextViewText(R.id.textView_widget_time, new SimpleDateFormat("hh:mm").format(System.currentTimeMillis()));
        rv.setTextViewText(R.id.textView_widget_date, DateUtils.getNowDateStr().substring(5, 10) + " " + DateUtils.getWeekOfDate(new Date()));
        if (!weatherInfoLast.equals("") && weatherInfoLast != null) {
            rv.setTextViewText(R.id.textView_widget_location, weatherInfoLast.getCityName());
            rv.setTextViewText(R.id.textView_widget_avgTemp, weatherInfoLast.getTemp() + "℃");
            rv.setTextViewText(R.id.textView_widget_lowHighTemp, weatherInfoLast.getTempMin() + "/" + weatherInfoLast.getTempMax() + "℃");
            //设置天气状态图标
            //WeatherUtil.changeIcon(R.id.imageView_widget_weather_ic1, weatherInfoList.get(0).getWeatherInfoStateCode());
        }
        //调用组件管理器修改小组件，使其生效
        ComponentName componentName = new ComponentName(context, WightProvider.class);
        appWidgetManager.updateAppWidget(componentName, rv);
    }

}
