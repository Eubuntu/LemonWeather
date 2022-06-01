package com.lw.lemonweather.adapter;

import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lw.lemonweather.R;
import com.lw.lemonweather.bean.DailyResponse;
import com.lw.lemonweather.utils.DateUtils;
import com.lw.lemonweather.utils.WeatherUtil;

import java.util.List;

/**
 * 地图天气中 未来七天的天气列表适配器
 */
public class SevenDailyAdapter extends BaseQuickAdapter<DailyResponse.DailyBean, BaseViewHolder> {
    public SevenDailyAdapter(int layoutResId, @Nullable List<DailyResponse.DailyBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DailyResponse.DailyBean item) {
        //日期//最高温//最低温
        helper.setText(R.id.tv_date, DateUtils.dateSplitPlus(item.getFxDate()) +
                DateUtils.Week(item.getFxDate()))
                .setText(R.id.tv_temp_height, item.getTempMax() + "℃")
                .setText(R.id.tv_temp_low, " / " + item.getTempMin() + "℃");
        //天气状态图片//获取天气状态码，根据状态码来显示图标//调用工具类中写好的方法
        ImageView weatherStateIcon = helper.getView(R.id.iv_weather_state);
        int code = Integer.parseInt(item.getIconDay());
        WeatherUtil.changeIcon(weatherStateIcon, code);
    }
}
