package com.hunty.widget.event;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangjianqiu on 2016/9/12.
 * 日K
 */
public class DayKHistoryEvent extends Event {

    private Context mContext;

    public DayKHistoryEvent(Context context) {
        this.mContext = context;
    }

    public void getObservableDayKHistory(String type) {
        Map<String, String> params = new HashMap();
        String interfaceHttpUrl = "";


    }
}
