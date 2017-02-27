package com.hunty.widget.event;

import android.content.Context;
import android.text.TextUtils;


import java.util.HashMap;
import java.util.Map;

/**
 * EventBus 网路中心
 * Created by zhangJianqiu on 2016/9/12.
 */
public class TimeSharingHistoryEvent extends Event {

    private Context mContext;

    public TimeSharingHistoryEvent(Context context) {
        this.mContext = context;
    }

    public void getObservableTimeSharing(String type) {
        Map<String, String> params = new HashMap();
        String interfaceHttpUrl = "";
    }
}
