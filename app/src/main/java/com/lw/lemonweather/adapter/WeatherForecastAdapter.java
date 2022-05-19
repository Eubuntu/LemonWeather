package com.lw.lemonweather.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lw.lemonweather.R;
import com.lw.lemonweather.bean.WeatherForecastResponse;

import java.util.List;

/**
 * 天气预报列表展示适配器
 */
public class WeatherForecastAdapter extends BaseQuickAdapter<WeatherForecastResponse.HeWeather6Bean.DailyForecastBean, BaseViewHolder> {
    public WeatherForecastAdapter(int layoutResId, List<WeatherForecastResponse.HeWeather6Bean.DailyForecastBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, WeatherForecastResponse.HeWeather6Bean.DailyForecastBean item) {
        helper.setText(R.id.tv_date, item.getDate()).setText(R.id.tv_info, item.getCond_txt_d()).setText(R.id.tv_low_height, item.getTmp_min() + "/" + item.getTmp_max() + "°c");
    }
}
