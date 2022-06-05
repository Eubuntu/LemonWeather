package com.lw.lemonweather.ui;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.lw.lemonweather.R;
import com.lw.lemonweather.adapter.CommonlyCityAdapter;
import com.lw.lemonweather.adapter.CommonlyCityAddAdapter;
import com.lw.lemonweather.bean.NewSearchCityResponse;
import com.lw.lemonweather.contract.SearchCityContract;
import com.lw.lemonweather.eventbus.SearchCityEvent;
import com.lw.lemonweather.utils.CodeToStringUtils;
import com.lw.lemonweather.utils.Constant;
import com.lw.lemonweather.utils.SPUtils;
import com.lw.lemonweather.utils.StatusBarUtil;
import com.lw.lemonweather.utils.ToastUtils;
import com.lw.mvplibrary.bean.ResidentCity;
import com.lw.mvplibrary.mvp.MvpActivity;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Response;

/**
 * 常用城市
 */
public class CommonlyUsedCityActivity extends MvpActivity<SearchCityContract.SearchCityPresenter>
        implements SearchCityContract.ISearchCityView {

    /**
     * 输入框
     */
    @BindView(R.id.edit_query)
    EditText editQuery;
    /**
     * 清除输入框内容的图标
     */
    @BindView(R.id.iv_clear_search)
    ImageView ivClearSearch;
    /**
     * 标题控件
     */
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    /**
     * 常用城市列表
     */
    @BindView(R.id.rv_commonly_used)
    RecyclerView rvCommonlyUsed;
    /**
     * 搜索城市列表
     */
    @BindView(R.id.rv_search)
    RecyclerView rvSearch;
    /**
     * 常用城市为空时展示的布局
     */
    @BindView(R.id.lay_normal)
    LinearLayout layNormal;
    /**
     * 常用城市列表适配器
     */
    CommonlyCityAdapter mAdapter;
    List<NewSearchCityResponse.LocationBean> mList = new ArrayList<>();
    /**
     * 搜索城市列表适配器
     */
    CommonlyCityAddAdapter mAdapterAdd;
    /**
     * 常用城市列表
     */
    List<ResidentCity> cityList;

    @Override
    public void initData(Bundle savedInstanceState) {
        //白色状态栏
        StatusBarUtil.setStatusBarColor(context, R.color.white);
        //黑色字体
        StatusBarUtil.StatusBarLightMode(context);
        Back(toolbar);
        //初始化常用城市列表
        initCityList();
        //初始化搜索城市列表
        initQueryAddList();
        //初始化输入框
        initEdit();
    }

    /**
     * 初始化搜索要添加的城市列表
     */
    private void initQueryAddList() {
        mAdapterAdd = new CommonlyCityAddAdapter(R.layout.item_commonly_city_add_list, mList);
        rvSearch.setLayoutManager(new LinearLayoutManager(context));
        rvSearch.setAdapter(mAdapterAdd);
        //点击item时保存到数据库中，同时传递数据到主页面查询出天气
        mAdapterAdd.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                QueryWeather(position);
            }
        });
    }

    /**
     * 添加城市列表item，点击保存数据并发送事件
     *
     * @param position
     */
    private void QueryWeather(int position) {
        ResidentCity residentCity = new ResidentCity();
        //地区／城市名称
        residentCity.setLocation(mList.get(position).getName());
        //该地区／城市的上级城市
        residentCity.setParent_city(mList.get(position).getAdm2());
        //该地区／城市所属行政区域
        residentCity.setAdmin_area(mList.get(position).getAdm1());
        //该地区／城市所属国家名称
        residentCity.setCnty(mList.get(position).getCountry());
        //保存数据到数据库中
        residentCity.save();
        if (residentCity.save()) {
            //保存成功
            //然后使用之前在搜索城市天气中写好的代码
            SPUtils.putString(Constant.LOCATION, mList.get(position).getName(), context);
            //发送消息
            EventBus.getDefault().post(new SearchCityEvent(mList.get(position).getName(), mList.get(position).getAdm2()));
            finish();
        } else {
            //保存失败
            ToastUtils.showShortToast(context, "添加城市失败");
        }
    }

    /**
     * 初始化搜索输入框 ,输入后马上查询数据，不需要额外点击，同时查询到数据之后隐藏默认城市列表
     */
    private void initEdit() {
        editQuery.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals("")) {
                    //输入后，显示清除按钮
                    ivClearSearch.setVisibility(View.VISIBLE);
                    mAdapterAdd.changTxColor(s.toString());
//                  mPresent.searchCity(context, s.toString());//开始搜索
                    //搜索城市  V7  模糊搜索返回十条数据
                    mPresent.newSearchCity(s.toString());
                } else {
                    //隐藏和显示控件
                    initHideOrShow();
                }
            }
        });
    }

    /**
     * 根据常用城市数据来进行页面控件显示/隐藏
     */
    private void initHideOrShow() {
        //隐藏清除输入框内容的图标
        ivClearSearch.setVisibility(View.GONE);
        //隐藏搜索结果列表
        rvSearch.setVisibility(View.GONE);
        if (cityList != null && cityList.size() > 0) {
            //有数据 显示常用城市列表
            rvCommonlyUsed.setVisibility(View.VISIBLE);
            //隐藏没有数据时的布局
            layNormal.setVisibility(View.GONE);
        } else {
            //没数据 隐藏常用城市列表
            rvCommonlyUsed.setVisibility(View.GONE);
            //显示没有数据时的布局
            layNormal.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 初始化常用城市列表数据
     */
    private void initCityList() {
        //查询ResidentCity表中所有数据
        cityList = LitePal.findAll(ResidentCity.class);
        if (cityList.size() > 0 && cityList != null) {
            mAdapter = new CommonlyCityAdapter(R.layout.item_commonly_city_list, cityList);
            rvCommonlyUsed.setLayoutManager(new LinearLayoutManager(context));
            rvCommonlyUsed.setAdapter(mAdapter);
            mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
                @Override
                public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                    switch (view.getId()) {
                        case R.id.tv_city_name:
                            SPUtils.putString(Constant.LOCATION, cityList.get(position).getLocation(), context);
                            //发送消息
                            EventBus.getDefault().post(new SearchCityEvent(cityList.get(position).getLocation(), cityList.get(position).getParent_city()));
                            finish();
                            break;
                        //删除
                        case R.id.btn_delete:
                            //删除指定id
                            LitePal.delete(ResidentCity.class, cityList.get(position).getId());
                            initCityList();
                            //删除数据后判断一下显示和隐藏的控件
                            initHideOrShow();
                            break;
                    }

                }
            });
            mAdapter.notifyDataSetChanged();
        } else {
            rvCommonlyUsed.setVisibility(View.GONE);
            layNormal.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_commonly_used_city;
    }

    @Override
    protected SearchCityContract.SearchCityPresenter createPresent() {
        return new SearchCityContract.SearchCityPresenter();
    }

    /**
     * 搜索城市天气 V7
     *
     * @param response
     */
    @Override
    public void getNewSearchCityResult(Response<NewSearchCityResponse> response) {
        dismissLoadingDialog();
        if (response.body().getCode().equals(Constant.SUCCESS_CODE)) {
            List<NewSearchCityResponse.LocationBean> data = response.body().getLocation();
            if (data != null && data.size() > 0) {
                //隐藏常用城市列表
                rvCommonlyUsed.setVisibility(View.GONE);
                mList.clear();
                mList.addAll(response.body().getLocation());
                mAdapterAdd.notifyDataSetChanged();
                //显示搜索城市列表
                rvSearch.setVisibility(View.VISIBLE);
                layNormal.setVisibility(View.GONE);
            } else {
                ToastUtils.showShortToast(context, "没有找到相关城市");
            }
        } else {
            ToastUtils.showShortToast(context, CodeToStringUtils.WeatherCode(response.body().getCode()));
        }
    }

    /**
     * 网络异常返回处理
     */
    @Override
    public void getDataFailed() {
        //关闭弹窗
        dismissLoadingDialog();
        //这里的context是框架中封装好的，等同于this
        ToastUtils.showShortToast(context, "网络异常");
    }

    @OnClick(R.id.iv_clear_search)
    public void onViewClicked() {
        //置为空
        editQuery.setText("");
        initHideOrShow();
    }

}
