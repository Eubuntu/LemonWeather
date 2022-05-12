package com.lw.lemonweather.api;

import com.lw.lemonweather.bean.AirNowResponse;
import com.lw.lemonweather.bean.DailyResponse;
import com.lw.lemonweather.bean.HourlyResponse;
import com.lw.lemonweather.bean.LifestyleResponse;
import com.lw.lemonweather.bean.NewSearchCityResponse;
import com.lw.lemonweather.bean.NowResponse;
import com.lw.lemonweather.bean.TodayResponse;
import com.lw.lemonweather.bean.WeatherForecastResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @GET("")
    Call<TodayResponse> getTodayWeather(@Query("location") String location);

    @GET("")
    Call<WeatherForecastResponse> getWeatherForecast(@Query("location") String location);

    @GET("/v7/weather/now?key=806072ffa95d4a5897fcbcf1b5866452")
    Call<NowResponse> nowWeather(@Query("location") String location);

    @GET("/v7/weather/{type}?key=806072ffa95d4a5897fcbcf1b5866452")
    Call<DailyResponse> dailyWeather(@Path("type") String type, @Query("location") String location);

    @GET("/v7/weather/24h?key=806072ffa95d4a5897fcbcf1b5866452")
    Call<HourlyResponse> hourlyWeather(@Query("location") String location);

    @GET("/v7/air/now?key=806072ffa95d4a5897fcbcf1b5866452")
    Call<AirNowResponse> airNowWeather(@Query("location") String location);

    @GET("/v7/indices/1d?key=806072ffa95d4a5897fcbcf1b5866452")
    Call<LifestyleResponse> Lifestyle(@Query("type") String type, @Query("location") String location);

    @GET("/v7/city/lookup?key=806072ffa95d4a5897fcbcf1b5866452=cn")
    Call<NewSearchCityResponse> newSearchCity(@Query("location") String location, @Query("mode") String mode);
}
