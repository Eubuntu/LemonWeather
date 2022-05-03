package com.lw.mvplibrary.base;

import java.lang.ref.WeakReference;

public class BasePersenter<V extends BaseView> {
    private WeakReference<V> mWeakReference;

    public void attach(V v) {
        mWeakReference = new WeakReference<V>(v);
    }

    public void detach(V v) {
        if (mWeakReference != null) {
            mWeakReference.clear();
            mWeakReference = null;
        }
    }

    public V getView() {
        if (mWeakReference != null) {
            return mWeakReference.get();
        }
        return null;
    }
}
