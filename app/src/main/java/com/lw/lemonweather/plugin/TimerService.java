package com.lw.lemonweather.plugin;

import static com.lw.lemonweather.utils.Constant.API_KEY;

import android.annotation.SuppressLint;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.lw.lemonweather.R;
import com.lw.lemonweather.utils.ToastUtils;
import com.lw.lemonweather.widget.WightProvider;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@RequiresApi(api = Build.VERSION_CODES.M)
public class TimerService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initLemonWeather();
        updateWeather();
    }

    /**
     * 初始化天气信息
     */
    private void initLemonWeather() {

    }

    /**
     * 设置显示视图
     */
    private void updateWeather() {
        @SuppressLint("RemoteViewLayout") RemoteViews rv = new RemoteViews(getPackageName(),R.layout.lemonplugin_view);
        AppWidgetManager manager = AppWidgetManager.getInstance(getApplicationContext());
        ComponentName cn = new ComponentName(getApplicationContext(), WightProvider.class);
        manager.updateAppWidget(cn, rv);
    }
}
