package com.lw.mvplibrary.mvp;

import android.os.Bundle;

import com.lw.mvplibrary.base.BaseActivity;
import com.lw.mvplibrary.base.BasePresenter;
import com.lw.mvplibrary.base.BaseView;

public abstract class MvpActivity<P extends BasePresenter> extends BaseActivity {
    protected P mPresent;

    protected abstract P createPresent();


    @Override
    public void initBeforeView(Bundle savedInstanceState) {
        mPresent = createPresent();
        mPresent.attach((BaseView) this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPresent.detach((BaseView) this);
    }
}
