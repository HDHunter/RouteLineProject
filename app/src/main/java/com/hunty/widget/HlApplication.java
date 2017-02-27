package com.hunty.widget;

import android.app.Application;
import android.content.Context;

/**
 * Created by zhangJianqiu on 2017/2/26 0026.
 */
public class HlApplication extends Application {

    private static Context context;

    public HlApplication() {
        context = this;
    }

    public static Context getContext() {
        return context;
    }
}
