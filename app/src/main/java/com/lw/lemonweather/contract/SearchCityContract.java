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
 *  城市搜索订阅器
 */
public class SearchCityContract {
    public static class SearchCityPresenter extends BasePresenter<ISearchCityView> {

        public void searchCity(final Context context, String location) {
            ApiService service = ServiceGenerator.createService(ApiService.class, 2);
            service.searchCity(location).enqueue(new NetCallBack<SearchCityResponse>() {
                @Override
                public void onSuccess(Call<SearchCityResponse> call, Response<SearchCityResponse> response) {
                    if (getView() != null) {
                        getView().getSearchCityResult(response);
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
        void getSearchCityResult(Response<SearchCityResponse> response);

        void getNewSearchCityResult(Response<NewSearchCityResponse> response);

        void getDataFailed();

    }
}
