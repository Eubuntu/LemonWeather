package com.lw.lemonweather;

import android.Manifest;
import android.animation.Animator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lw.lemonweather.adapter.AreaAdapter;
import com.lw.lemonweather.adapter.CityAdapter;
import com.lw.lemonweather.adapter.DailyAdapter;
import com.lw.lemonweather.adapter.HourlyAdapter;
import com.lw.lemonweather.adapter.ProvinceAdapter;
import com.lw.lemonweather.adapter.WeatherForecastAdapter;
import com.lw.lemonweather.adapter.WeatherHourlyAdapter;
import com.lw.lemonweather.bean.AirNowResponse;
import com.lw.lemonweather.bean.BiYingImgResponse;
import com.lw.lemonweather.bean.CityResponse;
import com.lw.lemonweather.bean.DailyResponse;
import com.lw.lemonweather.bean.HourlyResponse;
import com.lw.lemonweather.bean.LifestyleResponse;
import com.lw.lemonweather.bean.NewSearchCityResponse;
import com.lw.lemonweather.bean.NowResponse;
import com.lw.lemonweather.bean.WeatherForecastResponse;
import com.lw.lemonweather.contract.WeatherContract;
import com.lw.lemonweather.eventbus.SearchCityEvent;
import com.lw.lemonweather.ui.BackgroundManagerActivity;
import com.lw.lemonweather.ui.CommonlyUsedCityActivity;
import com.lw.lemonweather.utils.CodeToStringUtils;
import com.lw.lemonweather.utils.Constant;
import com.lw.lemonweather.utils.DateUtils;
import com.lw.lemonweather.utils.SPUtils;
import com.lw.lemonweather.utils.StatusBarUtil;
import com.lw.lemonweather.utils.ToastUtils;
import com.lw.lemonweather.utils.WeatherUtil;
import com.lw.mvplibrary.mvp.MvpActivity;
import com.lw.mvplibrary.utils.AnimationUtil;
import com.lw.mvplibrary.utils.LiWindow;
import com.lw.mvplibrary.utils.SizeUtils;
import com.lw.mvplibrary.view.WhiteWindmills;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import retrofit2.Response;

public class MainActivity extends MvpActivity<WeatherContract.WeatherPresenter> implements WeatherContract.IWeatherView {

    private boolean flag = true;//图标显示标识,true显示，false不显示,只有定位的时候才为true,切换城市和常用城市都为false

    private RxPermissions rxPermissions;//权限请求框架
    //定位器
    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();
    //总数据  七天天气数据
    //List<WeatherResponse.HeWeather6Bean.DailyForecastBean> mListDailyForecast;
    //初始化适配器 七天天气预报
    WeatherForecastAdapter mAdapter;
    //总数据  逐小时天气预报
    //List<WeatherResponse.HeWeather6Bean.HourlyBean> mListHourlyBean;
    //初始化适配器 逐小时天气预报
    WeatherHourlyAdapter mAdapterHourly;
    //V7 版本
    //天气预报数据列表
    List<DailyResponse.DailyBean> dailyListV7 = new ArrayList<>();
    //天气预报适配器
    DailyAdapter mAdapterDailyV7;
    //逐小时天气预报数据列表
    List<HourlyResponse.HourlyBean> hourlyListV7 = new ArrayList<>();
    //逐小时预报适配器
    HourlyAdapter mAdapterHourlyV7;
    //字符串列表
    private List<String> list;
    //省列表数据
    private List<CityResponse> provinceList;
    //市列表数据
    private List<CityResponse.CityBean> citylist;
    //区/县列表数据
    private List<CityResponse.CityBean.AreaBean> arealist;
    //省数据适配器
    ProvinceAdapter provinceAdapter;
    //市数据适配器
    CityAdapter cityAdapter;
    //县/区数据适配器
    AreaAdapter areaAdapter;
    //标题
    String provinceTitle;
    //自定义弹窗
    LiWindow liWindow;
    //区/县  改为全局的静态变量,方便更换城市之后也能进行下拉刷新
    private String district = null;
    //市 国控站点数据  用于请求空气质量
    private String city;
    //城市id，用于查询城市数据  V7版本 中 才有
    private String locationId = null;

    //右上角的弹窗
    private PopupWindow mPopupWindow;
    private AnimationUtil animUtil;
    private float bgAlpha = 1f;
    private boolean bright = false;
    //0.5s
    private static final long DURATION = 500;
    //开始透明度
    private static final float START_ALPHA = 0.7f;
    //结束透明度
    private static final float END_ALPHA = 1f;
    //跳转其他页面时才为true
    public boolean flagOther = false;
    //搜索城市是否传递数据回来
    public boolean searchCityData = false;
    //进入手机定位设置页面标识
    private int OPEN_LOCATION = 9527;
    //弹窗中的今日温度
    private String dialogTemp = null;
    //弹窗中的天气状态
    private String dialogWeatherState = null;
    //弹窗中的天气状态码
    private int dialogWeatherStateCode = 0;
    //弹窗中的降水预告
    private String dialogPrecipitation = null;
    //弹窗中今日最高温
    private int dialogTempHeight = 0;
    //弹窗中今日最低温
    private int dialogTempLow = 0;

    private List<WeatherForecastResponse.HeWeather6Bean.DailyForecastBean> mList;
    private List<HourlyResponse.HourlyBean> mListHourly;

    //private com.scwang.smartrefresh.layout.SmartRefreshLayout refresh;

    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;//刷新布局

    private androidx.appcompat.widget.Toolbar toolbar;

    private android.widget.LinearLayout bg;
    //private ImageView ivCitySelect;
    private TextView tvInfo;
    private TextView tvTemperature;
    private TextView tvLowHeight;
    private TextView tvCity;
    private ImageView ivLocation;
    private TextView tvOldTime;
    private RecyclerView rvHourly;
    private RecyclerView rv;
    private com.lw.mvplibrary.view.RoundProgressBar rpbAqi;
    private TextView tvPm10;
    private TextView tvPm25;
    private TextView tvNo2;
    private TextView tvSo2;
    private TextView tvO3;
    private TextView tvCo;
    private RelativeLayout rlWind;
    private WhiteWindmills wwBig;
    private WhiteWindmills wwSmall;
    private TextView tvWindDirection;
    private TextView tvWindPower;
    private TextView tvComf;
    private TextView tvTrav;
    private TextView tvSport;
    private TextView tvCw;
    private TextView tvAir;
    private TextView tvDrsg;
    private TextView tvFlu;
    private ImageView ivAdd;

    @Override
    public void initData(Bundle savedInstanceState) {
        //透明状态栏
        StatusBarUtil.transparencyBar(context);
        //绑定控件
        initView();
        //天气预报列表初始化
        initList();
        //实例化请求框架
        rxPermissions = new RxPermissions(this);
        //权限判断
        permissionVersion();
        //由于这个刷新框架默认是有下拉和上拉，但是上拉没有用到，这里禁用上拉
        refresh.setEnableLoadMore(false);
        //初始化弹窗
        mPopupWindow = new PopupWindow(this);
        animUtil = new AnimationUtil();
        //注册
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    //接收
    public void onEvent(SearchCityEvent event) {
//        //获取weather所有数据
//        mPresent.weatherData(context, event.mLocation);
//        //获取空气质量数据
//        mPresent.airNowCity(context, event.mCity);
        //V7版本中需要先获取到城市ID ,在结果返回值中再进行下一步的数据查询
        mPresent.newSearchCity(event.mLocation);//相应事件时
    }

    private void initView() {
        bg = findViewById(R.id.bg);
        //ivCitySelect = findViewById(R.id.iv_city_select);
        tvInfo = findViewById(R.id.tv_info);
        tvTemperature = findViewById(R.id.tv_temperature);
        tvLowHeight = findViewById(R.id.tv_low_height);
        tvCity = findViewById(R.id.tv_city);
        ivLocation = findViewById(R.id.iv_location);
        tvOldTime = findViewById(R.id.tv_old_time);
        rvHourly = findViewById(R.id.rv_hourly);
        rv = findViewById(R.id.rv);
        rpbAqi = findViewById(R.id.rpb_aqi);
        tvPm10 = findViewById(R.id.tv_pm10);
        tvPm25 = findViewById(R.id.tv_pm25);
        tvNo2 = findViewById(R.id.tv_no2);
        tvSo2 = findViewById(R.id.tv_so2);
        tvO3 = findViewById(R.id.tv_o3);
        tvCo = findViewById(R.id.tv_co);
        rlWind = findViewById(R.id.rl_wind);
        wwBig = findViewById(R.id.ww_big);
        wwSmall = findViewById(R.id.ww_small);
        tvWindDirection = findViewById(R.id.tv_wind_direction);
        tvWindPower = findViewById(R.id.tv_wind_power);
        tvComf = findViewById(R.id.tv_comf);
        tvTrav = findViewById(R.id.tv_trav);
        tvSport = findViewById(R.id.tv_sport);
        tvCw = findViewById(R.id.tv_cw);
        tvAir = findViewById(R.id.tv_air);
        tvDrsg = findViewById(R.id.tv_drsg);
        tvFlu = findViewById(R.id.tv_flu);
        ivAdd = findViewById(R.id.iv_add);
    }
    /**
     * 初始化天气预报数据列表
     */
    private void initList() {
        //  V7 版本
        //天气预报  7天
        mAdapterDailyV7 = new DailyAdapter(R.layout.item_weather_forecast_list, dailyListV7);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(mAdapterDailyV7);
        //天气预报列表item点击事件
        mAdapterDailyV7.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                DailyResponse.DailyBean data = dailyListV7.get(position);
                showForecastWindow(data);
            }
        });

        //逐小时天气预报  24小时
        mAdapterHourlyV7 = new HourlyAdapter(R.layout.item_weather_hourly_list, hourlyListV7);
        LinearLayoutManager managerHourly = new LinearLayoutManager(context);
        managerHourly.setOrientation(RecyclerView.HORIZONTAL);//设置列表为横向
        rvHourly.setLayoutManager(managerHourly);
        rvHourly.setAdapter(mAdapterHourlyV7);
        //逐小时天气预报列表item点击事件
        mAdapterHourlyV7.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                //赋值
                HourlyResponse.HourlyBean data = hourlyListV7.get(position);
                //小时天气详情弹窗
                showHourlyWindow(data);
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    //绑定Presenter ，这里不绑定会报错
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

    /**
     * 城市弹窗
     */
    private void showCityWindow() {
        provinceList = new ArrayList<>();
        citylist = new ArrayList<>();
        arealist = new ArrayList<>();
        list = new ArrayList<>();
        liWindow = new LiWindow(context);
        final View view = LayoutInflater.from(context).inflate(R.layout.window_city_list, null);
        ImageView areaBack = (ImageView) view.findViewById(R.id.iv_back_area);
        ImageView cityBack = (ImageView) view.findViewById(R.id.iv_back_city);
        TextView windowTitle = (TextView) view.findViewById(R.id.tv_title);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv);
        liWindow.showRightPopupWindow(view);//显示弹窗
        initCityData(recyclerView, areaBack, cityBack, windowTitle);//加载城市列表数据
    }

    /**
     * 省市县数据渲染
     *
     * @param recyclerView 列表
     * @param areaBack     区县返回
     * @param cityBack     市返回
     * @param windowTitle  窗口标题
     */
    private void initCityData(RecyclerView recyclerView, ImageView areaBack, ImageView cityBack, TextView windowTitle) {
        //初始化省数据 读取省数据并显示到列表中
        try {
            InputStream inputStream = getResources().getAssets().open("City.txt");//读取数据
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer stringBuffer = new StringBuffer();
            String lines = bufferedReader.readLine();
            while (lines != null) {
                stringBuffer.append(lines);
                lines = bufferedReader.readLine();
            }

            final JSONArray Data = new JSONArray(stringBuffer.toString());
            //循环这个文件数组、获取数组中每个省对象的名字
            for (int i = 0; i < Data.length(); i++) {
                JSONObject provinceJsonObject = Data.getJSONObject(i);
                String provinceName = provinceJsonObject.getString("name");
                CityResponse response = new CityResponse();
                response.setName(provinceName);
                provinceList.add(response);
            }

            //定义省份显示适配器
            provinceAdapter = new ProvinceAdapter(R.layout.item_city_list, provinceList);
            LinearLayoutManager manager = new LinearLayoutManager(context);
            recyclerView.setLayoutManager(manager);
            recyclerView.setAdapter(provinceAdapter);
            provinceAdapter.notifyDataSetChanged();
            //runLayoutAnimationRight(recyclerView);//动画展示
            provinceAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    try {
                        //返回上一级数据
                        cityBack.setVisibility(View.VISIBLE);
                        cityBack.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                recyclerView.setAdapter(provinceAdapter);
                                provinceAdapter.notifyDataSetChanged();
                                cityBack.setVisibility(View.GONE);
                                windowTitle.setText("中国");
                            }
                        });

                        //根据当前位置的省份所在的数组位置、获取城市的数组
                        JSONObject provinceObject = Data.getJSONObject(position);
                        windowTitle.setText(provinceList.get(position).getName());
                        provinceTitle = provinceList.get(position).getName();
                        final JSONArray cityArray = provinceObject.getJSONArray("city");

                        //更新列表数据
                        if (citylist != null) {
                            citylist.clear();
                        }

                        for (int i = 0; i < cityArray.length(); i++) {
                            JSONObject cityObj = cityArray.getJSONObject(i);
                            String cityName = cityObj.getString("name");
                            CityResponse.CityBean response = new CityResponse.CityBean();
                            response.setName(cityName);
                            citylist.add(response);
                        }

                        cityAdapter = new CityAdapter(R.layout.item_city_list, citylist);
                        LinearLayoutManager manager1 = new LinearLayoutManager(context);
                        recyclerView.setLayoutManager(manager1);
                        recyclerView.setAdapter(cityAdapter);
                        cityAdapter.notifyDataSetChanged();
                        //runLayoutAnimationRight(recyclerView);

                        cityAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                            @Override
                            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                                try {
                                    //返回上一级数据
                                    areaBack.setVisibility(View.VISIBLE);
                                    areaBack.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            recyclerView.setAdapter(cityAdapter);
                                            cityAdapter.notifyDataSetChanged();
                                            areaBack.setVisibility(View.GONE);
                                            windowTitle.setText(provinceTitle);
                                            arealist.clear();
                                        }
                                    });
                                    //获取区/县的上级 市，用于请求空气质量数据API接口
                                    city = citylist.get(position).getName();
                                    //根据当前城市数组位置 获取地区数据
                                    windowTitle.setText(citylist.get(position).getName());
                                    JSONObject cityJsonObj = cityArray.getJSONObject(position);
                                    JSONArray areaJsonArray = cityJsonObj.getJSONArray("area");
                                    if (arealist != null) {
                                        arealist.clear();
                                    }
                                    if (list != null) {
                                        list.clear();
                                    }
                                    for (int i = 0; i < areaJsonArray.length(); i++) {
                                        list.add(areaJsonArray.getString(i));
                                    }
                                    Log.i("list", list.toString());
                                    for (int j = 0; j < list.size(); j++) {
                                        CityResponse.CityBean.AreaBean response = new CityResponse.CityBean.AreaBean();
                                        response.setName(list.get(j).toString());
                                        arealist.add(response);
                                    }
                                    areaAdapter = new AreaAdapter(R.layout.item_city_list, arealist);
                                    LinearLayoutManager manager2 = new LinearLayoutManager(context);

                                    recyclerView.setLayoutManager(manager2);
                                    recyclerView.setAdapter(areaAdapter);
                                    areaAdapter.notifyDataSetChanged();
                                    //runLayoutAnimationRight(recyclerView);

                                    areaAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                                        @Override
                                        public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                                            showLoadingDialog();
                                            district = arealist.get(position).getName();//选中的区/县赋值给这个全局变量

                                            //mPresent.weatherData(context, district);//获取weather所有数据
                                            //mPresent.airNowCity(context, city);//空气质量数据

                                            //V7版本中需要先获取到城市ID ,在结果返回值中再进行下一步的数据查询
                                            mPresent.newSearchCity(district);//切换城市时

                                            flag = false;//切换城市得到的城市不属于定位，因此这里隐藏定位图标
                                            liWindow.closePopupWindow();//关闭弹窗
                                        }
                                    });
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    //点击事件  图标的ID也做了更换，点击之后的弹窗也更换了
    /*@OnClick(R.id.iv_add)
    public void onViewClicked() {
        //showAddWindow();//更多功能弹窗
        toggleBright();//计算动画时间
    }*/

    /**
     * 定位结果返回
     */
    private class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            //获取区/县
            district = bdLocation.getDistrict();
            //获取市
            city = bdLocation.getCity();
            //在数据请求之前加载等待弹窗
            showLoadingDialog();
            //mPresent.weatherData(context, district);
            //获取空气质量数据
            //mPresent.airNowCity(context, city);
            mPresent.newSearchCity(district);
            //下拉刷新
            refresh.setOnRefreshListener(refreshLayout -> {
                //获取weather所有数据
                //mPresent.weatherData(context, district);
                //获取空气质量数据
                //mPresent.airNowCity(context, city);
                mPresent.newSearchCity(district);
            });
            //获取必应每日一图
            mPresent.biying(context);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在数据请求之前放在加载等待弹窗，返回结果后关闭弹窗
        showLoadingDialog();
        flagOther = SPUtils.getBoolean(Constant.FLAG_OTHER_RETURN, false, context);
        if (flagOther == true) {
            //取出缓存
            district = SPUtils.getString(Constant.DISTRICT, "", context);
            city = SPUtils.getString(Constant.CITY, "", context);
            //获取weather所有数据
            //mPresent.weatherData(context, district);
            //获取空气质量数据
            //mPresent.airNowCity(context, city);
            //V7版本中需要先获取到城市ID ,在结果返回值中再进行下一步的数据查询
            //其他页面返回时
            mPresent.newSearchCity(district);
        } else {
            dismissLoadingDialog();
        }
        //是否开启了切换背景
        isOpenChangeBg();
    }

    /**
     * 判断是否开启了切换背景，没有开启则用默认的背景
     */
    private void isOpenChangeBg () {
        boolean isEverydayImg = SPUtils.getBoolean(Constant.EVERYDAY_IMG, false, context);//每日图片
        boolean isImgList = SPUtils.getBoolean(Constant.IMG_LIST, false, context);//图片列表
        boolean isCustomImg = SPUtils.getBoolean(Constant.CUSTOM_IMG, false, context);//手动定义
        //因为只有有一个为true，其他两个就都会是false,所以可以一个一个的判断
        if (isEverydayImg != true && isImgList != true && isCustomImg != true) {
            //当所有开关都没有打开的时候用默认的图片
            bg.setBackgroundResource(R.drawable.img_5);
        } else {
            if (isEverydayImg != false) {//开启每日一图
                mPresent.biying(context);
            } else if (isImgList != false) {//开启图片列表
                int position = SPUtils.getInt(Constant.IMG_POSITION, -1, context);
                switch (position) {
                    case 0:
                        bg.setBackgroundResource(R.drawable.img_5);
                        break;
                    case 1:
                        bg.setBackgroundResource(R.drawable.img_5);
                        break;
                    case 2:
                        bg.setBackgroundResource(R.drawable.img_5);
                        break;
                    case 3:
                        bg.setBackgroundResource(R.drawable.img_5);
                        break;
                    case 4:
                        bg.setBackgroundResource(R.drawable.img_5);
                        break;
                    case 5:
                        bg.setBackgroundResource(R.drawable.img_6);
                        break;
                }
            } else if (isCustomImg != false) {
                String imgPath = SPUtils.getString(Constant.CUSTOM_IMG_PATH, "", context);
                Glide.with(context)
                        .asBitmap()
                        .load(imgPath)
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                                Drawable drawable = new BitmapDrawable(context.getResources(), resource);
                                bg.setBackground(drawable);
                            }
                        });
            }
        }
    }
    //获取必应每日一图返回
    @Override
    public void getBiYingResult(Response<BiYingImgResponse> response) {
        dismissLoadingDialog();
        if (response.body().getImages() != null) {
            String imgUrl = "http://cn.bing.com" + response.body().getImages().get(0).getUrl();
            Glide.with(context).asBitmap().load(imgUrl).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    Drawable drawable = new BitmapDrawable(context.getResources(), resource);
                    bg.setBackground(drawable);
                }
            });
        } else {
            ToastUtils.showShortToast(context, "数据为空");
        }
    }

    @Override
    public void getNewSearchCityResult(Response<NewSearchCityResponse> response) {
        //关闭刷新
        refresh.finishRefresh();
        //关闭弹窗
        dismissLoadingDialog();
        if (mLocationClient != null) {
            //数据返回后关闭定位
            mLocationClient.stop();
        }
        if (response.body().getStatus().equals(Constant.SUCCESS_CODE)) {
            if (response.body().getLocation() != null && response.body().getLocation().size() > 0) {
                //城市
                tvCity.setText(response.body().getLocation().get(0).getName());
                //城市Id
                locationId = response.body().getLocation().get(0).getId();

                showLoadingDialog();
                //查询实况天气
                mPresent.nowWeather(locationId);
                //查询天气预报
                mPresent.dailyWeather(locationId);
                //查询逐小时天气预报
                mPresent.hourlyWeather(locationId);
                //空气质量
                mPresent.airNowWeather(locationId);
                //生活指数
                mPresent.lifestyle(locationId);
            } else {
                ToastUtils.showShortToast(context, "数据为空");
            }
        } else {
            tvCity.setText("查询城市失败");
            ToastUtils.showShortToast(context, CodeToStringUtils.WeatherCode(response.body().getStatus()));
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void getNowResult(Response<NowResponse> response) {
        dismissLoadingDialog();
        //200则成功返回数据
        if (response.body().getCode().equals(Constant.SUCCESS_CODE)) {
            //根据V7版本的原则，只要是200就一定有数据，我们可以不用做判空处理，但是，为了使程序不ANR，还是要做的
            NowResponse data = response.body();
            if (data != null) {
                //温度
                tvTemperature.setText(data.getNow().getTemp());
                if (flag) {
                    //显示定位图标
                    ivLocation.setVisibility(View.VISIBLE);
                } else {
                    //不显示定位图标
                    ivLocation.setVisibility(View.GONE);
                }
                //天气状况
                tvInfo.setText(data.getNow().getText());
                //截去前面的字符，保留后面所有的字符，就剩下 22:00
                String time = DateUtils.updateTime(data.getUpdateTime());

                tvOldTime.setText("上次更新时间：" + WeatherUtil.showTimeInfo(time) + time);
                //风向
                tvWindDirection.setText("风向     " + data.getNow().getWindDir());
                //风力
                tvWindPower.setText("风力     " + data.getNow().getWindScale() + "级");
                //大风车开始转动
                wwBig.startRotate();
                //小风车开始转动
                wwSmall.startRotate();
            } else {
                ToastUtils.showShortToast(context, "暂无实况天气数据");
            }
        } else {
            //其他状态返回提示文字
            ToastUtils.showShortToast(context, CodeToStringUtils.WeatherCode(response.body().getCode()));
        }
    }


    @Override
    public void getDailyResult(Response<DailyResponse> response) {
        if (response.body().getCode().equals(Constant.SUCCESS_CODE)) {
            List<DailyResponse.DailyBean> data = response.body().getDaily();
            if (data != null && data.size() > 0) {
                //判空处理
                tvLowHeight.setText(data.get(0).getTempMin() + " / " + data.get(0).getTempMax() + "℃");
                //添加数据之前先清除
                dailyListV7.clear();
                //添加数据
                dailyListV7.addAll(data);
                //刷新列表
                mAdapterDailyV7.notifyDataSetChanged();
            } else {
                ToastUtils.showShortToast(context, "天气预报数据为空");
            }
        } else {
            //异常状态码返回
            ToastUtils.showShortToast(context, CodeToStringUtils.WeatherCode(response.body().getCode()));
        }
    }

    @Override
    public void getHourlyResult(Response<HourlyResponse> response) {
        //关闭弹窗
        dismissLoadingDialog();
        if (response.body().getCode().equals(Constant.SUCCESS_CODE)) {
            List<HourlyResponse.HourlyBean> data = response.body().getHourly();
            if (data != null && data.size() > 0) {
                hourlyListV7.clear();
                hourlyListV7.addAll(data);
                mAdapterHourlyV7.notifyDataSetChanged();
            } else {
                ToastUtils.showShortToast(context, "逐小时预报数据为空");
            }
        } else {
            ToastUtils.showShortToast(context, CodeToStringUtils.WeatherCode(response.body().getCode()));
        }
    }

    /**
     * 空气质量返回  V7
     *
     * @param response
     */
    @Override
    public void getAirNowResult(Response<AirNowResponse> response) {
        if (response.body().getCode().equals(Constant.SUCCESS_CODE)) {
            AirNowResponse.NowBean data = response.body().getNow();
            if (response.body().getNow() != null) {
                rpbAqi.setMaxProgress(300);//最大进度，用于计算
                rpbAqi.setMinText("0");//设置显示最小值
                rpbAqi.setMinTextSize(32f);
                rpbAqi.setMaxText("300");//设置显示最大值
                rpbAqi.setMaxTextSize(32f);
                rpbAqi.setProgress(Float.valueOf(data.getAqi()));//当前进度
                //rpbAqi.setArcBgColor(getResources().getColor(R.color.arc_bg_color));//圆弧的颜色
                //rpbAqi.setProgressColor(getResources().getColor(R.color.arc_progress_color));//进度圆弧的颜色
                rpbAqi.setFirstText(data.getCategory());//空气质量描述 取值范围：优，良，轻度污染，中度污染，重度污染，严重污染
                rpbAqi.setFirstTextSize(44f);//第一行文本的字体大小
                rpbAqi.setSecondText(data.getAqi());//空气质量值
                rpbAqi.setSecondTextSize(64f);//第二行文本的字体大小
                rpbAqi.setMinText("0");
                //rpbAqi.setMinTextColor(getResources().getColor(R.color.arc_progress_color));
                //PM10
                tvPm10.setText(data.getPm10());
                //PM2.5
                tvPm25.setText(data.getPm2p5());
                //二氧化氮
                tvNo2.setText(data.getNo2());
                //二氧化硫
                tvSo2.setText(data.getSo2());
                //臭氧
                tvO3.setText(data.getO3());
                //一氧化碳
                tvCo.setText(data.getCo());
            } else {
                ToastUtils.showShortToast(context, "空气质量数据为空");
            }
        } else {
            ToastUtils.showShortToast(context, CodeToStringUtils.WeatherCode(response.body().getCode()));
        }
    }

    /**
     * 生活数据返回 V7
     *
     * @param response
     */
    @Override
    public void getLifestyleResult(Response<LifestyleResponse> response) {
        if (response.body().getCode().equals(Constant.SUCCESS_CODE)) {
            List<LifestyleResponse.DailyBean> data = response.body().getDaily();
            for (int i = 0; i < data.size(); i++) {
                switch (data.get(i).getType()) {
                    case "5":
                        //tvUv.setText("紫外线：" + data.get(i).getText());
                        break;
                    case "8":
                        tvComf.setText("舒适度：" + data.get(i).getText());
                        break;
                    case "3":
                        tvDrsg.setText("穿衣指数：" + data.get(i).getText());
                        break;
                    case "9":
                        tvFlu.setText("感冒指数：" + data.get(i).getText());
                        break;
                    case "1":
                        tvSport.setText("运动指数：" + data.get(i).getText());
                        break;
                    case "6":
                        tvTrav.setText("旅游指数：" + data.get(i).getText());
                        break;
                    case "2":
                        tvCw.setText("洗车指数：" + data.get(i).getText());
                        break;
                    case "10":
                        tvAir.setText("空气指数：" + data.get(i).getText());
                        break;
                }
            }
        } else {
            ToastUtils.showShortToast(context, CodeToStringUtils.WeatherCode(response.body().getCode()));
        }
    }

    /**
     * 数据请求失败返回
     */
    @Override
    public void getWeatherDataFailed() {
        //关闭刷新
        refresh.finishRefresh();
        //关闭弹窗
        dismissLoadingDialog();
        ToastUtils.showShortToast(context, "天气数据获取异常");
    }

    @Override
    public void getDataFailed() {
        ToastUtils.showShortToast(context, "网络异常");
    }

    /**
     * 页面销毁时
     */
    @Override
    public void onDestroy() {
        wwBig.stop();//停止大风车
        wwSmall.stop();//停止小风车
        EventBus.getDefault().unregister(this);//解注
        super.onDestroy();
    }

    /**
     * 显示天气预报详情弹窗
     */
    private void showForecastWindow(DailyResponse.DailyBean data) {
        liWindow = new LiWindow(context);
        final View view = LayoutInflater.from(context).inflate(R.layout.window_forecast_detail, null);
        TextView tv_datetime = view.findViewById(R.id.tv_datetime);
        //最高温
        TextView tv_tmp_max = view.findViewById(R.id.tv_tmp_max);
        //最低温
        TextView tv_tmp_min = view.findViewById(R.id.tv_tmp_min);
        //紫外线强度
        TextView tv_uv_index = view.findViewById(R.id.tv_uv_index);
        //白天天气状态
        TextView tv_cond_txt_d = view.findViewById(R.id.tv_cond_txt_d);
        //晚上天气状态
        TextView tv_cond_txt_n = view.findViewById(R.id.tv_cond_txt_n);
        //风向360角度
        TextView tv_wind_deg = view.findViewById(R.id.tv_wind_deg);
        //风向
        TextView tv_wind_dir = view.findViewById(R.id.tv_wind_dir);
        //风力
        TextView tv_wind_sc = view.findViewById(R.id.tv_wind_sc);
        //风速
        TextView tv_wind_spd = view.findViewById(R.id.tv_wind_spd);
        //云量  V7 新增
        TextView tv_cloud = view.findViewById(R.id.tv_cloud);
        //相对湿度
        TextView tv_hum = view.findViewById(R.id.tv_hum);
        //大气压强
        TextView tv_pres = view.findViewById(R.id.tv_pres);
        //降水量
        TextView tv_pcpn = view.findViewById(R.id.tv_pcpn);
        //能见度
        TextView tv_vis = view.findViewById(R.id.tv_vis);
        //时间日期
        tv_datetime.setText(data.getFxDate() + "   " + DateUtils.Week(data.getFxDate()));
        tv_tmp_max.setText(data.getTempMax() + "℃");
        tv_tmp_min.setText(data.getTempMin() + "℃");
        tv_uv_index.setText(data.getUvIndex());
        tv_cond_txt_d.setText(data.getTextDay());
        tv_cond_txt_n.setText(data.getTextNight());
        tv_wind_deg.setText(data.getWind360Day() + "°");
        tv_wind_dir.setText(data.getWindDirDay());
        tv_wind_sc.setText(data.getWindScaleDay() + "级");
        tv_wind_spd.setText(data.getWindSpeedDay() + "公里/小时");
        //V7 版本中，新增 云量
        tv_cloud.setText(data.getCloud() + "%");
        tv_hum.setText(data.getHumidity() + "%");
        tv_pres.setText(data.getPressure() + "hPa");
        tv_pcpn.setText(data.getPrecip() + "mm");
        tv_vis.setText(data.getVis() + "km");
        liWindow.showCenterPopupWindow(view, SizeUtils.dp2px(context, 300), SizeUtils.dp2px(context, 500), true);
    }

    /**
     * 显示小时详情天气信息弹窗
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void showHourlyWindow(HourlyResponse.HourlyBean data) {
        liWindow = new LiWindow(context);
        final View view = LayoutInflater.from(context).inflate(R.layout.window_hourly_detail, null);
        //时间
        TextView tv_time = view.findViewById(R.id.tv_time);
        //温度
        TextView tv_tem = view.findViewById(R.id.tv_tem);
        //天气状态描述
        TextView tv_cond_txt = view.findViewById(R.id.tv_cond_txt);
        //风向360角度
        TextView tv_wind_deg = view.findViewById(R.id.tv_wind_deg);
        //风向
        TextView tv_wind_dir = view.findViewById(R.id.tv_wind_dir);
        //风力
        TextView tv_wind_sc = view.findViewById(R.id.tv_wind_sc);
        //风速
        TextView tv_wind_spd = view.findViewById(R.id.tv_wind_spd);
        //相对湿度
        TextView tv_hum = view.findViewById(R.id.tv_hum);
        //大气压强
        TextView tv_pres = view.findViewById(R.id.tv_pres);
        //降水概率
        TextView tv_pop = view.findViewById(R.id.tv_pop);
        //露点温度
        TextView tv_dew = view.findViewById(R.id.tv_dew);
        //云量
        TextView tv_cloud = view.findViewById(R.id.tv_cloud);

        String time = DateUtils.updateTime(data.getFxTime());
        tv_time.setText(WeatherUtil.showTimeInfo(time) + time);
        tv_tem.setText(data.getTemp() + "℃");
        tv_cond_txt.setText(data.getText());
        tv_wind_deg.setText(data.getWind360() + "°");
        tv_wind_dir.setText(data.getWindDir());
        tv_wind_sc.setText(data.getWindScale() + "级");
        tv_wind_spd.setText(data.getWindSpeed() + "公里/小时");
        tv_hum.setText(data.getHumidity() + "%");
        tv_pres.setText(data.getPressure() + "hPa");
        tv_pop.setText(data.getPop() + "%");
        tv_dew.setText(data.getDew() + "℃");
        tv_cloud.setText(data.getCloud() + "%");
        liWindow.showCenterPopupWindow(view, SizeUtils.dp2px(context, 300), SizeUtils.dp2px(context, 400), true);
    }

    /**
     * 更多功能弹窗，因为区别于我原先写的弹窗
     */
    private void showAddWindow() {
        // 设置布局文件
        mPopupWindow.setContentView(LayoutInflater.from(this).inflate(R.layout.window_add, null));// 为了避免部分机型不显示，我们需要重新设置一下宽高
        mPopupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x0000));// 设置pop透明效果
        mPopupWindow.setAnimationStyle(com.lw.mvplibrary.R.style.pop_add);// 设置pop出入动画
        mPopupWindow.setFocusable(true);// 设置pop获取焦点，如果为false点击返回按钮会退出当前Activity，如果pop中有Editor的话，focusable必须要为true
        mPopupWindow.setTouchable(true);// 设置pop可点击，为false点击事件无效，默认为true
        mPopupWindow.setOutsideTouchable(true);// 设置点击pop外侧消失，默认为false；在focusable为true时点击外侧始终消失
        mPopupWindow.showAsDropDown(ivAdd, -100, 0);// 相对于 + 号正下面，同时可以设置偏移量
        // 设置pop关闭监听，用于改变背景透明度
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {//关闭弹窗
            @Override
            public void onDismiss() {
                toggleBright();
            }
        });
        //绑定布局中的控件
        TextView changeCity = mPopupWindow.getContentView().findViewById(R.id.tv_change_city);//切换城市
        TextView changeBg = mPopupWindow.getContentView().findViewById(R.id.tv_wallpaper);//切换背景
        TextView searchCity = mPopupWindow.getContentView().findViewById(R.id.tv_search_city);//城市搜索
        //TextView hotCity = mPopupWindow.getContentView().findViewById(R.id.tv_hot_city);//热门城市
        TextView residentCity = mPopupWindow.getContentView().findViewById(R.id.tv_resident_city);//常用城市
        //TextView more = mPopupWindow.getContentView().findViewById(R.id.tv_more);
        //切换城市
        changeCity.setOnClickListener(view -> {
            showCityWindow();
            mPopupWindow.dismiss();
        });
        //切换背景
        changeBg.setOnClickListener(view -> {
            //放入缓存
            SPUtils.putBoolean(Constant.FLAG_OTHER_RETURN, true, context);//缓存标识
            SPUtils.putString(Constant.DISTRICT, district, context);
            SPUtils.putString(Constant.CITY, city, context);
            startActivity(new Intent(context, BackgroundManagerActivity.class));
            mPopupWindow.dismiss();
        });
        /*searchCity.setOnClickListener(view -> {//城市搜索
            SPUtils.putBoolean(Constant.FLAG_OTHER_RETURN, false, context);//缓存标识
            startActivity(new Intent(context, SearchCityActivity.class));
            mPopupWindow.dismiss();
        });*/
        /*hotCity.setOnClickListener(view -> {//热门城市
            startActivity(new Intent(context, HotCityActivity.class));
            mPopupWindow.dismiss();
        });*/
        residentCity.setOnClickListener(view -> {//常用城市
            startActivity(new Intent(context, CommonlyUsedCityActivity.class));
            mPopupWindow.dismiss();
        });
        /*more.setOnClickListener(view -> {//更多功能
            ToastUtils.showShortToast(context, "如果你有什么好的建议，可以博客留言哦！");
            mPopupWindow.dismiss();
        });*/
    }

    /**
     * 计算动画时间
     */
    private void toggleBright() {
        // 三个参数分别为：起始值 结束值 时长，那么整个动画回调过来的值就是从0.5f--1f的
        animUtil.setValueAnimator(START_ALPHA, END_ALPHA, DURATION);
        animUtil.addUpdateListener(new AnimationUtil.UpdateListener() {
            @Override
            public void progress(float progress) {
                // 此处系统会根据上述三个值，计算每次回调的值是多少，我们根据这个值来改变透明度
                bgAlpha = bright ? progress : (START_ALPHA + END_ALPHA - progress);
                backgroundAlpha(bgAlpha);
            }
        });
        animUtil.addEndListner(new AnimationUtil.EndListener() {
            @Override
            public void endUpdate(Animator animator) {
                // 在一次动画结束的时候，翻转状态
                bright = !bright;
            }
        });
        animUtil.startAnimator();
    }

    /**
     * 此方法用于改变背景的透明度，从而达到“变暗”的效果
     */
    private void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        // 0.0-1.0
        lp.alpha = bgAlpha;
        getWindow().setAttributes(lp);
        // everything behind this window will be dimmed.
        // 此方法用来设置浮动层，防止部分手机变暗无效
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }
}