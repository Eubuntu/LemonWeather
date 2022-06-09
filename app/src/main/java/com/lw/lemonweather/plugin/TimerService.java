package com.lw.lemonweather.plugin;

import android.annotation.SuppressLint;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.IBinder;
import android.widget.RemoteViews;
import androidx.annotation.Nullable;

import com.lw.lemonweather.R;
import com.lw.lemonweather.widget.WightProvider;

public class TimerService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        weater();
    }
    /**
     * 设置显示视图
     */
    private void weater() {
        @SuppressLint("RemoteViewLayout") RemoteViews rv = new RemoteViews(getPackageName(),R.layout.lemonplugin_view);
        AppWidgetManager manager = AppWidgetManager.getInstance(getApplicationContext());
        ComponentName cn = new ComponentName(getApplicationContext(), WightProvider.class);
        manager.updateAppWidget(cn, rv);
    }
}
