package com.lw.lemonweather.adapter;

import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.lw.lemonweather.R;
import com.lw.lemonweather.bean.TodayDetailBean;

import java.util.List;

/**
 * 地图天气中 今日天气详情数据
 */
public class TodayDetailAdapter extends BaseQuickAdapter<TodayDetailBean, BaseViewHolder> {
    public TodayDetailAdapter(int layoutResId, @Nullable List<TodayDetailBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TodayDetailBean item) {
        ImageView imageView = helper.getView(R.id.iv_icon);
        //图标
        imageView.setImageResource(item.getIcon());
        //值//名称
        helper.setText(R.id.tv_value, item.getValue())
                .setText(R.id.tv_name, item.getName());
    }
}
