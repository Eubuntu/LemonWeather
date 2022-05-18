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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.lw.lemonweather.adapter.WeatherForecastAdapter;
import com.lw.lemonweather.adapter.WeatherHourlyAdapter;
import com.lw.lemonweather.bean.AirNowResponse;
import com.lw.lemonweather.bean.BiYingImgResponse;
import com.lw.lemonweather.bean.DailyResponse;
import com.lw.lemonweather.bean.HourlyResponse;
import com.lw.lemonweather.bean.LifestyleResponse;
import com.lw.lemonweather.bean.NewSearchCityResponse;
import com.lw.lemonweather.bean.NowResponse;
import com.lw.lemonweather.bean.TodayResponse;
import com.lw.lemonweather.bean.WeatherForecastResponse;
import com.lw.lemonweather.contract.WeatherContract;
import com.lw.lemonweather.utils.StatusBarUtil;
import com.lw.lemonweather.utils.ToastUtils;
import com.lw.mvplibrary.mvp.MvpActivity;
import com.lw.mvplibrary.view.WhiteWindmills;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

public class MainActivity extends MvpActivity<WeatherContract.WeatherPresenter> implements WeatherContract.IWeatherView {

    //权限请求框架
    private RxPermissions rxPermissions;
    //定位器
    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();

    private List<WeatherForecastResponse.HeWeather6Bean.DailyForecastBean> mList;
    private WeatherForecastAdapter mAdapter;
    private List<HourlyResponse.HourlyBean> mListHourly;
    private WeatherHourlyAdapter mAdapterHourly;

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
    private androidx.appcompat.widget.Toolbar toolbar;
    private ImageView ivLocation;
    private RecyclerView rvHourly;
    private TextView tvComf;
    private TextView tvTrav;
    private TextView tvSport;
    private TextView tvCw;
    private TextView tvAir;
    private TextView tvDrsg;
    private TextView tvFlu;
    private com.lw.mvplibrary.view.RoundProgressBar rpbAqi;
    private TextView tvPm10;
    private TextView tvPm25;
    private TextView tvNo2;
    private TextView tvSo2;
    private TextView tvO3;
    private TextView tvCo;

    @Override
    public void getDataFailed() {
        ToastUtils.showShortToast(context, "网络异常");
    }

    @Override
    public void getWeatherDataFailed() {

    }

    @Override
    public void initData(Bundle savedInstanceState) {
        StatusBarUtil.transparencyBar(context);
        initList();
        //实例化请求框架
        rxPermissions = new RxPermissions(this);
        permissionVersion();
    }

    /**
     * 初始化天气预报数据列表
     */
    private void initList() {
        //七天天气预报
        mList = new ArrayList<>();
        mAdapter = new WeatherForecastAdapter(R.layout.item_weather_forecast_list, mList);
        LinearLayoutManager manager = new LinearLayoutManager(context);
        rv.setLayoutManager(manager);
        rv.setAdapter(mAdapter);
        //逐小时天气预报
        mListHourly = new ArrayList<>();
        mAdapterHourly = new WeatherHourlyAdapter(R.layout.item_weather_hourly_list,mListHourly);
        LinearLayoutManager managerHourly = new LinearLayoutManager(context);
        managerHourly.setOrientation(RecyclerView.HORIZONTAL);
        rvHourly.setLayoutManager(managerHourly);
        rvHourly.setAdapter(mAdapterHourly);
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

    //动态权限申请
    private void permissionsRequest() {
        rxPermissions.request(Manifest.permission.ACCESS_FINE_LOCATION).subscribe(granted -> {
            if (granted) {
                //申请成功后，开始定位
                startLocation();
            } else {
                //申请失败
                ToastUtils.showShortToast(this, "权限未开启");
            }
        });
    }

    //定位
    private void startLocation() {
        LocationClient.setAgreePrivacy(true);
        try {
            //声明LocationClient类
            mLocationClient = new LocationClient(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mLocationClient != null) {
            //注册监听函数
            mLocationClient.registerLocationListener(myListener);
            LocationClientOption option = new LocationClientOption();
            //如果需要当前点的位置信息，设置为true
            option.setIsNeedAddress(true);
            //设置是否需要最新版本的地址信息。默认不需要，参数为false
            option.setNeedNewVersionRgc(true);
            //将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
            mLocationClient.setLocOption(option);
            //启动定位
            mLocationClient.start();
        }
    }

    private void initView() {
        bg = findViewById(R.id.bg);
        refresh = findViewById(R.id.refresh);
        toolbar = findViewById(R.id.toolbar);
        ivLocation = findViewById(R.id.iv_location);
        rvHourly = findViewById(R.id.rv_hourly);
        tvComf = findViewById(R.id.tv_comf);
        tvTrav = findViewById(R.id.tv_trav);
        tvSport = findViewById(R.id.tv_sport);
        tvCw = findViewById(R.id.tv_cw);
        tvAir = findViewById(R.id.tv_air);
        tvDrsg = findViewById(R.id.tv_drsg);
        tvFlu = findViewById(R.id.tv_flu);
        rpbAqi = findViewById(R.id.rpb_aqi);
        tvPm10 = findViewById(R.id.tv_pm10);
        tvPm25 = findViewById(R.id.tv_pm25);
        tvNo2 = findViewById(R.id.tv_no2);
        tvSo2 = findViewById(R.id.tv_so2);
        tvO3 = findViewById(R.id.tv_o3);
        tvCo = findViewById(R.id.tv_co);
    }

    /**
     *定位结果返回
     */
    private class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            //获取区/县
            district = bdLocation.getDistrict();
            //在数据请求之前加载等待弹窗
            showLoadingDialog();
            //获取生活指数数据
            //mPresent.lifestyle(context, district);
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
/*    @Override
    public void getTodayWeatherResult(Response<TodayResponse> response) {
        //数据返回后关闭定位
        mLocationClient.stop();
        if (response.body().getHeWeather6().get(0).getBasic() != null) {
            //得到数据不为空则进行数据显示
            //数据渲染显示出来
            //温度
            tvTemperature.setText(response.body().getHeWeather6().get(0).getNow().getTmp());
            //城市
            tvCity.setText(response.body().getHeWeather6().get(0).getBasic().getLocation());
            //天气状况
            tvInfo.setText(response.body().getHeWeather6().get(0).getNow().getCond_txt());
            tvOldTime.setText("上次更新时间：" + response.body().getHeWeather6().get(0).getUpdate().getLoc());
        } else {
            ToastUtils.showShortToast(context, response.body().getHeWeather6().get(0).getStatus());
        }
    }*/

    //查询天气预报，请求成功后的数据返回
  /*  @Override
    public void getWeatherForecastResult(Response<WeatherForecastResponse> response) {
        if (("ok").equals(response.body().getHeWeather6().get(0).getStatus())) {
            //最低温和最高温
            tvLowHeight.setText(response.body().getHeWeather6().get(0).getDaily_forecast().get(0).getTmp_min() + " / " +
                    response.body().getHeWeather6().get(0).getDaily_forecast().get(0).getTmp_max() + "℃");

            if (response.body().getHeWeather6().get(0).getDaily_forecast() != null) {
                List<WeatherForecastResponse.HeWeather6Bean.DailyForecastBean> data
                        = response.body().getHeWeather6().get(0).getDaily_forecast();
                mList.clear();//添加数据之前先清除
                mList.addAll(data);//添加数据
                mAdapter.notifyDataSetChanged();//刷新列表
            } else {
                ToastUtils.showShortToast(context, "天气预报数据为空");
            }
        } else {
            ToastUtils.showShortToast(context, response.body().getHeWeather6().get(0).getStatus());
        }
    }*/

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

    @Override
    public void getNewSearchCityResult(Response<NewSearchCityResponse> response) {

    }

    @Override
    public void getNowResult(Response<NowResponse> response) {

    }

    @Override
    public void getDailyResult(Response<DailyResponse> response) {

    }

    @Override
    public void getHourlyResult(Response<HourlyResponse> response) {
        //关闭弹窗
        dismissLoadingDialog();

    }

    @Override
    public void getAirNowResult(Response<AirNowResponse> response) {

    }

    @Override
    public void getLifestyleResult(Response<LifestyleResponse> response) {

    }

}