package com.lw.lemonweather.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lw.lemonweather.R;
import com.lw.lemonweather.bean.SearchCityResponse;

import java.util.List;

public class SearchCityAdapter extends BaseQuickAdapter<SearchCityResponse.BasicBean, BaseViewHolder> {
    public SearchCityAdapter(int layoutResId, @Nullable List<SearchCityResponse.BasicBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, SearchCityResponse.BasicBean item) {
        helper.setText(R.id.tv_city_name, item.getLocation());
        helper.addOnClickListener(R.id.tv_city_name);
    }
}
