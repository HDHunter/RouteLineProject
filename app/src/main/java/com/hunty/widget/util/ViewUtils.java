package com.hunty.widget.util;

import android.content.Context;

import com.hunty.widget.HlApplication;


public class ViewUtils {

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        if (context == null) context = HlApplication.getContext();
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(float dpValue) {
        return dip2px(null, dpValue);
    }

    public static float sp2px(float sp) {
        final float scale = HlApplication.getContext().getResources().getDisplayMetrics().scaledDensity;
        return sp * scale;
    }


    public static int getScreenWidth() {
        return HlApplication.getContext().getResources().getDisplayMetrics().widthPixels;
    }
}
