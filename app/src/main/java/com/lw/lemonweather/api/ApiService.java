package com.lw.lemonweather.api;

import com.lw.lemonweather.bean.TodayResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("")
    Call<TodayResponse> getTodayWeather(@Query("location") String location);
}
