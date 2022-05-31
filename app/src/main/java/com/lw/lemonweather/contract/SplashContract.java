package com.lw.lemonweather.contract;

import com.lw.lemonweather.api.ApiService;
import com.lw.lemonweather.bean.BiYingImgResponse;
import com.lw.mvplibrary.base.BasePresenter;
import com.lw.mvplibrary.base.BaseView;
import com.lw.mvplibrary.net.NetCallBack;
import com.lw.mvplibrary.net.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Response;

public class SplashContract {

    public static class SplashPresenter extends BasePresenter<ISplashView> {
        /**
         * 获取必应  每日一图
         */
        public void biying() {
            ApiService service = ServiceGenerator.createService(ApiService.class, 1);
            service.biying().enqueue(new NetCallBack<BiYingImgResponse>() {
                @Override
                public void onSuccess(Call<BiYingImgResponse> call, Response<BiYingImgResponse> response) {
                    if (getView() != null) {
                        getView().getBiYingResult(response);
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

    public interface ISplashView extends BaseView {

        /**
         * 获取必应每日一图返回
         *
         * @param response BiYingImgResponse
         */
        void getBiYingResult(Response<BiYingImgResponse> response);

        //错误返回
        void getDataFailed();

    }

}
