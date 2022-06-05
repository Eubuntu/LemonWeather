package com.lw.lemonweather.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import com.lw.lemonweather.R;
import com.lw.mvplibrary.bean.Country;

import java.util.List;

/**
 * 国家列表适配器
 */
public class CountryAdapter extends BaseQuickAdapter<Country, BaseViewHolder> {
    public CountryAdapter(int layoutResId, @Nullable List<Country> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Country item) {
        helper.setText(R.id.tv_country_name, item.getName());
        helper.addOnClickListener(R.id.tv_country_name);
    }
}
