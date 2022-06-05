package com.lw.lemonweather.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lw.lemonweather.R;
import com.lw.lemonweather.bean.MoreAirFiveResponse;
import com.lw.lemonweather.utils.DateUtils;

import java.util.List;

/**
 * 5天空气质量预报适配器
 */
public class MoreAirFiveAdapter extends BaseQuickAdapter<MoreAirFiveResponse.DailyBean, BaseViewHolder> {
    public MoreAirFiveAdapter(int layoutResId, @Nullable List<MoreAirFiveResponse.DailyBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, MoreAirFiveResponse.DailyBean item) {
        //日期描述 日期 空气质量指数 空气质量描述 污染物
        helper.setText(R.id.tv_date_info, DateUtils.Week(item.getFxDate()))
                .setText(R.id.tv_date, DateUtils.dateSplit(item.getFxDate()))
                .setText(R.id.tv_aqi, item.getAqi())
                .setText(R.id.tv_category, item.getCategory())
                .setText(R.id.tv_primary, item.getPrimary().equals("NA") ? "无污染" : item.getPrimary());
    }
}
