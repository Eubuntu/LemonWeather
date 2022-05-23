package com.lw.lemonweather.ui;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.lw.lemonweather.R;
import com.lw.lemonweather.adapter.MoreDailyAdapter;
import com.lw.lemonweather.bean.DailyResponse;
import com.lw.lemonweather.contract.MoreDailyContract;
import com.lw.lemonweather.utils.CodeToStringUtils;
import com.lw.lemonweather.utils.Constant;
import com.lw.lemonweather.utils.DateUtils;
import com.lw.lemonweather.utils.RecyclerViewScrollHelper;
import com.lw.lemonweather.utils.StatusBarUtil;
import com.lw.lemonweather.utils.ToastUtils;
import com.lw.mvplibrary.mvp.MvpActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import retrofit2.Response;

/**
 * 更多天气预报
 */
public class MoreDailyActivity extends MvpActivity<MoreDailyContract.MoreDailyPresenter> implements MoreDailyContract.IMoreDailyView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.rv)
    RecyclerView rv;

    List<DailyResponse.DailyBean> mList = new ArrayList<>();//数据实体
    MoreDailyAdapter mAdapter;//适配器

    @Override
    public void initData(Bundle savedInstanceState) {
        StatusBarUtil.transparencyBar(context);//透明状态栏
        showLoadingDialog();
        initList();
        Back(toolbar);
    }

    /**
     * 初始化列表
     */
    private void initList() {
        mAdapter = new MoreDailyAdapter(R.layout.item_more_daily_list, mList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv.setLayoutManager(linearLayoutManager);
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(rv);//滚动对齐，使RecyclerView像ViewPage一样，一次滑动一项,居中
        rv.setAdapter(mAdapter);
        tvTitle.setText(getIntent().getStringExtra("cityName"));
        mPresent.worldCity(getIntent().getStringExtra("locationId"));
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_more_daily;
    }

    @Override
    protected MoreDailyContract.MoreDailyPresenter createPresent() {
        return new MoreDailyContract.MoreDailyPresenter();
    }

    /**
     * 更多预报天气返回值
     *
     * @param response
     */
    @Override
    public void getMoreDailyResult(Response<DailyResponse> response) {
        dismissLoadingDialog();
        if (response.body().getCode().equals(Constant.SUCCESS_CODE)) {
            List<DailyResponse.DailyBean> data = response.body().getDaily();
            if (data != null && data.size() > 0) {//判空处理
                mList.clear();//添加数据之前先清除
                mList.addAll(data);//添加数据
                mAdapter.notifyDataSetChanged();//刷新列表

                for (int i = 0; i < data.size(); i++) {
                    if(data.get(i).getFxDate().equals(DateUtils.getNowDate())){
                        RecyclerViewScrollHelper.scrollToPosition(rv,i);//渲染完成后，定位到今天，因为和风天气预报有时候包括了昨天，有时候又不包括，搞得我很被动
                    }
                }

            } else {
                ToastUtils.showShortToast(context, "天气预报数据为空");
            }
        } else {//异常状态码返回
            ToastUtils.showShortToast(context, CodeToStringUtils.WeatherCode(response.body().getCode()));
        }
    }

    /**
     * 其他异常返回
     */
    @Override
    public void getDataFailed() {
        dismissLoadingDialog();
        ToastUtils.showShortToast(context, "更多天气数据获取异常");
    }

}