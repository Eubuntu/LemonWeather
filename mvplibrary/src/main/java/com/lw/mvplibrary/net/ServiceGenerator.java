package com.lw.mvplibrary.net;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {
    //https://free-api.heweather.net/s6/weather/now?key=3086e91d66c04ce588a7f538f917c7f4&location=深圳
    //将上方的API接口地址进行拆分得到不变的一部分,实际开发中可以将这一部分作为服务器的ip访问地址

    //https://devapi.qweather.com/v7/weather/now?[请求参数]
    // key
    //用户认证key，请参考如何获取你的KEY。支持数字签名方式进行认证。例如 key=123456789ABC
    //location
    //需要查询地区的LocationID或以英文逗号分隔的经度,纬度坐标（十进制，最多支持小数点后两位），LocationID可通过城市搜索服务获取。例如 location=101010100 或 location=116.41,39.92
    //开发版https://devapi.qweather.com/v7/weather/now?location=101010100&key=你的KEY
    public static String BASE_URL = "https://free-api.heweather.net";

    //创建服务  参数就是API服务
    public static <T> T createService(Class<T> serviceClass) {

        //创建OkHttpClient构建器对象
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();

        //设置请求超时的时间，这里是10秒
        okHttpClientBuilder.connectTimeout(10000, TimeUnit.MILLISECONDS);

        //消息拦截器  因为有时候接口不同在排错的时候 需要先从接口的响应中做分析。利用了消息拦截器可以清楚的看到接口返回的所有内容
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();

        //setlevel用来设置日志打印的级别，共包括了四个级别：NONE,BASIC,HEADER,BODY
        //BASEIC:请求/响应行
        //HEADER:请求/响应行 + 头
        //BODY:请求/响应航 + 头 + 体
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //为OkHttp添加消息拦截器
        okHttpClientBuilder.addInterceptor(httpLoggingInterceptor);

        //在Retrofit中设置httpclient
        Retrofit retrofit = new Retrofit.Builder().baseUrl(BASE_URL)//设置地址  就是上面的固定地址,如果你是本地访问的话，可以拼接上端口号  例如 +":8080"
                .addConverterFactory(GsonConverterFactory.create())//用Gson把服务端返回的json数据解析成实体
                .client(okHttpClientBuilder.build())//放入OKHttp，之前说过retrofit是对OkHttp的进一步封装
                .build();
        return retrofit.create(serviceClass);//返回这个创建好的API服务
    }
}