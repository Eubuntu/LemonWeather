package com.lw.lemonweather.ui;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lw.lemonweather.R;
import com.lw.lemonweather.adapter.CommonlyCityAdapter;
import com.lw.lemonweather.bean.NewSearchCityResponse;
import com.lw.lemonweather.bean.SearchCityResponse;
import com.lw.lemonweather.contract.SearchCityContract;
import com.lw.lemonweather.eventbus.SearchCityEvent;
import com.lw.lemonweather.utils.Constant;
import com.lw.lemonweather.utils.SPUtils;
import com.lw.lemonweather.utils.ToastUtils;
import com.lw.mvplibrary.bean.ResidentCity;
import com.lw.mvplibrary.mvp.MvpActivity;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

public class CommonlyUsedCityActivity extends MvpActivity<SearchCityContract.SearchCityPresenter> implements SearchCityContract.ISearchCityView {

    private androidx.appcompat.widget.Toolbar toolbar;
    private android.widget.EditText editQuery;
    private android.widget.ImageView ivClearSearch;
    private android.widget.LinearLayout layNormal;
    private androidx.recyclerview.widget.RecyclerView rvCommonlyUsed;
    private androidx.recyclerview.widget.RecyclerView rvSearch;

    //常用城市适配器
    private CommonlyCityAdapter mAdapter;
    //数据源
    private List<SearchCityResponse.BasicBean> mList = new ArrayList<>();
    //搜索城市列表适配器
    private CommonlyCityAdapter mAdapterAdd;
    //常用城市列表
    private List<ResidentCity> cityList;

    /**
     * @param response
     */
    @Override
    public void getSearchCityResult(Response<SearchCityResponse> response) {

    }

    /**
     * @param response
     */
    @Override
    public void getNewSearchCityResult(Response<NewSearchCityResponse> response) {

    }

    /**
     * 网络异常返回处理
     */
    @Override
    public void getDataFailed() {
        //关闭弹窗
        dismissLoadingDialog();
        ToastUtils.showShortToast(context, "网络异常");
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_commonly_used_city;
    }

    @Override
    protected SearchCityContract.SearchCityPresenter createPresent() {
        return new SearchCityContract.SearchCityPresenter();
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        editQuery = findViewById(R.id.edit_query);
        ivClearSearch = findViewById(R.id.iv_clear_search);
        layNormal = findViewById(R.id.lay_normal);
        rvCommonlyUsed = findViewById(R.id.rv_commonly_used);
        rvSearch = findViewById(R.id.rv_search);
    }

    private void initCityList(){
        //查询ResidentCity表中所有数据
        cityList = LitePal.findAll(ResidentCity.class);
        if (cityList.size()>0&&cityList!=null){
            mAdapter = new CommonlyCityAdapter(R.layout.item_commonly_city_list,cityList);
            rvCommonlyUsed.setLayoutManager(new LinearLayoutManager(context));
            rvCommonlyUsed.setAdapter(mAdapter);
            mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    switch (view.getId()){
                        case R.id.tv_city_name:
                            SPUtils.putString(Constant.LOCATION,cityList.get(position).getLocation(),context);
                            //发送消息
                            EventBus.getDefault().post(new SearchCityEvent(cityList.get(position).getLocation(),cityList.get(position).getParent_city()));
                            finish();
                            break;
                        //删除
                        case R.id.btn_delete:
                            //删除指定id
                            LitePal.delete(ResidentCity.class,cityList.get(position).getId());
                            initCityList();
                            //删除数据后判断显示或隐藏的控件
                            initHideOrShow();
                            break;
                    }
                }
            });
            mAdapter.notifyDataSetChanged();
        }else {
            rvCommonlyUsed.setVisibility(View.GONE);
            layNormal.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 根据常用城市数据进行页面控件的显示与隐藏
     */
    private void initHideOrShow() {
        //隐藏清除输入框内容的图标
        ivClearSearch.setVisibility(View.GONE);
        //隐藏搜索结果列表
        rvSearch.setVisibility(View.GONE);
        //有数据
        if (cityList != null&&cityList.size()>0) {
            //显示常用城市列表
            rvCommonlyUsed.setVisibility(View.VISIBLE);
            //隐藏没有数据时的布局
            layNormal.setVisibility(View.GONE);
        }else {
            //隐藏常用城市列表
            rvCommonlyUsed.setVisibility(View.GONE);
            //显示没有数据时的布局
            layNormal.setVisibility(View.VISIBLE);
        }
    }

}