package com.hunty.widget.charts;

/**
 * Candle Data handle,then pass to UI thread.
 * Created by zhangjianqiu on 16-8-18.
 */
public class CandleParamResult {

    String[] mDates;//30`s day.
    float[][] mData;
    float max;
    float min;
    String firstCandleDate;//Not first Data.in case of 60`s for web.
    String lastcandleDate;

    public String[] getmDates() {
        return mDates;
    }

    public void setmDates(String[] mDates) {
        this.mDates = mDates;
    }

    public float[][] getmData() {
        return mData;
    }

    public void setmData(float[][] mData) {
        this.mData = mData;
    }

    public float getMax() {
        return max;
    }

    public void setMax(float max) {
        this.max = max;
    }

    public float getMin() {
        return min;
    }

    public void setMin(float min) {
        this.min = min;
    }

    public String getFirstCandleDate() {
        return firstCandleDate;
    }

    public void setFirstCandleDate(String firstCandleDate) {
        this.firstCandleDate = firstCandleDate;
    }

    public String getLastcandleDate() {
        return lastcandleDate;
    }

    public void setLastcandleDate(String lastcandleDate) {
        this.lastcandleDate = lastcandleDate;
    }
}
