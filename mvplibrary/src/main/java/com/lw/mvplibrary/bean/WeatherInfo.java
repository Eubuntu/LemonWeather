package com.lw.mvplibrary.bean;

import org.litepal.crud.LitePalSupport;

public class WeatherInfo extends LitePalSupport {
    /**
     * 定位城市
     */
    private String cityName;
    /**
     * 温度
     */
    private String temp;
    /**
     * 最高温度
     */
    private int tempMax;
    /**
     * 最低温度
     */
    private int tempMin;
    /**
     * 天气状况
     */
    private String weatherInfoState;

    /**
     *天气状况图标码
     */
    private int weatherInfoStateCode;


    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public int getTempMax() {
        return tempMax;
    }

    public void setTempMax(int tempMax) {
        this.tempMax = tempMax;
    }

    public int getTempMin() {
        return tempMin;
    }

    public void setTempMin(int tempMin) {
        this.tempMin = tempMin;
    }

    public String getWeatherInfoState() {
        return weatherInfoState;
    }

    public void setWeatherInfoState(String weatherInfoState) {
        this.weatherInfoState = weatherInfoState;
    }

    public int getWeatherInfoStateCode() {
        return weatherInfoStateCode;
    }

    public void setWeatherInfoStateCode(int weatherInfoStateCode) {
        this.weatherInfoStateCode = weatherInfoStateCode;
    }

    @Override
    public String toString() {
        return "WeatherInfo{" +
                "cityName='" + cityName + '\'' +
                ", temp='" + temp + '\'' +
                ", tempMax=" + tempMax +
                ", tempMin=" + tempMin +
                ", weatherInfoState='" + weatherInfoState + '\'' +
                ", weatherInfoStateCode=" + weatherInfoStateCode +
                '}';
    }
}
