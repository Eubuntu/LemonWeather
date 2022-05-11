package com.lw.mvplibrary.utils;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.lw.mvplibrary.R;

import java.util.HashMap;
import java.util.Map;

public class LiWindow {
    private LiWindow mLiWindow;
    private PopupWindow mPopupWindow;
    private LayoutInflater inflater;
    private View view;
    private Context mContext;
    private WindowManager show;
    WindowManager.LayoutParams context;
    private Map<String, Object> mMap = new HashMap<>();

    public Map<String, Object> getmMap() {
        return mMap;
    }

    public LiWindow(Context context) {
        this.mContext = context;
        inflater = LayoutInflater.from(context);
        mLiWindow = this;
    }

    public LiWindow(Context context, Map<String, Object> map) {
        this.mContext = context;
        this.mMap = map;
        inflater = LayoutInflater.from(context);
    }

    public void showRightPopupWindow(View mView) {
        mPopupWindow = new PopupWindow(mView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setContentView(mView);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setAnimationStyle(R.style.AnimationRightFade);
        mPopupWindow.showAtLocation(mView, Gravity.RIGHT, 0, 0);
        setBackgroundAlpha(0.5f, mContext);
        WindowManager.LayoutParams nomal = ((Activity) mContext).getWindow().getAttributes();
        nomal.alpha = 0.5f;
        ((Activity) mContext).getWindow().setAttributes(nomal);
        mPopupWindow.setOnDismissListener(closeDismiss);
    }

    public void showBottomPopupWindow(View mView) {
        mPopupWindow = new PopupWindow(mView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setContentView(mView);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setFocusable(true);
        mPopupWindow.setAnimationStyle(R.style.AnimationBottomFade);
        mPopupWindow.showAtLocation(mView, Gravity.BOTTOM, 0, 0);
        setBackgroundAlpha(0.5f, mContext);
        WindowManager.LayoutParams nomal = ((Activity) mContext).getWindow().getAttributes();
        nomal.alpha = 0.5f;
        ((Activity) mContext).getWindow().setAttributes(nomal);
        mPopupWindow.setOnDismissListener(closeDismiss);
    }


    private void setBackgroundAlpha(float bgAlpha, Context mContext) {
        WindowManager.LayoutParams lp = ((Activity) mContext).getWindow().getAttributes();
        lp.alpha = bgAlpha;
        ((Activity) mContext).getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        ((Activity) mContext).getWindow().setAttributes(lp);
    }

    public LiWindow setAnim(int animId) {
        if (mPopupWindow != null) {
            mPopupWindow.setAnimationStyle(animId);
        }
        return mLiWindow;
    }

    public PopupWindow.OnDismissListener closeDismiss = new PopupWindow.OnDismissListener() {
        @Override
        public void onDismiss() {
            WindowManager.LayoutParams nomal = ((Activity) mContext).getWindow().getAttributes();
            nomal.alpha = 1f;
            ((Activity) mContext).getWindow().setAttributes(nomal);
        }
    };

    public void closePopupWindow() {
        if (mPopupWindow != null) {
            mPopupWindow.dismiss();
        }
    }

}
