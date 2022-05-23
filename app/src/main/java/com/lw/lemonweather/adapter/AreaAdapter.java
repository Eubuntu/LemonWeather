package com.lw.lemonweather.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lw.lemonweather.R;
import com.lw.lemonweather.bean.CityResponse;

import java.util.List;

/**
 * 区/县列表适配器
 */
public class AreaAdapter extends BaseQuickAdapter<CityResponse.CityBean.AreaBean, BaseViewHolder> {
    public AreaAdapter(int layoutResId, @Nullable List<CityResponse.CityBean.AreaBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CityResponse.CityBean.AreaBean item) {
        helper.setText(R.id.tv_city,item.getName());
        helper.addOnClickListener(R.id.item_city);
    }
}
