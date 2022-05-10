package com.lw.lemonweather;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.lw.lemonweather.bean.TodayResponse;
import com.lw.lemonweather.bean.WeatherForecastResponse;
import com.lw.lemonweather.contract.WeatherContract;
import com.lw.lemonweather.utils.ToastUtils;
import com.lw.mvplibrary.mvp.MvpActivity;
import com.lw.mvplibrary.view.WhiteWindmills;
import com.tbruyelle.rxpermissions2.RxPermissions;

import retrofit2.Response;

public class MainActivity extends MvpActivity<WeatherContract.WeatherPresenter> implements WeatherContract.IWeatherView {

    //权限
    private RxPermissions rxPermissions;
    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();


    private TextView tvInfo;
    private TextView tvTemperature;
    private TextView tvLowHeight;
    private TextView tvCity;
    private TextView tvOldTime;
    private RecyclerView rv;
    private RelativeLayout rlWind;
    private WhiteWindmills wwBig;
    private WhiteWindmills wwSmall;
    private TextView tvWindDirection;
    private TextView tvWindPower;
    private ImageView ivCitySelect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        //权限判断
        rxPermissions = new RxPermissions(this);
        permissionVersion();
        tvInfo = findViewById(R.id.tv_info);
        tvTemperature = findViewById(R.id.tv_temperature);
        tvLowHeight = findViewById(R.id.tv_low_height);
        tvCity = findViewById(R.id.tv_city);
        tvOldTime = findViewById(R.id.tv_old_time);
        rv = findViewById(R.id.rv);
        rlWind = findViewById(R.id.rl_wind);
        wwBig = findViewById(R.id.ww_big);
        wwSmall = findViewById(R.id.ww_small);
        tvWindDirection = findViewById(R.id.tv_wind_direction);
        tvWindPower = findViewById(R.id.tv_wind_power);
        ivCitySelect = findViewById(R.id.iv_city_select);
    }

    //权限判断
    private void permissionVersion() {
        //6.0或6.0以上
        if (Build.VERSION.SDK_INT >= 23) {
            //动态权限申请
            permissionsRequest();
        } else {//6.0以下
            //发现只要权限在AndroidManifest.xml中注册过，均会认为该权限granted  提示一下即可
            ToastUtils.showShortToast(this, "你的版本在Android6.0以下，不需要动态申请权限。");
        }
    }

    //
    private void permissionsRequest() {
        rxPermissions.request(Manifest.permission.ACCESS_FINE_LOCATION).subscribe(granted -> {
            if (granted) {
                startLocation();
            } else {
                ToastUtils.showShortToast(this, "权限未开启");
            }
        });
    }

    private void startLocation() {
        LocationClient.setAgreePrivacy(true);
        try {
            mLocationClient = new LocationClient(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mLocationClient != null) {
            mLocationClient.registerLocationListener(myListener);
            LocationClientOption option = new LocationClientOption();
            option.setIsNeedAddress(true);
            option.setNeedNewVersionRgc(true);
            mLocationClient.setLocOption(option);
            mLocationClient.start();
        }
    }

    @Override
    public void getTodayWeatherResult(Response<TodayResponse> response) {

    }

    @Override
    public void getWeatherForecastResult(Response<WeatherForecastResponse> response) {

    }

    @Override
    public void getDataFailed() {

    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public int getLayoutId() {
        return 0;
    }

    @Override
    protected WeatherContract.WeatherPresenter createPresent() {
        return null;
    }

    /**
     *
     */
    private class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            double latitude = bdLocation.getLatitude();
            double longitude = bdLocation.getLongitude();
            float radius = bdLocation.getRadius();
            String coorType = bdLocation.getCoorType();
            int locType = bdLocation.getLocType();
            String addrStr = bdLocation.getAddrStr();
            String country = bdLocation.getCountry();
            String province = bdLocation.getProvince();
            String city = bdLocation.getCity();
            String district = bdLocation.getDistrict();
            String street = bdLocation.getStreet();
            String locationDescribe = bdLocation.getLocationDescribe();
        }
    }
}