package com.lw.lemonweather.contract;

import com.lw.lemonweather.api.ApiService;
import com.lw.lemonweather.bean.DailyResponse;
import com.lw.lemonweather.bean.HourlyResponse;
import com.lw.lemonweather.bean.NowResponse;
import com.lw.mvplibrary.base.BasePresenter;
import com.lw.mvplibrary.base.BaseView;
import com.lw.mvplibrary.net.NetCallBack;
import com.lw.mvplibrary.net.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Response;

/**
 * 世界城市天气订阅器
 */
public class WorldCityWeatherContract {

    public static class WorldCityWeatherPresenter extends BasePresenter<IWorldCityWeatherView> {

        /**
         * 实况天气  V7版本
         * @param location  城市名
         */
        public void nowWeather(String location){//这个3 表示使用新的V7API访问地址
            ApiService service = ServiceGenerator.createService(ApiService.class,3);
            service.nowWeather(location).enqueue(new NetCallBack<NowResponse>() {
                @Override
                public void onSuccess(Call<NowResponse> call, Response<NowResponse> response) {
                    if(getView() != null){
                        getView().getNowResult(response);
                    }
                }

                @Override
                public void onFailed() {
                    if(getView() != null){
                        getView().getDataFailed();
                    }
                }
            });
        }

        /**
         * 天气预报  V7版本   7d 表示天气的数据 为了和之前看上去差别小一些，这里先用七天的
         * @param location   城市名
         */
        public void dailyWeather(String location){//这个3 表示使用新的V7API访问地址
            ApiService service = ServiceGenerator.createService(ApiService.class,3);
            service.dailyWeather("7d",location).enqueue(new NetCallBack<DailyResponse>() {
                @Override
                public void onSuccess(Call<DailyResponse> call, Response<DailyResponse> response) {
                    if(getView() != null){
                        getView().getDailyResult(response);
                    }
                }

                @Override
                public void onFailed() {
                    if(getView() != null){
                        getView().getDataFailed();
                    }
                }
            });
        }

        /**
         * 逐小时预报（未来24小时）
         * @param location   城市名
         */
        public void hourlyWeather(String location){
            ApiService service = ServiceGenerator.createService(ApiService.class,3);
            service.hourlyWeather(location).enqueue(new NetCallBack<HourlyResponse>() {
                @Override
                public void onSuccess(Call<HourlyResponse> call, Response<HourlyResponse> response) {
                    if(getView() != null){
                        getView().getHourlyResult(response);
                    }
                }

                @Override
                public void onFailed() {
                    if(getView() != null){
                        getView().getDataFailed();
                    }
                }
            });
        }

    }

    public interface IWorldCityWeatherView extends BaseView {
        /*               以下为V7版本新增               */
        //实况天气
        void getNowResult(Response<NowResponse> response);
        //天气预报  7天
        void getDailyResult(Response<DailyResponse> response);
        //逐小时天气预报
        void getHourlyResult(Response<HourlyResponse> response);

        //错误返回
        void getDataFailed();
    }
}
