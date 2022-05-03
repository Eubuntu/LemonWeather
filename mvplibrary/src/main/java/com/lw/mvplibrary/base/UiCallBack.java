package com.lw.mvplibrary.base;

import android.os.Bundle;

public interface UiCallBack {

    //
    void initBeforeView(Bundle savedInstanceState);

    //
    void initData(Bundle savedInstanceState);

    //
    int getLayoutId();
}
