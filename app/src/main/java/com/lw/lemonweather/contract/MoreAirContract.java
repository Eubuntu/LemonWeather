package com.lw.lemonweather.contract;

import com.lw.lemonweather.api.ApiService;
import com.lw.lemonweather.bean.AirNowResponse;
import com.lw.lemonweather.bean.MoreAirFiveResponse;
import com.lw.lemonweather.bean.NewSearchCityResponse;
import com.lw.mvplibrary.base.BasePresenter;
import com.lw.mvplibrary.base.BaseView;
import com.lw.mvplibrary.net.NetCallBack;
import com.lw.mvplibrary.net.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Response;

/**
 * 更多空气质量数据订阅器
 */
public class MoreAirContract {

    public static class MoreAirPresenter extends BasePresenter<IMoreAirView> {

        /**
         * 搜索城市  搜索站点的城市id，用于查询空气质量
         *
         * @param location 城市名
         */
        public void searchCityId(String location) {//注意这里的4表示新的搜索城市地址接口
            ApiService service = ServiceGenerator.createService(ApiService.class, 4);//指明访问的地址
            service.newSearchCity(location, "exact").enqueue(new NetCallBack<NewSearchCityResponse>() {
                @Override
                public void onSuccess(Call<NewSearchCityResponse> call, Response<NewSearchCityResponse> response) {
                    if (getView() != null) {
                        getView().getSearchCityIdResult(response);
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

        /**
         * 空气质量  V7
         *
         * @param location 城市id
         */
        public void air(String location) {
            ApiService service = ServiceGenerator.createService(ApiService.class, 3);
            service.airNowWeather(location).enqueue(new NetCallBack<AirNowResponse>() {
                @Override
                public void onSuccess(Call<AirNowResponse> call, Response<AirNowResponse> response) {
                    if (getView() != null) {
                        getView().getMoreAirResult(response);
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

        /**
         * 五天空气质量数据  V7
         *
         * @param location 城市id
         */
        public void airFive(String location) {
            ApiService service = ServiceGenerator.createService(ApiService.class, 3);
            service.airFiveWeather(location).enqueue(new NetCallBack<MoreAirFiveResponse>() {
                @Override
                public void onSuccess(Call<MoreAirFiveResponse> call, Response<MoreAirFiveResponse> response) {
                    if (getView() != null) {
                        getView().getMoreAirFiveResult(response);
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

    public interface IMoreAirView extends BaseView {

        /**
         * 搜索城市Id
         *
         * @param response
         */
        void getSearchCityIdResult(Response<NewSearchCityResponse> response);

        /**
         * 空气质量返回数据 V7
         *
         * @param response
         */
        void getMoreAirResult(Response<AirNowResponse> response);

        /**
         * 五天空气质量数据返回 V7
         *
         * @param response
         */
        void getMoreAirFiveResult(Response<MoreAirFiveResponse> response);

        /**
         * 错误返回
         */
        void getDataFailed();

    }
}
