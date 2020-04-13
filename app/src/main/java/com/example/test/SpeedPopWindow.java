package com.example.test;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.test.R;

public class SpeedPopWindow extends PopupWindow {
    private static final String TAG = "SpeedPopWindow";
    public final View view;
    private Activity context;
    private View.OnClickListener itemClick;

    public SpeedPopWindow(Activity context, View.OnClickListener itemClick) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.speed_pop_menu, null);//alt+ctrl+f
        this.itemClick = itemClick;
        this.context = context;
        initView();
        initPopWindow();
    }


    private void initView() {
    }

    private void initPopWindow() {
        this.setContentView(view);
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int windowheight = dm.heightPixels;
        int windowwidth=dm.widthPixels;
        // 设置弹出窗体的宽
        this.setWidth((windowwidth/9)*4);
        // 设置弹出窗体的高
        this.setHeight((windowheight/5)*2);
        // 设置弹出窗体可点击()
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.popwin_anim_style);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x00FFFFFF);
        //设置弹出窗体的背景
        this.setBackgroundDrawable(dw);
        backgroundAlpha(context, 0.9f);//0.0-1.0
    }

    /**
     * 设置添加屏幕的背景透明度(值越大,透明度越高)
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(Activity context, float bgAlpha) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        context.getWindow().setAttributes(lp);
    }
}