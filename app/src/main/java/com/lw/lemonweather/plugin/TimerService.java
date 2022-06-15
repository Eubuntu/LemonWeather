package com.lw.lemonweather.plugin;

import android.annotation.SuppressLint;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Build;
import android.os.IBinder;
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

@RequiresApi(api = Build.VERSION_CODES.M)
public class TimerService extends Service {
    /**
     * 定位器
     */
    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();

    //区/县  改为全局的静态变量,方便更换城市之后也能进行下拉刷新
    private String district = null;
    //市 国控站点数据  用于请求空气质量
    private String city;

    private Context context;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        updateWeather();
    }

    /**
     * 初始化天气信息
     */
    private void initLemonWeather() {
        if (isOpenLocationServiceEnable()) {
            startLocation();
        } else {
            ToastUtils.showShortToast(context, "(((φ(◎ロ◎;)φ)))，你好像忘记打开定位功能了");
        }
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

    /**
     * 定位
     */
    private void startLocation() {
        LocationClient.setAgreePrivacy(true);
        //声明LocationClient类
        try {
            mLocationClient = new LocationClient(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //注册监听函数
        if (mLocationClient != null) {
            mLocationClient.registerLocationListener(myListener);
        }
        LocationClientOption option = new LocationClientOption();
        //如果开发者需要获得当前点的地址信息，此处必须为true
        option.setIsNeedAddress(true);
        //可选，设置是否需要最新版本的地址信息。默认不需要，即参数为false
        option.setNeedNewVersionRgc(true);
        //mLocationClient为第二步初始化过的LocationClient对象
        //需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
        mLocationClient.setLocOption(option);
        //启动定位
        mLocationClient.start();
    }

    /**
     * 定位结果返回
     */
    private class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            //获取区/县
            district = location.getDistrict();
            //获取市
            city = location.getCity();
            //未获取到定位信息，请重新定位
            if (district == null) {
                ToastUtils.showShortToast(context, "未获取到定位信息，请重新定位");
                //tvCity.setText("重新定位");
                //可点击
                //tvCity.setEnabled(true);
                //页面处理
            } else {
                //不可点击
                //tvCity.setEnabled(false);
                //在数据请求之前放在加载等待弹窗，返回结果后关闭弹窗
                //showLoadingDialog();
                //V7版本中需要先获取到城市ID ,在结果返回值中再进行下一步的数据查询
                //定位返回时
                //mPresent.newSearchCity(district);
                //下拉刷新
                //refresh.setOnRefreshListener(refreshLayout -> {
                    //V7版本中需要先获取到城市ID ,在结果返回值中再进行下一步的数据查询
                    //mPresent.newSearchCity(district);
                //});
            }
        }
    }

    /**
     * 手机是否开启位置服务，如果没有开启那么App将不能使用定位功能
     */
    private boolean isOpenLocationServiceEnable() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }
        return false;
    }
}
