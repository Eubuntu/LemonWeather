package com.lw.lemonweather;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.lw.lemonweather.bean.BiYingImgResponse;
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
    private android.widget.LinearLayout bg;
    private com.scwang.smartrefresh.layout.SmartRefreshLayout refresh;

    private String district;

    @Override
    public void getDataFailed() {
        ToastUtils.showShortToast(context, "网络异常");
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        rxPermissions = new RxPermissions(this);
        permissionVersion();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected WeatherContract.WeatherPresenter createPresent() {
        return new WeatherContract.WeatherPresenter();
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

    private void initView() {
        bg = findViewById(R.id.bg);
        refresh = findViewById(R.id.refresh);
    }

    /**
     *
     */
    private class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            //获取区/县
            String district = bdLocation.getDistrict();

            showLoadingDialog();

            //获取今天的天气数据
            mPresent.todayWeather(context,district);

            mPresent.weatherForecast(context,district);
            //获取必应每日一图
            mPresent.biying(context);

            /*double latitude = bdLocation.getLatitude();
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
            String locationDescribe = bdLocation.getLocationDescribe();*/
        }
    }

    //查询当天天气，请求成功后的数据返回
    @Override
    public void getTodayWeatherResult(Response<TodayResponse> response) {
        //数据返回后关闭定位
        mLocationClient.stop();
        if (response.body().getHeWeather6().get(0).getBasic() != null) {//得到数据不为空则进行数据显示
            //数据渲染显示出来
            tvTemperature.setText(response.body().getHeWeather6().get(0).getNow().getTmp());//温度
            tvCity.setText(response.body().getHeWeather6().get(0).getBasic().getLocation());//城市
            tvInfo.setText(response.body().getHeWeather6().get(0).getNow().getCond_txt());//天气状况
            tvOldTime.setText("上次更新时间：" + response.body().getHeWeather6().get(0).getUpdate().getLoc());
        } else {
            ToastUtils.showShortToast(context, response.body().getHeWeather6().get(0).getStatus());
        }
    }

    @Override
    public void getWeatherForecastResult(Response<WeatherForecastResponse> response) {

    }

    @Override
    public void getBiYingResult(Response<BiYingImgResponse> response) {
        dismissLoadingDialog();
        if (response.body().getImages() != null){
            String imgUrl = "http://cn.bing.com" + response.body().getImages().get(0).getUrl();
            Glide.with(context).asBitmap().load(imgUrl).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    Drawable drawable = new BitmapDrawable(context.getResources(),resource);
                    bg.setBackground(drawable);
                }
            });
        }else {
            ToastUtils.showShortToast(context, "数据为空");
        }
    }

}