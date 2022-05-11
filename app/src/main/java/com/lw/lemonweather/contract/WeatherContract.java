package com.lw.lemonweather.contract;

import android.content.Context;

import com.lw.lemonweather.api.ApiService;
import com.lw.lemonweather.bean.TodayResponse;
import com.lw.lemonweather.bean.WeatherForecastResponse;
import com.lw.mvplibrary.base.BasePresenter;
import com.lw.mvplibrary.base.BaseView;
import com.lw.mvplibrary.net.NetCallBack;
import com.lw.mvplibrary.net.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Response;

public class WeatherContract {
    public static class WeatherPresenter extends BasePresenter<IWeatherView> {

        /*public void todayWeather(final Context context, String location) {
            ApiService service = ServiceGenerator.createService(ApiService.class);
            service.getTodayWeather(location).enqueue(new NetCallBack<TodayResponse>() {
                @Override
                public void onSuccess(Call<TodayResponse> call, Response<TodayResponse> response) {
                    if (getView() != null) {
                        getView().getTodayWeatherResult(response);
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

        public void weatherForecast(final Context context, String location) {
            ApiService service = ServiceGenerator.createService(ApiService.class);
            service.getWeatherForecast(location).enqueue(new NetCallBack<WeatherForecastResponse>() {
                @Override
                public void onSuccess(Call<WeatherForecastResponse> call, Response<WeatherForecastResponse> response) {
                    if (getView() != null) {
                        getView().getWeatherForecastResult(response);
                    }
                }

                @Override
                public void onFailed() {
                    if (getView() != null) {
                        getView().getDataFailed();
                    }
                }
            });
        }*/


    }


    public interface IWeatherView extends BaseView {
        /*void getTodayWeatherResult(Response<TodayResponse> response);

        void getWeatherForecastResult(Response<WeatherForecastResponse> response);*/

        void getDataFailed();
    }
}
