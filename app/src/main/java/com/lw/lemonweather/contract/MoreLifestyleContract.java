package com.lw.lemonweather.contract;

import com.lw.lemonweather.api.ApiService;
import com.lw.lemonweather.bean.LifestyleResponse;
import com.lw.mvplibrary.base.BasePresenter;
import com.lw.mvplibrary.base.BaseView;
import com.lw.mvplibrary.net.NetCallBack;
import com.lw.mvplibrary.net.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Response;

/**
 * 更多生活指数订阅器
 */
public class MoreLifestyleContract {

    public static class MoreLifestylePresenter extends BasePresenter<IMoreLifestyleView> {

        /**
         * 更多生活指数  V7
         * @param location  城市id
         */
        public void worldCity(String location) {
            ApiService service = ServiceGenerator.createService(ApiService.class,3);
            service.lifestyle("0",location).enqueue(new NetCallBack<LifestyleResponse>() {
                @Override
                public void onSuccess(Call<LifestyleResponse> call, Response<LifestyleResponse> response) {
                    if(getView() != null){
                        getView().getMoreLifestyleResult(response);
                    }
                }

                @Override
                public void onFailed() {
                    if(getView() != null){
                        getView().getDataFailed();
                    }
                }
            });
        }

    }

    public interface IMoreLifestyleView extends BaseView {

        //更多生活指数返回数据 V7
        void getMoreLifestyleResult(Response<LifestyleResponse> response);

        //错误返回
        void getDataFailed();
    }
}
