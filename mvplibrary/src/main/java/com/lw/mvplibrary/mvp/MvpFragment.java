package com.lw.mvplibrary.mvp;

import android.os.Bundle;

import com.lw.mvplibrary.base.BaseFragment;
import com.lw.mvplibrary.base.BasePresenter;
import com.lw.mvplibrary.base.BaseView;

/**
 * 适用于需要访问网络接口的Fragment
 * @param <P>
 */
public abstract class MvpFragment<P extends BasePresenter> extends BaseFragment {
    protected P mPresent;

    protected abstract P createPresent();

    @Override
    public void initBeforeView(Bundle savedInstanceState) {
        mPresent = createPresent();
        mPresent.attach((BaseView) this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mPresent != null) {
            mPresent.detach((BaseView) this);
        }
    }
}
