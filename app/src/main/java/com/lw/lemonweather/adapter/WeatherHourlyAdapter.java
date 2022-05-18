package com.lw.lemonweather.adapter;

import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lw.lemonweather.R;
import com.lw.lemonweather.bean.HourlyResponse;
import com.lw.lemonweather.utils.DateUtils;
import com.lw.lemonweather.utils.WeatherUtil;

import java.util.List;

/**
 * 逐时天气适配器
 */
public class WeatherHourlyAdapter extends BaseQuickAdapter<HourlyResponse.HourlyBean, BaseViewHolder> {

    public WeatherHourlyAdapter(int layoutResId, @Nullable List<HourlyResponse.HourlyBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HourlyResponse.HourlyBean item) {
        //首先是对时间格式的处理,因为拿到的时间是  2021-04-28 22:00  要改成   晚上22:00
        //分两步，第一个是字符串的截取，第二个是时间段的判断返回文字描述
        /**
         * V7 API 涉及到时间的，都会返回 2021-07-16T09:39+08:00  这种格式
         * 所以最好写一个通用的返回进行处理 方法已经写好了使用可以了
         */
        String time = DateUtils.updateTime(item.getFxTime());
        //时间  温度
        helper.setText(R.id.tv_time, WeatherUtil.showTimeInfo(time) + time).setText(R.id.tv_temperature, item.getTemp() + "℃");
        //天气状态图片
        ImageView weatherStateIcon = helper.getView(R.id.iv_weather_state);
        //获取天气状态码，根据状态码来显示图标
        int code = Integer.parseInt(item.getIcon());
        WeatherUtil.changeIcon(weatherStateIcon, code);
        helper.addOnClickListener(R.id.item_hourly);
    }
}
