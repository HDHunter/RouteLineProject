package com.hunty.widget.event;


/**
 * 网络请求，数据封装
 * <p/>
 */
public class Event {

    public Event(int id) {
        setId(id);
    }

    public Event() {
    }

    /**
     * Old Code Style.Log mark.
     */
    public String TAG = getClass().getSimpleName();
    /**
     * 普通id
     */
    private int id = 0;
    /**
     * 响应数据体
     */
    private Object object = new Object();
    private boolean isSuccess = false;
    private boolean isMore = false;//是否是加载更多
    private String mGuessType = "";//仅仅在投资风向标的地方使用

    public String getTAG() {
        return TAG;
    }

    public void setTAG(String TAG) {
        this.TAG = TAG;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(boolean success) {
        isSuccess = success;
    }//previous guy did

    public boolean isMore() {
        return isMore;
    }

    public void setIsMore(boolean more) {
        isMore = more;
    }//previous guy did

    public String getmGuessType() {
        return mGuessType;
    }

    public void setmGuessType(String mGuessType) {
        this.mGuessType = mGuessType;
    }
}
