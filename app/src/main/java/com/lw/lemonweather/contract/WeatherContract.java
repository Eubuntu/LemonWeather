package com.lw.lemonweather.contract;

import android.content.Context;

import com.lw.lemonweather.api.ApiService;
import com.lw.lemonweather.bean.AirNowResponse;
import com.lw.lemonweather.bean.BiYingImgResponse;
import com.lw.lemonweather.bean.DailyResponse;
import com.lw.lemonweather.bean.HourlyResponse;
import com.lw.lemonweather.bean.LifestyleResponse;
import com.lw.lemonweather.bean.NewSearchCityResponse;
import com.lw.lemonweather.bean.NowResponse;
import com.lw.mvplibrary.base.BasePresenter;
import com.lw.mvplibrary.base.BaseView;
import com.lw.mvplibrary.net.NetCallBack;
import com.lw.mvplibrary.net.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Response;

/**
 * 天气订阅器
 */
public class WeatherContract {
    public static class WeatherPresenter extends BasePresenter<IWeatherView> {
        /**
         * 必应 每日一图
         *
         * @param context
         */
        public void biying(final Context context) {
            ApiService service = ServiceGenerator.createService(ApiService.class, 1);
            service.biying().enqueue(new NetCallBack<BiYingImgResponse>() {
                @Override
                public void onSuccess(Call<BiYingImgResponse> call, Response<BiYingImgResponse> response) {
                    if (getView() != null) {
                        getView().getBiYingResult(response);
                    }
                }
                @Override
                public void onFailed() {
                    if (getView() != null) {
                        getView().getDataFailed();
                    }
                }
            });
        }

        /**
         * 搜索城市 V7版本中需要将定位城市id查询出，通过id查询详细数据
         *
         * @param location 城市名
         */
        public void newSearchCity(String location) {
            ApiService service = ServiceGenerator.createService(ApiService.class, 4);
            service.newSearchCity(location, "exact").enqueue(new NetCallBack<NewSearchCityResponse>() {
                @Override
                public void onSuccess(Call<NewSearchCityResponse> call, Response<NewSearchCityResponse> response) {
                    if (getView() != null) {
                        getView().getNewSearchCityResult(response);
                    }
                }
                @Override
                public void onFailed() {
                    if (getView() != null) {
                        getView().getDataFailed();
                    }
                }
            });
        }

        /**
         * 实况天气 V7版本
         * @param location 城市名
         */
        public void nowWeather(String location) {
            ApiService service = ServiceGenerator.createService(ApiService.class, 3);
            service.nowWeather(location).enqueue(new NetCallBack<NowResponse>() {
                @Override
                public void onSuccess(Call<NowResponse> call, Response<NowResponse> response) {
                    if (getView() != null) {
                        getView().getNowResult(response);
                    }
                }
                @Override
                public void onFailed() {
                    if (getView() != null) {
                        getView().getWeatherDataFailed();
                    }
                }
            });
        }

        /**
         * 天气预报 V7版本 7d表示获取7天的天气数据
         * @param location 城市名
         */
        public void dailyWeather(String location){
            ApiService service = ServiceGenerator.createService(ApiService.class, 3);
            service.dailyWeather("7d",location).enqueue(new NetCallBack<DailyResponse>() {
                @Override
                public void onSuccess(Call<DailyResponse> call, Response<DailyResponse> response) {
                    if (getView() != null) {
                        getView().getDailyResult(response);
                    }
                }
                @Override
                public void onFailed() {
                    if (getView() != null) {
                        getView().getWeatherDataFailed();
                    }
                }
            });
        }

        /**
         * 逐时天气预报（未来24小时）
         * @param location 城市名
         */
        public void hourlyWeather(String location){
            ApiService service = ServiceGenerator.createService(ApiService.class, 3);
            service.hourlyWeather(location).enqueue(new NetCallBack<HourlyResponse>() {
                @Override
                public void onSuccess(Call<HourlyResponse> call, Response<HourlyResponse> response) {
                    if (getView() != null) {
                        getView().getHourlyResult(response);
                    }
                }
                @Override
                public void onFailed() {
                    if (getView() != null) {
                        getView().getWeatherDataFailed();
                    }
                }
            });

        }

        /**
         * 当天空气质量
         * @param location   城市名
         */
        public void airNowWeather(String location){
            ApiService service = ServiceGenerator.createService(ApiService.class,3);
            service.airNowWeather(location).enqueue(new NetCallBack<AirNowResponse>() {
                @Override
                public void onSuccess(Call<AirNowResponse> call, Response<AirNowResponse> response) {
                    if(getView() != null){
                        getView().getAirNowResult(response);
                    }
                }
                @Override
                public void onFailed() {
                    if(getView() != null){
                        getView().getWeatherDataFailed();
                    }
                }
            });
        }

        /**
         * 生活指数
         * @param location   城市名  type中的"1,2,3,5,6,8,9,10"，表示只获取这8种类型的指数信息，同样是为了对应之前的界面UI
         */
        public void lifestyle(String location){
            ApiService service = ServiceGenerator.createService(ApiService.class,3);
            service.lifestyle("1,2,3,5,6,8,9,10",location).enqueue(new NetCallBack<LifestyleResponse>() {
                @Override
                public void onSuccess(Call<LifestyleResponse> call, Response<LifestyleResponse> response) {
                    if(getView() != null){
                        getView().getLifestyleResult(response);
                    }
                }
                @Override
                public void onFailed() {
                    if(getView() != null){
                        getView().getWeatherDataFailed();
                    }
                }
            });
        }
    }


    public interface IWeatherView extends BaseView {

        void getBiYingResult(Response<BiYingImgResponse> response);

        void getNewSearchCityResult(Response<NewSearchCityResponse> response);

        void getNowResult(Response<NowResponse> response);

        void getDailyResult(Response<DailyResponse> response);

        void getHourlyResult(Response<HourlyResponse> response);

        void getAirNowResult(Response<AirNowResponse> response);

        void getLifestyleResult(Response<LifestyleResponse> response);
        //错误返回
        void getDataFailed();
        //获取天气数据失败返回
        void getWeatherDataFailed();
    }
}
