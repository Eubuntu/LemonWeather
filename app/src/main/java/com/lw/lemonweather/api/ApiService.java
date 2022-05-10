package com.lw.lemonweather.api;

import com.lw.lemonweather.bean.TodayResponse;
import com.lw.lemonweather.bean.WeatherForecastResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("")
    Call<TodayResponse> getTodayWeather(@Query("location") String location);

    @GET("")
    Call<WeatherForecastResponse> getWeatherForecast(@Query("location") String location);
}
