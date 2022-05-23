package com.lw.lemonweather.adapter;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lw.lemonweather.R;
import com.lw.lemonweather.bean.CityResponse;

import java.util.List;
/**
 * 市列表适配器
 */
public class CityAdapter extends BaseQuickAdapter<CityResponse.CityBean, BaseViewHolder> {


    public CityAdapter(int layoutResId, @Nullable List<CityResponse.CityBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CityResponse.CityBean item) {
        helper.setText(R.id.tv_city,item.getName());
        helper.addOnClickListener(R.id.item_city);
    }
}
