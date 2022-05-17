package com.lw.lemonweather.contract;

import android.content.Context;

import com.lw.lemonweather.api.ApiService;
import com.lw.lemonweather.bean.BiYingImgResponse;
import com.lw.lemonweather.bean.NewSearchCityResponse;
import com.lw.lemonweather.bean.NowResponse;
import com.lw.lemonweather.bean.TodayResponse;
import com.lw.lemonweather.bean.WeatherForecastResponse;
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
                        getView().getDataFailed();
                    }
                }
            });
        }


    }


    public interface IWeatherView extends BaseView {

        void getBiYingResult(Response<BiYingImgResponse> response);

        void getNewSearchCityResult(Response<NewSearchCityResponse> response);

        void getNowResult(Response<NowResponse> response);


        //错误返回
        void getDataFailed();


    }
}
