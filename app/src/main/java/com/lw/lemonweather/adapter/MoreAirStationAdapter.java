package com.lw.lemonweather.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lw.lemonweather.R;
import com.lw.lemonweather.bean.AirNowResponse;

import java.util.List;

/**
 * 更多空气质量之空气站点列表适配器
 */
public class MoreAirStationAdapter extends BaseQuickAdapter<AirNowResponse.StationBean, BaseViewHolder> {
    public MoreAirStationAdapter(int layoutResId, @Nullable List<AirNowResponse.StationBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AirNowResponse.StationBean item) {
        //监测站名称 空气质量 空气质量指数 污染物 pm10 pm2.5 二氧化氮 二氧化硫 臭氧 一氧化碳
        helper.setText(R.id.tv_station_name, item.getName())
                .setText(R.id.tv_air_category, item.getCategory())
                .setText(R.id.tv_aqi, item.getAqi())
                .setText(R.id.tv_primary, item.getPrimary().equals("NA") ? "无污染" : item.getPrimary())
                .setText(R.id.tv_pm10, item.getPm10())
                .setText(R.id.tv_pm25, item.getPm2p5())
                .setText(R.id.tv_no2, item.getNo2())
                .setText(R.id.tv_so2, item.getSo2())
                .setText(R.id.tv_o3, item.getO3())
                .setText(R.id.tv_co, item.getCo());
    }
}
