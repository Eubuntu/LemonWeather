package com.lw.lemonweather.adapter;

import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lw.lemonweather.R;
import com.lw.lemonweather.bean.DailyResponse;
import com.lw.lemonweather.utils.WeatherUtil;

import java.util.List;

/**
 * V7 API 天气预报数据列表适配器
 */
public class DailyAdapter extends BaseQuickAdapter<DailyResponse.DailyBean, BaseViewHolder> {
    public DailyAdapter(int layoutResId, @Nullable List<DailyResponse.DailyBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, DailyResponse.DailyBean item) {
        //日期  最低温和最高温
        helper.setText(R.id.tv_date, item.getFxDate()).setText(R.id.tv_low_height, item.getTempMin() + "/" + item.getTempMax() + "℃");
        //天气状态图片
        ImageView weatherStateIcon = helper.getView(R.id.iv_weather_state);
        //获取天气状态码，根据状态码来显示图标
        int code = Integer.parseInt(item.getIconDay());
        //调用工具类中写好的方法
        WeatherUtil.changeIcon(weatherStateIcon,code);
        //绑定点击事件的id
        helper.addOnClickListener(R.id.item_forecast);
    }
}

