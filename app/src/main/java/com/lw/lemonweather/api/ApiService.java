package com.lw.lemonweather.api;

import com.lw.lemonweather.bean.AirNowResponse;
import com.lw.lemonweather.bean.BiYingImgResponse;
import com.lw.lemonweather.bean.DailyResponse;
import com.lw.lemonweather.bean.HourlyResponse;
import com.lw.lemonweather.bean.LifestyleResponse;
import com.lw.lemonweather.bean.NewSearchCityResponse;
import com.lw.lemonweather.bean.NowResponse;
import com.lw.lemonweather.bean.SearchCityResponse;
import com.lw.lemonweather.bean.WarningResponse;
import com.lw.lemonweather.bean.WorldCityResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * API服务接口
 */
public interface ApiService {

    /**
     * 实况天气
     * @param location 城市名
     * @return 返回实况天气数据
     */
    @GET("/v7/weather/now?key=806072ffa95d4a5897fcbcf1b5866452&gzip=n")
    Call<NowResponse> nowWeather(@Query("location") String location);

    /**
     * 天气预报 开发者最多可以获取15天数据，动态请求数据
     * @param type 天数类型 传入3d / 7d / 10d / 15d 通过path拼接到请求的url中
     * @param location 城市名
     * @return 返回天气预报数据
     */
    @GET("/v7/weather/{type}?key=806072ffa95d4a5897fcbcf1b5866452")
    Call<DailyResponse> dailyWeather(@Path("type") String type, @Query("location") String location);

    /**
     * 逐小时预报（未来24小时）
     * @param location 城市名
     * @return 返回小时数据
     */
    @GET("/v7/weather/24h?key=806072ffa95d4a5897fcbcf1b5866452")
    Call<HourlyResponse> hourlyWeather(@Query("location") String location);

    /**
     * 当天空气质量
     * @param location 城市名
     * @return 返回当天空气质量数据
     */
    @GET("/v7/air/now?key=806072ffa95d4a5897fcbcf1b5866452")
    Call<AirNowResponse> airNowWeather(@Query("location") String location);

    /**
     * 生活指数
     * @param type 可以控制定向获取那几项数据 全部数据 0, 运动指数	1 ，洗车指数	2 ，穿衣指数	3 ，
     *             钓鱼指数	4 ，紫外线指数  5 ，旅游指数  6，花粉过敏指数	7，舒适度指数	8，
     *             感冒指数	9 ，空气污染扩散条件指数	10 ，空调开启指数	 11 ，太阳镜指数	12 ，
     *             化妆指数  13 ，晾晒指数  14 ，交通指数  15 ，防晒指数	16
     * @param location 城市名
     * @return 返回当前生活指数数据
     */
    @GET("/v7/indices/1d?key=806072ffa95d4a5897fcbcf1b5866452")
    Call<LifestyleResponse> lifestyle(@Query("type") String type, @Query("location") String location);

    /**
     * 搜索城市 V7版本 模糊搜索，国内范围 返回10条数据
     * @param location 城市名
     * @param mode exact精准搜索 fuzzy 模糊搜索
     * @return
     */
    @GET("/v2/city/lookup?key=806072ffa95d4a5897fcbcf1b5866452=cn")
    Call<NewSearchCityResponse> newSearchCity(@Query("location") String location, @Query("mode") String mode);

    /**
     * 必应 每日一图
     * @return
     */
    @GET("HPImageArchive.aspx?format=js&idx=0&n=1")
    Call<BiYingImgResponse> biying();

/*    @GET("/find?key=806072ffa95d4a5897fcbcf1b5866452&group=cn&number=10")
    Call<SearchCityResponse> searchCity(@Query("location") String location);*/

    /**
     * 世界城市
     *
     * @param range cn表示国内  world 表示全世界
     * @return 返回热门城市数据 遗憾的是，现在最多只能返回20条，
     * 之前是有50条的，后面只能想别的办法来做海外热门城市了，目前就先这样了
     */
    @GET("/v2/city/top?key=806072ffa95d4a5897fcbcf1b5866452&number=20")
    Call<WorldCityResponse> worldCity(@Query("range") String range);

    /**
     * 当前城市灾害预警
     * @param location  城市id ，通过搜索城市获得
     * @return
     */
    @GET("/v7/warning/now?key=806072ffa95d4a5897fcbcf1b5866452")
    Call<WarningResponse> nowWarn(@Query("location") String location);

}
