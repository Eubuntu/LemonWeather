package com.lw.lemonweather.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lw.lemonweather.R;
import com.lw.lemonweather.bean.NewSearchCityResponse;

import java.util.List;

/**
 * 搜索城市结果列表适配器  V7
 */
public class SearchCityAdapter extends BaseQuickAdapter<NewSearchCityResponse.LocationBean, BaseViewHolder> {
    public SearchCityAdapter(int layoutResId, @Nullable List<NewSearchCityResponse.LocationBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, NewSearchCityResponse.LocationBean item) {
        helper.setText(R.id.tv_city_name, item.getName());
        //绑定点击事件
        helper.addOnClickListener(R.id.tv_city_name);

    }
}

