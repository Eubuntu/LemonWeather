package com.lw.lemonweather.ui;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.lw.lemonweather.MainActivity;
import com.lw.lemonweather.R;
import com.lw.lemonweather.bean.BiYingImgResponse;
import com.lw.lemonweather.contract.SplashContract;
import com.lw.lemonweather.utils.Constant;
import com.lw.lemonweather.utils.SPUtils;
import com.lw.lemonweather.utils.StatusBarUtil;
import com.lw.lemonweather.utils.ToastUtils;
import com.lw.mvplibrary.base.BaseActivity;
import com.lw.mvplibrary.bean.Country;
import com.lw.mvplibrary.mvp.MvpActivity;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.litepal.LitePal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import retrofit2.Response;

/**
 * 欢迎页
 */
public class SplashActivity extends MvpActivity<SplashContract.SplashPresenter> implements SplashContract.ISplashView {
    //权限请求框架
    private RxPermissions rxPermissions;

    @Override
    public void initData(Bundle savedInstanceState) {
        //透明状态栏
        StatusBarUtil.transparencyBar(context);
        //权限判断
        permissionVersion();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
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
        //使用这个框架需要制定JDK版本，建议用1.8  实例化这个权限请求框架，否则会报错
        rxPermissions = new RxPermissions(this);
        rxPermissions.request(Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe(granted -> {
                    if (granted) {//申请成功
                        //得到权限可以进入APP
                        //加载世界国家数据到本地数据库,已有则不加载
                        initCountryData();
                        //获取必应壁纸
                        mPresent.biying();
                    } else {//申请失败
                        finish();
                        ToastUtils.showShortToast(this, "权限未开启");
                    }
                });
    }

    private List<Country> list;

    private void initCountryData() {
        list = LitePal.findAll(Country.class);
        if (list.size() > 0) {
            //有数据
            goToMain();
        } else {
            //第一次加载
            InputStreamReader is = null;
            try {
                is = new InputStreamReader(getAssets().open("world_country.csv"), "UTF-8");
                BufferedReader reader = new BufferedReader(is);
                reader.readLine();
                String line;

                while ((line = reader.readLine()) != null) {
                    String[] result = line.split(",");
                    Country country = new Country();
                    country.setName(result[0]);
                    country.setCode(result[1]);
                    country.save();
                }
                goToMain();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 进入主页面
     */
    private void goToMain() {
        new Handler().postDelayed(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void run() {
                finish();
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
            }
        }, 1000);
    }

    @Override
    protected SplashContract.SplashPresenter createPresent() {
        return new SplashContract.SplashPresenter();
    }

    /**
     * 必应壁纸数据返回
     *
     * @param response BiYingImgResponse
     */
    @Override
    public void getBiYingResult(Response<BiYingImgResponse> response) {
        if (response.body().getImages() != null) {
            //得到的图片地址是没有前缀的，所以加上前缀否则显示不出来
            String biyingUrl = "http://cn.bing.com" + response.body().getImages().get(0).getUrl();
            SPUtils.putString(Constant.EVERYDAY_TIP_IMG,biyingUrl,context);
        } else {
            ToastUtils.showShortToast(context, "未获取到必应的图片");
        }
    }

    @Override
    public void getDataFailed() {
        Log.d("Network Error", "网络异常");
    }
}
