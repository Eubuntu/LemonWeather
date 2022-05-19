package com.lw.lemonweather.contract;

import android.content.Context;

import com.lw.lemonweather.api.ApiService;
import com.lw.lemonweather.bean.NewSearchCityResponse;
import com.lw.lemonweather.bean.SearchCityResponse;
import com.lw.mvplibrary.base.BasePresenter;
import com.lw.mvplibrary.base.BaseView;
import com.lw.mvplibrary.net.NetCallBack;
import com.lw.mvplibrary.net.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Response;

/**
 *  城市搜索订阅器 V7
 */
public class SearchCityContract {
    public static class SearchCityPresenter extends BasePresenter<ISearchCityView> {

        /**
         * 搜索城市 V7版本 模糊搜索 返回10条相关数据
         * @param location
         */
        public void newSearchCity(String location){
            ApiService service = ServiceGenerator.createService(ApiService.class,4);
            service.newSearchCity(location,"fuzzy").enqueue(new NetCallBack<NewSearchCityResponse>() {
                @Override
                public void onSuccess(Call<NewSearchCityResponse> call, Response<NewSearchCityResponse> response) {
                    if (getView() != null) {
                        getView().getNewSearchCityResult(response);
                    }
                }

                @Override
                public void onFailed() {
                    if (getView() != null) {
                        getView().getDataFailed();
                    }
                }
            });
        }

    }
    public interface ISearchCityView extends BaseView{
        //搜索城市返回数据 V7
        void getNewSearchCityResult(Response<NewSearchCityResponse> response);
        //错误返回
        void getDataFailed();
    }
}
