package com.lw.lemonweather.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;


import com.lw.lemonweather.R;
import com.lw.lemonweather.utils.APKVersionInfoUtils;
import com.lw.lemonweather.utils.StatusBarUtil;
import com.lw.lemonweather.utils.ToastUtils;
import com.lw.mvplibrary.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 关于 Weather
 */
public class AboutUsActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    //版本
    @BindView(R.id.tv_version)
    TextView tvVersion;
    //博客
    @BindView(R.id.tv_blog)
    TextView tvBlog;
    //源码
    @BindView(R.id.tv_code)
    TextView tvCode;
    //复制邮箱
    @BindView(R.id.tv_copy_email)
    TextView tvCopyEmail;
    //作者
    @BindView(R.id.tv_author)
    TextView tvAuthor;
    private ClipboardManager myClipboard;
    private ClipData myClip;

    @Override
    public void initData(Bundle savedInstanceState) {
        Back(toolbar);
        //白色状态栏
        StatusBarUtil.setStatusBarColor(context, com.lw.mvplibrary.R.color.about_bg_color);
        tvVersion.setText(APKVersionInfoUtils.getVerName(context));
        //下划线
        tvCopyEmail.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        //抗锯齿
        tvCopyEmail.getPaint().setAntiAlias(true);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_about_us;
    }

    @OnClick({R.id.tv_blog, R.id.tv_code, R.id.tv_copy_email,R.id.tv_author})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //博客地址
            case R.id.tv_blog:
                //jumpUrl(");
                break;
            //源码地址
            case R.id.tv_code:
                //jumpUrl("");
                break;
            //复制邮箱
            case R.id.tv_copy_email:
                myClipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
                myClip = ClipData.newPlainText("text", "123@qq.com");
                myClipboard.setPrimaryClip(myClip);
                ToastUtils.showShortToast(context,"邮箱已复制");
                break;
            case R.id.tv_author:
                ToastUtils.showShortToast(context,"你为啥要点我呢？");
                break;
        }
    }

    /**
     * 跳转URL
     * @param url 地址
     */
    private void jumpUrl(String url){
        if(url!=null){
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        }else{
            ToastUtils.showShortToast(context,"未找到相关地址");
        }
    }
}
